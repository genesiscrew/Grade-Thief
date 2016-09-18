package game.floor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


import items.Door;

public class TileMap {
	Tile[][] TileMapper;

	public TileMap(Tile[][] TileMap) {
		this.TileMapper = TileMap;
	}

	public Tile[][]  getTileMap() {

		return this.TileMapper;
	}
/*
	public Tile[][] createTileMap(String f) {
		String map = convertFileToTileString(f);
		return convertStringToTileMap(map);

	}
	public String convertFileToTileString(String fileName) {

		String fileString = null;

		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			fileString = sb.toString();
			br.close();

		} catch (Exception e) { e.printStackTrace(); }

		return fileString;
	}

	public Tile[][] convertStringToTileMap(String Tiles) {

		Scanner s = new Scanner(Tiles);

		String scode = null, swidth = null, sheight = null;



		int code = Integer.parseInt(s.nextLine());
		int width = Integer.parseInt(s.nextLine());
		int height = Integer.parseInt(s.nextLine());

		System.out.println(code + " " + width + " " + height);

		//int width = s.nextInt();
		//int height = s.nextInt();
		s.close();

		TileMapper = new Tile[width][height];
		Tiles = Tiles.substring(Tiles.indexOf('.') + 1); // concatenate dimensions now that is is loaded

		int count = 0;

				for (int y = 0; y < height; y++) {
						for (int x = 0; x < width; x++){


							char c = Tiles.charAt(count);

							Location loc = new Location(x,y);

						if (c == '*') {
							WallTile w = new WallTile();
							w.setLocation(loc);
							TileMapper[x][y] = w;
							System.out.println(w.getName());
						} else if ( c == '-') {
							EmptyTile e = new EmptyTile();
							e.setLocation(loc);
							TileMapper[x][y] = e;
							System.out.println(e.getName());
						} else if ( c == 'D') {
							DoorTile d = new DoorTile();
							d.setLocation(loc);
							d.setDoor(Door.getDoor(code)); // gets the door with the given code
							TileMapper[x][y] = d;
							System.out.println(d.getName());
						}

					

						count++;
				}
	}

					return TileMapper;

	}
	*/
	public  Tile[][] createTileMap(String filename) throws IOException {
		//read the door location data
	


		FileReader fr = new FileReader(filename);
		BufferedReader br = new BufferedReader(fr);
		ArrayList<String> lines = new ArrayList<String>();
		int width = -1;
		String line;
		int counter = 0;
		while((!(line = br.readLine()).isEmpty())) {
			counter++;
			System.out.println(line);
			lines.add(line);

			// now sanity check

			if(width == -1) {
				width = line.length();
			} else if(width != line.length()) {
				throw new IllegalArgumentException("Input file \"" + filename + "\" is malformed; line " + lines.size() + " incorrect width.");
			}
		}


		TileMapper = new Tile[width][lines.size()];
		
		for(int y=0;y!=lines.size();++y) {
			line = lines.get(y);

			for(int x=0;x!=width;++x) {
				Location loc = new Location(x,y);
				char c = line.charAt(x);
				switch (c) {
					case '-' :
						EmptyTile e = new EmptyTile();
						e.setLocation(loc);
						TileMapper[x][y] = e;
						break;			
					case '*' :
						WallTile w = new WallTile();
						w.setLocation(loc);
						TileMapper[x][y] = w;
						break;
					case 'D' :
						DoorTile d = new DoorTile();
						d.setLocation(loc);
						d.setDoor(Door.getDoor(001)); // gets the door with the given code
						TileMapper[x][y] = d;
						break;
				


				}


			}


		}


        
		return TileMapper;
	}


}

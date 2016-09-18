package game.floor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


import items.Door;

public class TileMap {

	Tile[][] TileMap;
	int TileMapWidth = 0;
	int TileMapHeight = 0;
	int optionalCode = -1; // default value of map does not contain a door
	private List<Location> doorLocations = new ArrayList<Location>();

	public int getMapWidth() { return TileMapWidth; }
	public int getMapHeight() { return TileMapHeight; }
	public int getOptionalCode() { return optionalCode; }


	public TileMap(Tile[][] TileMap) {
		this.TileMap = TileMap;
	}


	public Tile[][]  getTileMap() {
		return this.TileMap;
	}



	public TileMap createTileMap(String f) {
		String map = convertFileToTileString(f);
		return convertStringToTileMap(map);

	}

	public void addDoors(Location doorLocation) {
		this.doorLocations.add(doorLocation);
	}

	public List<Location> getDoors() {
		return this.doorLocations;
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

	public TileMap convertStringToTileMap(String Tiles) {

		Scanner s = new Scanner(Tiles);

		String scode = null, swidth = null, sheight = null;



		int code = Integer.parseInt(s.nextLine());
		int width = Integer.parseInt(s.nextLine());
		int height = Integer.parseInt(s.nextLine());

		 List<Location> doorLocs = new ArrayList<Location>();


		TileMap TileMapper = new TileMap(new Tile[width][height]);

		 TileMapper.optionalCode = code;
		TileMapper.TileMapWidth = width;
		TileMapper.TileMapHeight = height;

//		System.out.println(code + " " + width + " " + height);

		//int width = s.nextInt();
		//int height = s.nextInt();
		s.close();


		Tiles = Tiles.substring(Tiles.indexOf('.') + 2); // concatenate dimensions now that is is loaded


		int count = 0;

				for (int y = 0; y < height; y++) {
						for (int x = 0; x < width+1; x++){

							char c = Tiles.charAt(count);

							Location loc = new Location(x,y);
						//	System.out.println(loc);

						  if ( c == 'x') {
							EmptyTile e = new EmptyTile();
							e.setLocation(loc);
							TileMapper.TileMap[x][y] = e;
						} else if ( c == 'd') {

							System.out.println("adding door at " + loc.toString() );
							DoorTile d = new DoorTile();
							d.setLocation(loc);
							d.setDoor(Door.getDoor(code)); // gets the door with the given code
							TileMapper.TileMap[x][y] = d;
							doorLocs.add(loc); // adds the door to list of door locations


						}



						count++;
				}
	}
				System.out.println("=============================");
				System.out.println("before returning tile mapper we hav edoors " + doorLocs.size());
				TileMapper.doorLocations = doorLocs;
					return TileMapper;

	}


}
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
/**
 * @author Stefan Vrecic
 * Class keeps track of a room, including its height, width and the details of the room
 *
 */
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

	/**
	 * @param fileName
	 * @return -- converts file into string; used to load rooms
	 */
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

	/**
	 * @param Tiles -- the file as a string
	 * @return -- a map of tiles, including door locations etc
	 *
	 * Method used to load a file into a room
	 */
	public TileMap convertStringToTileMap(String Tiles) {

		Scanner s = new Scanner(Tiles);

		//	String scode = null, swidth = null, sheight = null;

		int code = Integer.parseInt(s.nextLine());
		int width = Integer.parseInt(s.nextLine());
		int height = Integer.parseInt(s.nextLine());

		List<Location> doorLocs = new ArrayList<Location>();

		TileMap TileMapper = new TileMap(new Tile[width][height]);

		TileMapper.optionalCode = code;
		TileMapper.TileMapWidth = width;
		TileMapper.TileMapHeight = height;

		s.close();

		Tiles = Tiles.substring(Tiles.indexOf('.') + 2); // concatenate dimensions now that they are loaded

		int count = 0;

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width+1; x++){

				char c = Tiles.charAt(count);

				Location loc = new Location(x,y);

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
		TileMapper.doorLocations = doorLocs;
		return TileMapper;

	}

}

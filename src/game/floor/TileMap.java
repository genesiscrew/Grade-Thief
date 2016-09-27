package game.floor;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import javax.swing.plaf.synth.SynthSpinnerUI;

import gui.*;
import items.Container;
import items.Door;
import items.Item;
import items.KeyDraw;
import items.Keys;

/**
 * @author Stefan Vrecic
 * Class keeps track of a room, including its height, width and the details of the room
 *
 */
public class TileMap {

	private gui.Room room;
	private String items; // text file contains items as strings
	private Tile[][] TileMap;
	private int TileMapWidth = 0;
	private int TileMapHeight = 0;
	private int optionalCode = -1; // default value of map does not contain a door
	private List<Location> doorLocations = new ArrayList<Location>();

	public int getMapWidth() { return TileMapWidth; }
	public int getMapHeight() { return TileMapHeight; }
	public int getOptionalCode() { return optionalCode; }

	public TileMap(Tile[][] TileMap, gui.Room room) {
		this.TileMap = TileMap;
		this.room = room;
	}

	public Tile[][]  getTileMap() {
		return this.TileMap;
	}

	public void setTile(int x, int y, Tile t) {
		this.TileMap[x][y] = t;
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

		TileMap TileMapper = new TileMap(new Tile[width][height], null);

		TileMapper.optionalCode = code;
		TileMapper.TileMapWidth = width;
		TileMapper.TileMapHeight = height;

		String items = Tiles.substring(Tiles.lastIndexOf("*") + 2);
		TileMapper.setItems(items);
		//  System.out.println(items);
		s.close();

		Tiles = Tiles.substring(Tiles.indexOf('.') + 2); // concatenate dimensions now that they are loaded

		int count = 0;

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width+1; x++){

				char c = Tiles.charAt(count);

				Location loc = new Location(x,y);
				if ( c == 'w') {
					WallTile W = new WallTile();
					W.setLocation(loc);
					TileMapper.TileMap[x][y] = W;
				}
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

	public void populateRoom(gui.Room room, String String, Container container) {

		Scanner sc = new Scanner(String);
		TileMap tileMap = room.getTileMap();

		while (sc.hasNextLine()) {
			if (!sc.equals("")) {
				int id;
				if (sc.hasNextInt())
					id  = sc.nextInt();
				else {
					sc.close();
					return;
				}
				// 1 6 1 0 10 10 10 255 0 0 container1 C 0 3
				int x = sc.nextInt();
				int y = sc.nextInt();

				int z = sc.nextInt(); // test
// 6 1 0 1 10 10 10 container1 C 0 3 255 0 0
				Tile tile = room.getTileMap().getTileMap()[x][y];


				//test
				int w = sc.nextInt();
				int h = sc.nextInt();
				int l = sc.nextInt();
				int red = sc.nextInt();
				int green = sc.nextInt();
				int blue = sc.nextInt();

				//
				String name = sc.next();
				String type = sc.next();
				// FOUND CONTAINER
				if (type.equals("C")) {
					// container found
					int keyID = sc.nextInt();
				//	System.out.println(keyID);
					int itemCount = sc.nextInt();
				//	System.out.println(itemCount);
					// while (itemCount > 0) {
					// loop from each item line
					String line = sc.nextLine();
					StringBuilder sb = new StringBuilder();
					while (!line.equals(".")) {
						sb.append(line);
						sb.append(System.lineSeparator());
						try {
							line = sc.nextLine();
						} catch (NoSuchElementException e) {break;}
					}
					String fileString = sb.toString();

					fileString = fileString.substring(1, fileString.length()); //trim the first blank space
					System.out.println(fileString + "fileString");

					Container con = new Container(id, null, "box", keyID,0,0,0,0,0,0,new Color(0,0,0));
					populateRoom(room, fileString, con);

					if (container != null) {
						System.out.println("container :: " + name + "  is contained by container ");
						System.out.println(con.toString());
						container.setContainedContainer(con);
						sc.close();
						return ; // should return by default anyway, code placed for readability

						// TODO: if method called by container item, then add item into container list

					}
					// if container called method then we add this container into the calling container, then we add it the tile map
					else {
						System.out.println("container :: " + name + "  is not contained any containers  " + name);
						EmptyTile E = (EmptyTile) tile;
						E.addObjectToTile(con);
						E.setOccupied(); System.out.println("e occupied" + E.isOccupied);
						tileMap.setTile(x, y, E);

						// public Laptop(double x, double y, double z, double width, double length, double height, Color c) {
					}

				}
				// FOUND KEY
				else if (type.equals("K")) {
					System.out.println("adding key??");
					// Key found
					int keyID = sc.nextInt();
					System.out.println(keyID);
					// TODO: create key item and add it to floor tile map

					EmptyTile E = (EmptyTile) tile;
					Keys K = new Keys(id, type, keyID);
					E.addObjectToTile(K);
					E.setOccupied(); System.out.println("e occupied" + E.isOccupied);
					tileMap.setTile(x, y, E);


					if (container != null) {
						//container.addItem(K);
						System.out.println("container size + " + container.getItems().size());
						// TODO: if method called by container item, then add item into container list
					}

				}
				// FOUND ITEM
				else {
					System.out.println("adding item??");
					EmptyTile E = (EmptyTile) tile;
					//Item i = new Item(id, type);

					// normal item found
					// TODO: create normal item and add it to floor tile map
					System.out.println("w====================================================");
					//System.out.println(container.toString());
					System.out.println("=addddddddded itemmmmmmmmmmmmm check contianer");

					if (container != null) {
						System.out.println("=====================================");
						//container.addItem(i);
//						E.addObjecttoTile(container);
//						E.setOccupied();
//						tileMap.setTile(x, y, E);

						System.out.println("adding item to ocntainer ggggggg");
						System.out.println("container size + " + container.containedItems.size());
						// TODO: if method called by container item, then add item into container list
					} else {
						// TODO E.addObjectToTile(i);
						E.setOccupied();
				//		System.out.println("e occupied" + E.isOccupied);
						tileMap.setTile(x, y, E);

						room.addDrawableItems(new KeyDraw(id,type,10*x, 10*y, z, w, h, l, new Color(red, green, blue)));
					}

				}
			} else {
				sc.close();
				return;
			}
		}

	}
	public String getItems() {
		return items;
	}
	public void setItems(String items) {
		this.items = items;
	}
	public gui.Room getRoom() {
		return room;
	}
	public void setRoom(gui.Room room) { // don't use?
		this.room = room;
	}

}
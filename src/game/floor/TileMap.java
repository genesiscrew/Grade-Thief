package game.floor;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import model.characters.GuardBot;
import model.items.Chair;
import model.items.Container;
import model.items.Dog;
import model.items.Door;
import model.items.Item;
import model.items.KeyDraw;
import model.items.Keys;
import model.items.Laptop;
import model.items.MetalSheet;
import model.items.Table;

/**
 * @author Stefan Vrecic Class keeps track of a room, including its height,
 *         width and the details of the room
 *
 */
public class TileMap {

	private view.Room room;
	private String items; // text file contains items as strings
	private Tile[][] TileMap;
	private int TileMapWidth = 0;
	private int TileMapHeight = 0;
	private int optionalCode = -1; // default value of map does not contain a
	// door
	private List<Location> doorLocations = new ArrayList<Location>();
	private List<Integer> lockedDoorNumbers = new ArrayList<Integer>();

	/*
	 * returns the width of the room parsed in from txt file
	 */
	public int getMapWidth() {
		return TileMapWidth;
	}
	/*
	 * returns the height of the room parsed in from txt file
	 */
	public int getMapHeight() {
		return TileMapHeight;
	}


	public TileMap(Tile[][] TileMap, view.Room room) {
		this.TileMap = TileMap;
		this.room = room;
	}
	/*
	 * returns a 2D array of Tiles contained in the tileMap (part of the Room)
	 */
	public Tile[][] getTileMap() {
		return this.TileMap;
	}

	/**
	 * sets the specified Tile on the TileMap to be of a certain Tile Type, t
	 * @param x -- specified X
	 * @param y -- specified Y
	 * @param t -- the tile for the map to be set to
	 */
	public void setTile(int x, int y, Tile t) {
		this.TileMap[x][y] = t;
	}

	/**
	 * returns the integer of the locked door (ie from 0 - (int) numberOfDoors)
	 * @return
	 */
	public List<Integer> getLockedDoors() {
		return this.lockedDoorNumbers;
	}
	/**
	 * creates a tileMap. Converts a file into a String; which is scanned and parsed as a TileMap
	 * @param f -- the file name as a string
	 * @return
	 */
	public TileMap createTileMap(String f) {
		String map = convertFileToTileString(f);
		// System.out.println("creatTileMap method");
		return convertStringToTileMap(map);
	}

	/*
	 * Adds a door at given location
	 */
	public void addDoors(Location doorLocation) {
		this.doorLocations.add(doorLocation);
	}

	/*
	 * Gets the locations of all the doors as an ArrayList
	 */

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

		} catch (Exception e) {
			e.printStackTrace();
		}

		return fileString;
	}

	/**
	 * @param Tiles
	 *            -- the file as a string
	 * @return -- a map of tiles, including door locations etc
	 *
	 *         Method used to load a file into a room
	 */
	public TileMap convertStringToTileMap(String Tiles) {

		Scanner s = new Scanner(Tiles);


		int code = Integer.parseInt(s.nextLine()); // redunant old logic -- name of room. Not used.
		int width = Integer.parseInt(s.nextLine());
		int height = Integer.parseInt(s.nextLine());

		List<Location> doorLocs = new ArrayList<Location>();

		TileMap tileMap = new TileMap(new Tile[width][height], null);

		tileMap.optionalCode = code;
		tileMap.TileMapWidth = width;
		tileMap.TileMapHeight = height;

		String items = Tiles.substring(Tiles.lastIndexOf("*") + 2);
		tileMap.setItems(items); // parses in the part of the txt file that are the items

		s.close();

		Tiles = Tiles.substring(Tiles.indexOf('.') + 2); // concatenate
		// dimensions now
		// that they are
		// loaded
		int count = 0;

		// iterates through the txt map and adds to the tileMap based on which tile (char) has been parsed
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width + 1; x++) {

				char c = Tiles.charAt(count);

				Location loc = new Location(x, y);
				if (c == 'w') {
					WallTile W = new WallTile();
					W.setLocation(loc);
					tileMap.TileMap[x][y] = W;
				}
				if (c == 'x' || c == 'e') {
					EmptyTile e = new EmptyTile();
					e.setLocation(loc);
					tileMap.TileMap[x][y] = e;
				}

				if (c == 'r') {
					RoomTile r = new RoomTile();
					r.setLocation(loc);
					tileMap.TileMap[x][y] = r;
				}

				else if (c == 'd') {;

				doorLocs.add(loc); // adds the door to list of door


				DoorTile d = new DoorTile();
				d.setLocation(loc);
				Door placeDoor = new Door(doorLocs.size(), null, 0,0,0,0,0,0,null);
				d.setDoor(placeDoor); // gets the door with the
				// given code
				tileMap.TileMap[x][y] = d;

				}
				count++;
			}
		}

		tileMap.doorLocations = doorLocs;
		// System.out.println("tileMapper width " + tileMap.TileMapWidth);

		return tileMap;

	}

	/**
	 *
	 * @param room -- the room to be populated with items
	 * @param String -- a String parsed in which contains details of ALL the items
	 * @param container -- a container to recursively add items to. Initially called as null
	 */
	public void populateRoom(view.Room room, String String, Container container) {
		// int count = 0;
		System.out.println(String);
		Scanner sc = new Scanner(String);
		TileMap tileMap = room.getTileMap();

		for (Location l: tileMap.getDoors()) {
			DoorTile dt = (DoorTile) tileMap.TileMap[l.locX()][l.locY()];
			Door d = dt.getDoor();
			room.addDoor(d);
		}

		while (sc.hasNextLine()) {
			if (!sc.equals("")) {
				int id = 0;


				if (sc.hasNextInt()) {
					id = sc.nextInt();
					System.out.println(id);
				}



				else {
					sc.close();
					return;
				}
				if (id == -9) {
					sc.next();
					while (sc.hasNextInt()) {
						int doorToLock = sc.nextInt();

						DoorTile d = (DoorTile) tileMap.TileMap[doorLocations.get(doorToLock).locX()][doorLocations.get(doorToLock).locY()];
						d.getDoor().setLock();

					}
					System.out.println(sc.nextLine() + " ======================== ");
					continue;
				}

				int x = sc.nextInt();
				int y = sc.nextInt();
				int z = sc.nextInt();
				Tile tile = room.getTileMap().getTileMap()[x][y];

				int w = sc.nextInt();
				int h = sc.nextInt();
				int l = sc.nextInt();
				int red = sc.nextInt();
				int green = sc.nextInt();
				int blue = sc.nextInt();

				sc.next(); sc.next();

				String type = sc.next();

				if (type.equals("D")) { // Door
					// System.out.println("===========================
					// ddddddddddddddddddddddd");
					int doorID = sc.nextInt();
					DoorTile DT = (DoorTile) tile;
					Door D = new Door(id, type, 10 * x, 10 * y, z, w, h, l, new Color(red, green, blue));
					DT.setDoor(D);
					tileMap.setTile(x, y, DT);
					//					room.addItemToRoom(
					//							new DoorDraw(id, type, 10 * x, 10 * y, z, w, h, l, new Color(red, green, blue), D));

				}
				/*if (type.equals("GuardBot")) { // Door
					// System.out.println("===========================
					// ddddddddddddddddddddddd");
					int moveStrategy = sc.nextInt();
					int arrSize = sc.nextInt();
					int[] distances = new int[arrSize];
					for (int i = 0; i < arrSize; i++) {

						distances[i] = sc.nextInt();
					}
					int floorNo = sc.nextInt();

					EmptyTile E = (EmptyTile) tile;
					GuardBot guard = new GuardBot(id, type, moveStrategy, distances, floorNo, x * 10, y * 10, z, w * 2,
							l * 2, h * 5, new Color(red, green, blue));
					E.addObjectToTile(guard);
					E.setOccupied();
					System.out.println("e occupied" + E.isOccupied);
					tileMap.setTile(x, y, E);
					// room.addItemToRoom(guard);
					room.addGuardtoRoom(guard);

				}
				*/
				// FOUND CONTAINER
				else if (type.equals("C")) {
					// container found
					int keyID = sc.nextInt();
					// System.out.println(keyID);
					int itemCount = sc.nextInt();
					// System.out.println(itemCount);
					// while (itemCount > 0) {
					// loop from each item line
					String line = sc.nextLine();
					StringBuilder sb = new StringBuilder();
					while (!line.equals(".")) {
						sb.append(line);
						sb.append(System.lineSeparator());
						try {
							line = sc.nextLine();
						} catch (NoSuchElementException e) {
							break;
						}
					}
					String fileString = sb.toString();

					fileString = fileString.substring(1, fileString.length()); // trim
					// the
					// first
					// blank
					// space
					//System.out.println(fileString + "fileString");

					Container con = new Container(id, null, "box", keyID, 0, 0, 0, 0, 0, 0, new Color(0, 0, 0));
					populateRoom(room, fileString, con);

					if (container != null) {
						//System.out.println("container :: " + drawableItem + "  is contained by container ");
						System.out.println(con.toString());
						//container.setContainedContainer(con);
						sc.close();
						return; // should return by default anyway, code placed
						// for readability

						// TODO: if method called by container item, then add
						// item into container list

					}
					// if container called method then we add this container
					// into the calling container, then we add it the tile map
					else {
						//System.out.println(
						//	"container :: " + drawableItem + "  is not contained any containers  " + drawableItem);
						EmptyTile E = (EmptyTile) tile;
						E.addObjectToTile(con);
						E.setOccupied();
						//System.out.println("e occupied" + E.isOccupied);
						tileMap.setTile(x, y, E);

						// public Laptop(double x, double y, double z, double
						// width, double length, double height, Color c) {
					}

				}
				// FOUND KEY
				else if (type.equals("K")) {
					//System.out.println("adding key??");
					// Key found
					int keyID = sc.nextInt();
					System.out.println(keyID);
					// TODO: create key item and add it to floor tile map

					EmptyTile E = (EmptyTile) tile;
					Keys K = new Keys(id, type, keyID);
					E.addObjectToTile(K);
					E.setOccupied();
					//System.out.println("e occupied" + E.isOccupied);
					tileMap.setTile(x, y, E);

					if (container != null) {
						// container.addItem(K);
						//System.out.println("container size + " + container.getItems().size());
						// TODO: if method called by container item, then add
						// item into container list
					}

				}
				// FOUND ITEM
				else {
					EmptyTile E = (EmptyTile) tile;
					Item drawItem = null;


					// adds
					switch (type) {
					case "KeyDraw":
						drawItem = new KeyDraw(id, type, 10 * x, 10 * y, z, w, h, l, new Color(red, green, blue));
						break;

					case "Table":
						drawItem = new Table(id, type, 10 * x, 10 * y, z, w, h, l, new Color(red, green, blue));
						break;

					case "MetalSheet":
						drawItem = new MetalSheet(id, type, 10 * x, 10 * y, z, w, h, l, new Color(red, green, blue));
						break;

					case "Chair":
						drawItem = new Chair(id, type, 10 * x, 10 * y, z, w, h, l, new Color(red, green, blue));
						break;

					case "Dog":
						drawItem = new Dog(id, type, 10 * x, 10 * y, z, w, h, l, new Color(red, green, blue));
						break;

					case "Laptop":
						drawItem = new Laptop(id, type, 10 * x, 10 * y, z, w, h, l, new Color(red, green, blue));
						break;
					}

					if (drawItem != null) {
						System.out.println("adding item!");

					}
					room.addItemToRoom(drawItem);
					//System.out.println("==================================");
					//System.out.println("adding item " + type);
					//System.out.println("==================================");
					// Item i = new Item(id, type);

					// normal item found
					// TODO: create normal item and add it to floor tile map
					//System.out.println("w====================================================");
					// System.out.println(container.toString());
					//System.out.println("=addddddddded itemmmmmmmmmmmmm check contianer");

					if (container != null) {
						//System.out.println("=====================================");
						// container.addItem(i);
						// E.addObjecttoTile(container);
						// E.setOccupied();
						// tileMap.setTile(x, y, E);

						// System.out.println("adding item to ocntainer
						// ggggggg");
						// System.out.println("container size + " +
						// container.containedItems.size());
						// TODO: if method called by container item, then add
						// item into container list
					} else {
						// TODO E.addObjectToTile(i);
						E.setOccupied();
						// System.out.println("e occupied" + E.isOccupied);
						tileMap.setTile(x, y, E);

						// Item drawItem = new
						if (drawItem != null)
							room.addItemToRoom(drawItem);
						E.addObjectToTile(drawItem);
						// room.addItemToRoom(new KeyDraw(id,type,10*x, 10*y, z,
						// w, h, l, new Color(red, green, blue)));
					}

				}
			} else {
				sc.close();
				return;
			}
		}
		//sc.close();
	}


	/**
	 * gets all the items to be parsed into populateRoom
	 * @return
	 */
	public String getItems() {
		return items;
	}

	/**
	 *  String of all items to be parsed into populateRoom
	 * @param items
	 */
	public void setItems(String items) {
		this.items = items;
	}

	/**
	 * returns the room the tileMap is part of
	 * @return
	 */
	public view.Room getRoom() {
		return room;
	}

	/**
	 * sets the room the tileMap is part of
	 * @param room
	 */
	public void setRoom(view.Room room) { // don't use?
		this.room = room;
	}

}
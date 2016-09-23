package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import characters.Guard;
import characters.Player;
import game.floor.EmptyTile;
import game.floor.Floor;
import game.floor.Location;
import game.floor.Tile;
import game.floor.TileMap;
import items.Container;
import items.Direction;
import items.Distance;
import items.GameObject;
import items.Interactable;
import items.Item;
import items.Keys;
import items.Movable;
import items.Direction.Dir;

/**
 *
 * @author Hamid Abubakr this class contains the game logic ie multiplayer ,
 *         player interaction with game objects
 */
public class Game {
	// the tile map
	public TileMap board;
	public Tile[][] TileMap;
	public Floor[] floors;

	public
	// list of all game objects
	static List<GameObject> containedItems = new ArrayList<GameObject>();
	private ArrayList<Character> players = new ArrayList<Character>();

	public Game() throws IOException {
		floors = new Floor[1];
	}

	public TileMap getGameMap() {
		return board;

	}

	/**
	 * for debugging purposes, draws map into console
	 */
	public void drawBoard() {

		int count = 0;
		for (int y = 0; y != this.board.getTileMap()[y].length; y++) {
			for (int x = 0; x != this.board.getTileMap().length; x++) {

				System.out.printf(this.board.getTileMap()[x][y].getName());
			}
			System.out.println();

		}

	}

	/**
	 * this method sets up guards within the map
	 *
	 * @param integer
	 *            parameter specifying floorNumber where guards will be
	 *            activated
	 */
	public void setupGuards(int floorNumber) {
		Guard gaurd1 = new Guard(0, "guard1");
		Guard guard2 = new Guard(1, "guard2");
		Player player = new Player(0, "H");
		Distance dist = new Distance(1);
		// set character location
		gaurd1.setCharacterLocation(7, 7);
		// add player object to map
		((EmptyTile) Game.this.getGameMap().getTileMap()[7][7]).addObjecttoTile(player);
		Game.this.drawBoard();

		// set gaurd's location
		gaurd1.setCharacterLocation(0, 7);
		guard2.setCharacterLocation(24, 7);
		// add guard object to tile on map
		((EmptyTile) Game.this.getGameMap().getTileMap()[0][7]).addObjecttoTile(gaurd1);
		drawBoard();
		((EmptyTile) Game.this.getGameMap().getTileMap()[24][7]).addObjecttoTile(guard2);
		drawBoard();

		// create a thread for the guard, so that he can move within map
		// independent of player

		Thread guardThread = new Thread() {
			public void run() {
				// move the guard in a fixed loop, once he reaches certain
				// coordinate on the Map, change destination
				// if () {}
				// gaurd will keep moving
				Direction dir = new Direction(Dir.EAST);
				while (!gaurd1.checkforIntruder(Game.this, dir)) {
					// update direction of guard based on hardcoded route
					// through Tilemap

					// move the guard to new location
					// remove gaurd as object from previous empty tile
					((EmptyTile) Game.this.getGameMap().getTileMap()[gaurd1.getCharacterLocation().row()][gaurd1
							.getCharacterLocation().column()]).resetEmptyTile();
					gaurd1.move(dir, dist);
					try {
						sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// add the gaurd as object to the new empty tile
					((EmptyTile) Game.this.getGameMap().getTileMap()[gaurd1.getCharacterLocation().row()][gaurd1
							.getCharacterLocation().column()]).addObjecttoTile(gaurd1);

					// draw board intp console for debugging purposes
					Game.this.drawBoard();

				}

			}
		};

		Thread guardThread1 = new Thread() {
			public void run() {
				// move the guard in a fixed loop, once he reaches certain
				// coordinate on the Map, change destination
				// if () {}
				// gaurd will keep moving

				Direction dir = new Direction(Dir.WEST);
				while (!guard2.checkforIntruder(Game.this, dir)) {
					// update direction of guard based on hardcoded route
					// through Tilemap

					// move the guard to new location
					// remove gaurd as object from previous empty tile
					((EmptyTile) Game.this.getGameMap().getTileMap()[guard2.getCharacterLocation().row()][guard2
							.getCharacterLocation().column()]).resetEmptyTile();
					guard2.move(dir, dist);
					try {
						sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// add the gaurd as object to the new empty tile
					((EmptyTile) Game.this.getGameMap().getTileMap()[guard2.getCharacterLocation().row()][guard2
							.getCharacterLocation().column()]).addObjecttoTile(guard2);

					// draw board intp console for debugging purposes
					Game.this.drawBoard();
				}

			}
		};
		// start the guard movement, thread stops running when intruder caught
		guardThread.start();
		guardThread1.start();

	}

	public void setupPlayers() {
		// TODO Auto-generated method stub

	}

	/**
	 * this method checks whether a move to certain tile is valid
	 *
	 * @param targetLocation
	 *            parameter specifies the tile the player wants to move into
	 *
	 * @return true/false
	 */
	public boolean isValidMove(Location targetLocation) {

		return !this.getGameMap().getTileMap()[targetLocation.row()][targetLocation.column()].occupied()
				&& this.getGameMap().getTileMap()[targetLocation.row()][targetLocation.column()] instanceof EmptyTile;

	}

	/**
	 * method that checks if item is interactable
	 *
	 * @param parameter
	 *            is item we need to check if can be interacted with
	 * @return true/false
	 */
	public boolean isInteractableItem(GameObject item) {

		return item instanceof Interactable;
	}

	/**
	 * method that checks if item is movable
	 *
	 * @param parameter
	 *            is item we need to check if can be moved
	 * @return true/false
	 */
	public boolean isMovableItem(GameObject item) {

		return item instanceof Movable;
	}

	/**
	 *
	 * @param parameter
	 *            is item we need to check if can be moved
	 * @return true/false
	 */
	public boolean isContainerItem(GameObject item) {

		return item instanceof Container;
	}

	/**
	 *this method allows player to pick up items from the game world
	 * @param the player who wants to pick up an item
	 * @param the item the user wants to pick up
	 * @return
	 */
	public boolean pickupItem(Player player, Item item) {

		if (this.isInteractableItem(item)) {
			if (this.isContainerItem(item)) {
				// item is a container so we do not pick it up, but we pick up
				// anything thing(if any) it contains and leave container
				// in the game world
				for (GameObject e : ((Container) item).getItems()) {
					// only adds an item to user's inventory if the inventory is
					// not full ie filled with less than 10 items
					if (player.getInventory().size() < 11) {
						player.addToInventory(item);
						((Container) item).getItems().remove(item);
					}
				}

			} else {
				// item is not a container so we pick it up and remove it from
				// tile in game world
				((EmptyTile) this.getGameMap().getTileMap()[item.getGameObjectLocation().row()][item
						.getGameObjectLocation().column()]).resetEmptyTile();
				// only adds an item to user's inventory if the inventory is not
				// full ie filled with less than 10 items
				if (player.getInventory().size() < 11) {
					player.addToInventory(item);
				}

			}
		}

		return false;
	}

	/**
	 *
	 * @the method returns a string that contains information about the item.
	 *      e.g. if item is container, it will return a string containing
	 *      description of container and of items it has inside
	 */
	public String inspectItem(GameObject item) {
		if (this.isContainerItem(item)) {
			// item is container so we list through all items within it
			if (((Container) item).getItems().isEmpty()) {
				return "This is a " + item.itemType() + ", it has no items inside it";

			} else {
				String output;
				output = "This is a " + item.itemType() + ", it has the following"
						+ ((Container) item).getItems().size() + "inside it:\n";
				for (int i = 0; i < ((Container) item).getItems().size(); i++) {
					if (((Container) item).getItems().get(i) instanceof Container) {
						this.inspectItem(((Container) item).getItems().get(i));
					}
					output += "Item 1: " + ((Container) item).getItems().get(i).itemType + "\n";

				}
				return output;
			}
		}
		return "This is a " + item.itemType();
	}

	/**
	 * method that drops an item from user's inventory into an emptyTile
	 *
	 * @param player
	 *            that will drop the item
	 * @param the
	 *            item to be dropped
	 * @return
	 */
	public boolean dropItem(Player player, Item item) {
		// first check if player is on an empty tile
		if (this.getGameMap().getTileMap()[player.getCharacterLocation().row()][player.getCharacterLocation()
				.column()] instanceof EmptyTile) {
			// next check it tile does not already include an item
			if (((EmptyTile) this.getGameMap().getTileMap()[player.getCharacterLocation().row()][player
					.getCharacterLocation().column()]).getObjectonTile() == null) {
				// next we add the item to the tile
				((EmptyTile) this.getGameMap().getTileMap()[player.getCharacterLocation().row()][player
						.getCharacterLocation().column()]).addObjecttoTile(item);
				// next we remove item from player inventory
				player.getInventory().remove(item);

			}

		}

		return false;
	}

	public void populateGameWorld(String file) {
		// TODO Auto-generated method stub

	}

	public void addFloor(Floor floor) {
		floors[0] = floor;
		populateFloor(floor, System.getProperty("user.dir") + "/src/map", null);
	}

	/**
	 * this mthod is responible for populating the floor corridors, excluding the rooms
	 * @param there floor where we will add the items
	 * @param the location of the text file will be reading the item data from
	 * @param the container object that has called this method (if any).
	 */
	public void populateFloor(Floor floor, String string, Container container) {

		File file = new File(string);
		Scanner sc = null;
		try {
			sc = new Scanner(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        String s = null;
		while (sc.hasNextLine()) {
			if (!sc.equals("")) {
				int x = sc.nextInt();
				System.out.println(x);
				int y = sc.nextInt();
				System.out.println(y);
				//Tile tile = floor.getFloorMap().getFloorTileMap()[x][y];
				int id = sc.nextInt();
				System.out.println(id);
				String name = sc.next();
				System.out.println(name);
				String type = sc.next();
				if (type.equals("C")) {
					// container found
					int keyID = sc.nextInt();
					System.out.println(keyID);
					int itemCount = sc.nextInt();
					System.out.println(itemCount);
					// while (itemCount > 0) {
					// loop from each item line
					String line = sc.nextLine();
					StringBuilder sb = new StringBuilder();
					while (!line.equals(".")) {
						sb.append(line);
						sb.append(System.lineSeparator());
						line = sc.nextLine();
					}
					String fileString = sb.toString();
					System.out.println(fileString);
					// Create temp file.
					File temp = null;

					try {
						temp = File.createTempFile("temp", ".txt");
						System.out.println("Temp file : " + temp.getAbsolutePath());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					try {
						fileString = fileString.substring(1, fileString.indexOf('.')); //trim the first blank space
						Files.write(Paths.get(temp.getAbsolutePath()), fileString.getBytes(),
								StandardOpenOption.APPEND);
					} catch (IOException e) {
						// exception handling left as an exercise for the reader
					}
                    Container con = new Container(id, null, "box", keyID);
                    if (container != null) {
						// TODO: if method called by container item, then add item into container list
					}
					this.populateFloor(floor, temp.getAbsolutePath(), con);
					temp.deleteOnExit();
					// itemCount--;
					// }

				} else if (type.equals("K")) {
					// Key found
					int keyID = sc.nextInt();
					System.out.println(keyID);
					// TODO: create key item and add it to floor tile map
					if (container != null) {
						// TODO: if method called by container item, then add item into container list
					}
					else {
						//EmptyTile E = (EmptyTile) tile;
						Keys i = new Keys(id, type, keyID);
						//E.addObjecttoTile(i);
						//E.setOccupied();
						//System.out.println("e occupied" + E.isOccupied);
						//floor.getFloorMap().getFloorTileMap()[x][y] = E;

					}

				} else {
					// normal item found
					// TODO: create normal item and add it to floor tile map
					if (container != null) {
						// TODO: if method called by container item, then add item into container list
					} else{
					//if method is not called by container item, then add the container into the tile map
					System.out.println("adding item??");
					//EmptyTile E = (EmptyTile) tile;
					Item i = new Item(id, type);
					//E.addObjecttoTile(i);
					//E.setOccupied();
				//	System.out.println("e occupied" + E.isOccupied);
				//	floor.getFloorMap().getFloorTileMap()[x][y] = E;
					}

				}
			}
		}
		sc.close();

	}

}

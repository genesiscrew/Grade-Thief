package characters;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import game.floor.EmptyTile;
import game.floor.Location;
import game.floor.Tile;
import items.Direction;
import items.Distance;
import items.GameObject;
import items.Item;
import model.Game;

public class Player extends Character implements KeyListener {

	Location characterLocation;
	private boolean inRoom = false;
	private ArrayList<Item> items = new ArrayList<Item>();
	private Game game;
	private Direction.Dir dir;
	int floorNo;



	public Player(int characterID, String characterName, Game game, int floorNo) {
		super(characterID, characterName);
		this.game = game;
		this.floorNo = floorNo;
		// TODO Auto-generated constructor stub
	}


	public void move(Direction dir, Distance d) {
		// altars details in tile in world
		// altars playerLocation
	}

	public void setDirection(Direction.Dir dir) {
		this.dir = dir;
	}



	public Location getCharacterLocation() {
		return super.getCharacterLocation();
	}

	public void setCharacterLocation(int x, int y) {
		super.setCharacterLocation(x,y);
	}

	// characterLocation = new Location(x, y);
	@Override
	public void characterInteraction(Character c) {
		// TODO Auto-generated method stub

	}
  /**
   * this method specifies how the player can interact with his world
   * two parameters, the object it wants to interact with, and the type of interaction
   * e.g. player wants to view
   */
	@Override
	public void objectInteraction(GameObject c) {


	}
    /**
     *
     * @returns the name of the player
     */
	public String getName() {
		return this.characterName;
	}

	public String toString() {
		return this.characterName;
	}
	/**
	 * adds an item to player's inventory
	 * @param parameter is the item to add
	 */
	public void addToInventory(Item item){
		this.items.add(item);

	}
	/**
	 *
	 * @returns the player's inventory
	 */
	public ArrayList<Item> getInventory(){
		return this.items;

	}


	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}


	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		/*
		if(code == KeyEvent.VK_RIGHT || code == KeyEvent.VK_KP_RIGHT) {
			game.player(characterID).moveRight();
		} else if(code == KeyEvent.VK_LEFT || code == KeyEvent.VK_KP_LEFT) {
			game.player(characterID).moveLeft();
		} else if(code == KeyEvent.VK_UP) {
			game.player(characterID)).moveUp();
		} else if(code == KeyEvent.VK_DOWN) {
			game.player(characterID).moveDown();
		}
		*/

		if(e.getKeyChar() == 'e') {
        if (this.checkifItemOnTile() != null) {
        	// item is on tile so we can interact with it
        	game.inspectItem(this.checkifItemOnTile());


        }

		}
		// redraw game board
		game.tick(true);

	}

/**
 * this method checks the tile directly in front of the player on the map, and returns it if any it is on it
 * @return
 */
	private Object checkifItemOnTile() {


		if(dir.equals(Direction.Dir.EAST)) {
			Tile tile = game.getFloor(floorNo).getFloorMap().getFloorTiles()
					[this.getCharacterLocation().row() + 1][this.getCharacterLocation().column()];
			if ( tile instanceof EmptyTile && tile.occupied()) {
				return tile.getObjectonTile();

			}

		}
		else if(dir.equals(Direction.Dir.WEST)) {
			Tile tile = game.getFloor(floorNo).getFloorMap().getFloorTiles()
					[this.getCharacterLocation().row() - 1][this.getCharacterLocation().column()];
			if ( tile instanceof EmptyTile) {
				return tile.occupied();

			}

		}
		else if(dir.equals(Direction.Dir.SOUTH)) {
			Tile tile = game.getFloor(floorNo).getFloorMap().getFloorTiles()
					[this.getCharacterLocation().row()][this.getCharacterLocation().column()+1];
			if ( tile instanceof EmptyTile) {
				return tile.occupied();

			}


		}

		else {
			Tile tile = game.getFloor(floorNo).getFloorMap().getFloorTiles()
					[this.getCharacterLocation().row()][this.getCharacterLocation().column()-1];
			if ( tile instanceof EmptyTile) {
				return tile.occupied();

			}


		}
		return false;

	}


	private void moveRight() {
		// TODO Auto-generated method stub

	}


	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}





}

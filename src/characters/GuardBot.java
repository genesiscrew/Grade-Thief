package characters;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
/**
 * created by hamid abubakr
 * creates a guard object that moves along  game floor map in certain path continuosly untill it detects an intruder
 */
import java.util.Collections;
import java.util.List;

import javax.swing.plaf.SliderUI;

import game.floor.EmptyTile;
import game.floor.Location;
import gui.Cube;
import gui.Drawable;
import gui.Polygon;
import items.Direction;
import items.Direction.Dir;
import items.Distance;
import items.GameObject;
import items.Item;
import model.Game;

public class GuardBot extends Item implements Drawable {

	Direction.Dir dir;
	ArrayList<Direction.Dir> directionList;
	int moveStrategy;
	GuardStrategy strategy;

	Location characterLocation;
	private int[] distance;
	private int floorNo;
	private java.util.List<Cube> cubes;
	protected double x;
	protected double y;
	protected double z;
	protected double width;
	protected double length;
	protected double height;
	protected Color color;

	/**
	 * Contructor for guard object
	 *
	 * @param characterID:
	 *            represents the the guard ID
	 * @param characterName:
	 *            represents the guard name
	 * @param moveStrategy:
	 *            representss the moving strategy to be used.
	 * @param distance:
	 *            an integer array that represents the distance moved as per x
	 *            and y coordinate.
	 * @param floorNo:
	 *            represents the floor number the guard belongs to
	 */
	public GuardBot(int itemID, String itemType, int moveStrategy, int[] distance, int floorNo, double x,
			double y, double z, double width, double length, double height, Color c) {

		super(itemID, itemType, x, y, z, width, length, height, c);
		this.moveStrategy = moveStrategy;
		strategy = new GuardStrategy(moveStrategy);
		directionList = strategy.getDirectionList();
		this.dir = directionList.get(0);
		this.floorNo = floorNo;
		this.distance = distance;
		cubes = new ArrayList<>();
		this.x = x;
		this.y = y;
		this.z = z;
		this.width = width;
		this.length = length;
		this.height = height;
		this.color = c;
		  // First make the legs
        int legWidth = (int) (width / 2.5);
        int legHeight = (int) (height / 2);
        // first leg
        cubes.add(new Cube(x, y + (legWidth / 2), z, legWidth, legWidth, legHeight, c));
        // second leg
        cubes.add(new Cube(x + width - legWidth, y + (legWidth / 2), z, legWidth, legWidth, legHeight, c));

        // body
        cubes.add(new Cube(x, y, z + legHeight, width, width / 1.5, legHeight, c));

        // arms
        cubes.add(new Cube(x + width, y, z + legHeight + (legHeight / 2), width, width / 1.5, legHeight / 3, c));
        cubes.add(new Cube(x - width, y, z + legHeight + (legHeight / 2), width, width / 1.5, legHeight / 3, c));

        // head
        cubes.add(new Cube(x + (width / 4), y, z + (legHeight * 2), width / 2, width / 1.5, legHeight / 3, c));

	}



	/**
	 * this method moves the guard along a path specified by the move strategy,
	 * the method should keep running until an intruder is detected
	 */
	/*
	public void move(Game game) {

		while (!this.checkforIntruder(game)) {
			for (int i = 0; i < directionList.size(); i++) {

				// set Guard's direction
				this.dir = directionList.get(i);

				// now we move guard n steps
				for (int x = 0; x < distance[i]; x++) {

					// move the guard to new location
					// remove gaurd as object from previous empty tile
					((EmptyTile) game.getRoom(floorNo).getTileMap().getTileMap()[this.getCharacterLocation().row()][this
							.getCharacterLocation().column()]).resetEmptyTile();
					// move the guard one step based on direction
					if (this.dir.equals(Dir.EAST)) {

						this.setCharacterLocation(this.getCharacterLocation().row() + 1,
								this.getCharacterLocation().column());

					} else if (this.dir.equals(Dir.WEST)) {

						this.setCharacterLocation(this.getCharacterLocation().row() - 1,
								this.getCharacterLocation().column());

					} else if (this.dir.equals(Dir.NORTH)) {

						this.setCharacterLocation(this.getCharacterLocation().row(),
								this.getCharacterLocation().column() - 1);

					} else if (this.dir.equals(Dir.SOUTH)) {

						this.setCharacterLocation(this.getCharacterLocation().row(),
								this.getCharacterLocation().column() + 1);

					}

					// add the gaurd as object to the new empty tile
					((EmptyTile) game.getRoom(floorNo).getTileMap().getTileMap()[this.getCharacterLocation().row()][this
							.getCharacterLocation().column()]).addObjectToTile(this);
					game.tick(true);
					try {
						Thread.sleep(700);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					// draw board intp console for debugging purposes
					// game.drawBoard(this.floorNo);
					// Guard has move n moves equal to distance specified in
					// strategy, now we reverse the movement directions (if
					// required
					// e.g. north south path does not require reversal
					// but a north west path would need to go back to origin
					// through
					// east south) when the guard reaches last square in path
					// and
					// run the
					// move method again
					;

					if (x == distance[i] - 1 && i == (directionList.size() - 1)) {
						this.reverseStrategy();
						break;
					}
				}
			}

		}

	}
	*/

	public int getFloorNo() {

		return this.floorNo;
	}

	private void revertDistance() {

		for (int i = 0; i < distance.length / 2; i++) {
			int temp = distance[i];
			distance[i] = distance[distance.length - 1 - i];
			distance[distance.length - 1 - i] = temp;
		}

	}

	/**
	 * reverse the directions of move strategy
	 */
	private void reverseStrategy() {

		if (this.moveStrategy == 5) {
			// reverse movement
			this.moveStrategy = 9;
			strategy = new GuardStrategy(9);
			this.revertDistance();
			directionList = strategy.getDirectionList();
			this.dir = directionList.get(0);
		} else if (this.moveStrategy == 6) {
			// reverse movement
			this.moveStrategy = 10;
			strategy = new GuardStrategy(10);
			this.revertDistance();
			directionList = strategy.getDirectionList();
			this.dir = directionList.get(0);
		} else if (this.moveStrategy == 7) {
			// reverse movement by updating the arraylist of directions based on
			// new movement strategy
			this.moveStrategy = 11;
			strategy = new GuardStrategy(11);
			this.revertDistance();
			directionList = strategy.getDirectionList();
			this.dir = directionList.get(0);
		} else if (this.moveStrategy == 8) {
			// reverse movement by updating the arraylist of directions based on
			// new movement strategy
			this.moveStrategy = 12;
			strategy = new GuardStrategy(12);
			this.revertDistance();
			directionList = strategy.getDirectionList();
			this.dir = directionList.get(0);
		} else if (this.moveStrategy == 9) {
			// reverse movement by updating the arraylist of directions based on
			// new movement strategy
			this.moveStrategy = 5;
			strategy = new GuardStrategy(5);
			this.revertDistance();
			directionList = strategy.getDirectionList();
			this.dir = directionList.get(0);
		} else if (this.moveStrategy == 10) {
			// reverse movement by updating the arraylist of directions based on
			// new movement strategy
			this.moveStrategy = 6;
			strategy = new GuardStrategy(6);
			this.revertDistance();
			directionList = strategy.getDirectionList();
			this.dir = directionList.get(0);
		} else if (this.moveStrategy == 11) {
			// reverse movement by updating the arraylist of directions based on
			// new movement strategy
			this.moveStrategy = 7;
			strategy = new GuardStrategy(7);
			this.revertDistance();
			directionList = strategy.getDirectionList();
			this.dir = directionList.get(0);
		} else if (this.moveStrategy == 12) {
			// reverse movement by updating the arraylist of directions based on
			// new movement strategy
			this.moveStrategy = 8;
			strategy = new GuardStrategy(8);
			this.revertDistance();
			directionList = strategy.getDirectionList();
			this.dir = directionList.get(0);
		}

	}
	/*

	public Boolean checkforIntruder(Game game) {
		try {
			if (dir.equals(Dir.EAST)) {
				for (int i = 0; i < 6; i++) {
					if (game.getRoom(floorNo).getTileMap().getTileMap()[this.getCharacterLocation().row() + i][this
							.getCharacterLocation().column()] instanceof EmptyTile
							&& game.getRoom(floorNo).getTileMap().getTileMap()[this.getCharacterLocation().row()
									+ i][this.getCharacterLocation().column()].occupied()
							&& ((EmptyTile) game.getRoom(floorNo).getTileMap()
									.getTileMap()[this.getCharacterLocation().row() + i][this.getCharacterLocation()
											.column()]).getObjectonTile() instanceof Player) {
						System.out.println("we have found an intruder");
						return true;

					}
				}

			} else if (dir.equals(Dir.WEST)) {
				for (int i = 0; i < 6; i++) {

					if (game.getRoom(floorNo).getTileMap().getTileMap()[this.getCharacterLocation().row() - i][this
							.getCharacterLocation().column()] instanceof EmptyTile
							&& game.getRoom(floorNo).getTileMap().getTileMap()[this.getCharacterLocation().row()
									- i][this.getCharacterLocation().column()].occupied()
							&& ((EmptyTile) game.getRoom(floorNo).getTileMap()
									.getTileMap()[this.getCharacterLocation().row() - i][this.getCharacterLocation()
											.column()]).getObjectonTile() instanceof Player) {
						System.out.println("we have found an intruder");
						return true;

					}
				}

			} else if (dir.equals(Dir.NORTH)) {
				for (int i = 0; i < 6; i++) {
					if (game.getRoom(floorNo).getTileMap().getTileMap()[this.getCharacterLocation().row()][this
							.getCharacterLocation().column() - 1] instanceof EmptyTile
							&& game.getRoom(floorNo).getTileMap().getTileMap()[this.getCharacterLocation().row()][this
									.getCharacterLocation().column() - 1].occupied()
							&& ((EmptyTile) game.getRoom(floorNo).getTileMap().getTileMap()[this.getCharacterLocation()
									.row()][this.getCharacterLocation().column() - 1])
											.getObjectonTile() instanceof Player) {
						System.out.println("we have found an intruder");
						return true;

					}
				}

			} else if (dir.equals(Dir.SOUTH)) {
				for (int i = 0; i < 6; i++) {
					if (game.getRoom(floorNo).getTileMap().getTileMap()[this.getCharacterLocation().row()][this
							.getCharacterLocation().column() + i] instanceof EmptyTile
							&& game.getRoom(floorNo).getTileMap().getTileMap()[this.getCharacterLocation().row()][this
									.getCharacterLocation().column() + 1].occupied()
							&& ((EmptyTile) game.getRoom(floorNo).getTileMap().getTileMap()[this.getCharacterLocation()
									.row()][this.getCharacterLocation().column() + i])
											.getObjectonTile() instanceof Player) {
						System.out.println("we have found an intruder");
						return true;

					}
				}

			}

		} catch (Exception e) {
			// Do nothing For Guard Quadratic Vision.

		}
		return false;
	}
	*/

	private boolean moveIsValid(Location p, GameObject c) {

		return false;

		// !board.squareAt(newPosition).isOccupied()
	}

	

	/**
	 * this inner class returns an arraylist of directions based on strategy
	 * chosen
	 *
	 *
	 * @author abubakhami
	 *
	 */
	private class GuardStrategy {
		private int moveStratey;
		private ArrayList<Direction.Dir> directionList;

		GuardStrategy(int moveStrategy) {
			this.moveStratey = moveStrategy;
			directionList = new ArrayList<Direction.Dir>();
			if (moveStrategy == 1) {
				directionList.add(Dir.EAST);
				directionList.add(Dir.WEST);
			}
			if (moveStrategy == 2) {
				directionList.add(Dir.WEST);
				directionList.add(Dir.EAST);
			}
			if (moveStrategy == 3) {
				directionList.add(Dir.NORTH);
				directionList.add(Dir.SOUTH);
			}
			if (moveStrategy == 4) {
				directionList.add(Dir.SOUTH);
				directionList.add(Dir.NORTH);
			}
			if (moveStrategy == 5) {
				directionList.add(Dir.SOUTH);
				directionList.add(Dir.WEST);
			}
			if (moveStrategy == 6) {
				directionList.add(Dir.SOUTH);
				directionList.add(Dir.EAST);
			}
			if (moveStrategy == 7) {
				directionList.add(Dir.NORTH);
				directionList.add(Dir.WEST);
			}
			if (moveStrategy == 8) {
				directionList.add(Dir.NORTH);
				directionList.add(Dir.EAST);
			}
			if (moveStrategy == 9) {
				directionList.add(Dir.EAST);
				directionList.add(Dir.NORTH);
			}
			if (moveStrategy == 10) {
				directionList.add(Dir.WEST);
				directionList.add(Dir.NORTH);
			}
			if (moveStrategy == 11) {
				directionList.add(Dir.EAST);
				directionList.add(Dir.SOUTH);
			}
			if (moveStrategy == 12) {
				directionList.add(Dir.WEST);
				directionList.add(Dir.SOUTH);
			}
			if (moveStrategy == 13) {
				directionList.add(Dir.EAST);
				directionList.add(Dir.NORTH);
				directionList.add(Dir.WEST);
				directionList.add(Dir.SOUTH);
			}
		}

		public ArrayList<Direction.Dir> getDirectionList() {

			return directionList;
		}

	}


	 /**
     * Moves the guard the specified amount. 0 means no change will be made.
     *
     * @param dx
     * @param dy
     * @param dz
     */
    public void updatePosition(double dx, double dy, double dz) {
        this.x += dx;
        this.y += dy;
        this.z += dz;

        cubes.forEach(c -> c.updatePosition(dx, dy, dz));
    }


	@Override
	public void updateDirection(double toX, double toY) {
		cubes.forEach(i -> i.updateDirection(toX, toY));

	}

	@Override
	public void updatePoly() {
		cubes.forEach(i -> i.updatePoly());

	}

	@Override
	public void setRotAdd() {
		  cubes.forEach(i -> i.setRotAdd());

	}

	@Override
	public void removeCube() {
		cubes.forEach(i -> i.removeCube());

	}

	@Override
	public boolean containsPoint(int x, int y, int z) {
		return (this.x + this.width) > x && (this.y + this.length) > y && this.x < x && this.y < y;
	}

	@Override
	public List<Polygon> getPolygons() {
		java.util.List<Polygon> allPolys = new ArrayList<>();
		// Add all the cubes cubes
		cubes.forEach(c -> allPolys.addAll(c.getPolygons()));
		return allPolys;
	}

	public double getZ() {
		return z;
	}

	public double getY() {
		return y;
	}

	public double getX() {
		return x;
	}

}

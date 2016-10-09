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
import gui.Screen;
import items.Direction;
import items.Direction.Dir;
import items.Distance;
import items.GameObject;
import items.Item;
import items.Player;
import model.Game;

public class GuardBot extends Player implements Drawable {

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
	public Screen screen;
	private int i;
	private int u;
	private int itemID;
	private String itemType;
	private double guardSpeed;
	private double guardConst;

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
	public GuardBot(int itemID, String itemType, int moveStrategy, int[] distance, int floorNo, double x, double y,
			double z, double width, double length, double height, double guardSpeed, Color c) {
		super(x, y, z, width, length, height, c);
		this.itemID = itemID;
		this.itemType = itemType;
		this.moveStrategy = moveStrategy;
		strategy = new GuardStrategy(moveStrategy);
		directionList = strategy.getDirectionList();
		this.dir = directionList.get(0);
		this.floorNo = floorNo;
		this.distance = distance;
		cubes = new ArrayList<>();
		this.x = x;
		this.y = y;
		this.guardSpeed = guardSpeed;
		this.guardConst = 10/guardSpeed;

		this.z = 0;
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

	/*
	 *
	 * /** this method moves the guard along a path specified by the move
	 * strategy, the method should keep running until an intruder is detected
	 */

	public void move() {

		// set Guard's direction
		this.dir = directionList.get(i);

		// move the guard to new location based on strategy
		// Guard has n moves equal to distance specified in
		// strategy, however if a player is detected within the direction he is
		// facing,
		// the bot stops moving and alerts other player
		// player as guard of his location
		if (!this.checkforIntruder()) {
			if (u == (distance[i] - 1) * guardConst && i < (directionList.size() - 1)) {
				i++;
				u = 0;
			//	System.out.println("value of i is: " + i);
			//	System.out.println("value of u is: " + u);
				this.updateDirection(x, y);


			}
			// we reverse the movement directions (if
			// required
			// e.g. north south path does not require reversal
			// but a north west path would need to go back to origin
			// through
			// east south) when the guard reaches last square in path
			// and
			// run the
			// move method again
			else if (u == (distance[i] - 1) * guardConst && i == (directionList.size() - 1)) {
				this.reverseStrategy();
				//System.out.println(" i should reverse" + this.getName());
				i = 0;
				u = 0;

			}

			if (this.dir.equals(Dir.EAST)) {
                // System.out.println(this.getName() + " should head east now");
				updatePosition(guardSpeed, 0, 0);
				this.updateDirection();
				u++;

			} else if (this.dir.equals(Dir.WEST)) {
				updatePosition(-guardSpeed, 0, 0);
				this.updateDirection();
				u++;
				;

			} else if (this.dir.equals(Dir.NORTH)) {
				this.updateDirection();
				updatePosition(0, -guardSpeed, 0);

				u++;

			} else if (this.dir.equals(Dir.SOUTH)) {
				this.updateDirection();
				updatePosition(0, guardSpeed, 0);
				u++;

			}

			;
		}
		else {

			// intruder detected, so we set timer for map to display on guards screen
			this.screen.timer = 200;
			/*if (guardSpeed < 1)
			guardSpeed += 0.01;*/
		}

	}
/**
 * get the floor number
 * @return
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
		if (this.moveStrategy == 17) {
			this.moveStrategy = 18;
			strategy = new GuardStrategy(18);
			this.revertDistance();
			directionList = strategy.getDirectionList();
			this.dir = directionList.get(0);
			System.out.println(directionList.get(0));
			}

	}

	/**
	 * this method checks whether there are any intruders within a certain range
	 * of view of the bot. the guard bot can only detect a player in front of
	 * him or in his side views, but he can not detect a player behind him,
	 * hence a player can sneak through safely if he moves behind the guard.

	 * @return
	 */
	public Boolean checkforIntruder() {
		try {
		 // intrusion only works if current player is a guard and if the other player is not in room
			if (!this.screen.isGuard() && !this.screen.getCurrentPlayer().isInRoom()) {
			if (dir.equals(Dir.EAST)) {

				int guardlocation = (int) Math.round(this.x);
				int playerlocation = (int) Math.round(this.screen.getPlayerView()[0]);
				int yOffset = (int) Math.round(this.screen.getPlayerView()[1]) - (int) Math.round(this.y);


				if ((playerlocation - guardlocation) > 0
						&& (playerlocation - guardlocation) < 100 & Math.abs(yOffset) < 100) {

					return true;

				}

			} else if (dir.equals(Dir.WEST)) {

				int guardlocation = (int) Math.round(this.x);
				int playerlocation = (int) Math.round(this.screen.getPlayerView()[0]);
				int yOffset = (int) Math.round(this.screen.getPlayerView()[1]) - (int) Math.round(this.y);
				if ((guardlocation - playerlocation) > 0
						&& (guardlocation - playerlocation) < 100 & Math.abs(yOffset) < 100) {

					return true;

				}

			} else if (dir.equals(Dir.NORTH)) {

				int guardlocation = (int) Math.round(this.y);
				int playerlocation = (int) Math.round(this.screen.getPlayerView()[1]);
				int xOffset = (int) Math.round(this.screen.getPlayerView()[0]) - (int) Math.round(this.x);

				if ((guardlocation - playerlocation) > 0
						&& (guardlocation - playerlocation) < 100 & Math.abs(xOffset) < 100) {


					return true;

				}

			} else if (dir.equals(Dir.SOUTH)) {

				int guardlocation = (int) Math.round(this.y);
				int playerlocation = (int) Math.round(this.screen.getPlayerView()[1]);
				int xOffset = (int) Math.round(this.screen.getPlayerView()[0]) - (int) Math.round(this.x);
				if ((playerlocation - guardlocation) > 0 && (playerlocation - guardlocation) < 100
						&& Math.abs(xOffset) < 100) {
					return true;

				}

			}
			}

		} catch (Exception e) { // Do nothing For Guard Quadratic Vision.

		}
		return false;
	}

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
			if (moveStrategy == 14) {
				directionList.add(Dir.SOUTH);
				directionList.add(Dir.WEST);
				directionList.add(Dir.NORTH);
				directionList.add(Dir.EAST);
			}
				
			if (moveStrategy == 15) {
				directionList.add(Dir.WEST);
				directionList.add(Dir.NORTH);
				directionList.add(Dir.EAST);
				directionList.add(Dir.SOUTH);
				}
			
			if (moveStrategy == 16) {
				directionList.add(Dir.SOUTH);
				directionList.add(Dir.EAST);
				directionList.add(Dir.NORTH);
				directionList.add(Dir.WEST);
				}
			
			if (moveStrategy == 17) {
				directionList.add(Dir.WEST);
				directionList.add(Dir.SOUTH);
				directionList.add(Dir.EAST);
				}
			
			if (moveStrategy == 18) {
				directionList.add(Dir.EAST);
				directionList.add(Dir.NORTH);
				directionList.add(Dir.WEST);
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

/**
 * changes the direction the bot is facing
 */
	public void updateDirection() {
		if (this.dir.equals(Direction.Dir.EAST) || this.dir.equals(Direction.Dir.WEST)) {
			cubes.clear();
			// First make the legs
			int legWidth = (int) (width / 2.5);
			int legHeight = (int) (height / 2);
			// first leg
			cubes.add(new Cube(x + (legWidth / 2), y, z, legWidth, legWidth, legHeight, color));
			// second leg
			cubes.add(new Cube(x + (legWidth / 2), y + width - legWidth, z, legWidth, legWidth, legHeight, color));

			// body
			cubes.add(new Cube(x, y, z + legHeight, width, width, legHeight, color));

			// arms
			cubes.add(new Cube(x, y + length, z + legHeight + (legHeight / 2), width, width / 1, legHeight / 3, color));
			cubes.add(new Cube(x, y - length, z + legHeight + (legHeight / 2), width, width / 1, legHeight / 3, color));

			// head
			cubes.add(new Cube(x, y + (length / 4), z + (legHeight * 2), width / 2, width / 1.5, legHeight / 3, color));
		} else {
			cubes.clear();
			// First make the legs
			int legWidth = (int) (width / 2.5);
			int legHeight = (int) (height / 2);
			// first leg
			cubes.add(new Cube(x, y + (legWidth / 2), z, legWidth, legWidth, legHeight, color));
			// second leg
			cubes.add(new Cube(x + width - legWidth, y + (legWidth / 2), z, legWidth, legWidth, legHeight, color));

			// body
			cubes.add(new Cube(x, y, z + legHeight, width, width / 1.5, legHeight, color));

			// arms
			cubes.add(
					new Cube(x + width, y, z + legHeight + (legHeight / 2), width, width / 1.5, legHeight / 3, color));
			cubes.add(
					new Cube(x - width, y, z + legHeight + (legHeight / 2), width, width / 1.5, legHeight / 3, color));

			// head
			cubes.add(new Cube(x + (width / 4), y, z + (legHeight * 2), width / 2, width / 1.5, legHeight / 3, color));
		}
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

	public Thread createGuardThread(GuardBot gaurd, int delay) {
		Thread guardThread = new Thread() {
			public void run() {
				// move the guard in a fixed loop, once he reaches certain
				// coordinate on the Map, change destination
				// if () {}
				// gaurd will keep moving

				// update direction of guard based on hardcoded route
				// through Tilemap

				try {
					Thread.sleep(delay);
					GuardBot.this.move();

				} catch (InterruptedException e) {
					// should never happen
				}

				// draw board intp console for debugging purposes
				// game.drawBoard(gaurd.getFloorNo());

			}
		};
		return guardThread;

	}

	public String getName() {

		return this.itemType;
	}

	public void setScreen(Screen screen) {
		this.screen = screen;

	}

}

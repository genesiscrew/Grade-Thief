package model.characters;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
/**
 * created by hamid abubakr
 * creates a guard object that moves along  game floor map in certain path continuosly untill it detects an intruder
 */
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.swing.plaf.SliderUI;

import model.floor.EmptyTile;
import model.floor.Location;
import model.items.Direction;
import model.items.Distance;
import model.items.GameObject;
import model.items.Item;
import model.items.Direction.Dir;
import model.rendering.Cube;
import model.rendering.Drawable;
import model.rendering.Polygon;
import view.Screen;

/**
 *
 * @author Hamid Abubakr
 *
 *
 *         Guard bot class creates a guard bot object that moves within each
 *         level. the bots are able to detect a player, and are able to move
 *         along predefined coordinates in the map. the guard bot route is created
 *         using predefined movement strategies. depending on the strategy chosen certain
 *         parts of the code are used and certain routes are taken. the bots
 *         also can have different speeds at which they move. the bots are
 *         unable to detect players inside rooms, they only detect players in
 *         corridors. once a guard bot finds a player, the guard player is
 *         informed of the players level, in addition a map is temporarily
 *         displayed on the other player's screen indicating the location of the
 *         player. once the player hides however, the map will then disappear
 *         after a set period of time.
 *
 */

public class GuardBot extends Player implements Drawable {

	Direction.Dir dir; // direction the guard will move
	ArrayList<Direction.Dir> directionList; // list of directions the guard will take
	int moveStrategy; // the identifier of the strategy to be used
	GuardStrategy strategy; // strategy the guard will use
	Location characterLocation; // location of character
	private int[] distance; // array of distances to take for each direction
	private int floorNo; // floor number of the guard
	private java.util.List<Cube> cubes; // list of cubes for the guard bot
	protected double x;
	protected double y;
	protected double z;
	protected double width;
	protected double length;
	protected double height;
	protected Color color; // color of the bot
	public Screen screen; // screen the guard bot will apear in
	private int i; // counter for the direction
	private double u; // counter for steps
	private int itemID;
	private String itemType;
	private double guardVelocity; // speed variable
	private String level;
	private int DetectionStrength; // strength of detection (distance)
	private double guardSpeedMultiplier; //modifier of coordinate steps to account for screen pixels

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
	public GuardBot(int itemID, String itemType, String level, int moveStrategy, int[] distance, int floorNo, double x,
			double y, double z, double width, double length, double height, double guardVelocity, Color c) {
		super(x, y, z, width, length, height, c);
		this.itemID = itemID;
		this.itemType = itemType;
		this.level = level;
		this.moveStrategy = moveStrategy;
		strategy = new GuardStrategy(moveStrategy);
		directionList = strategy.getDirectionList();
		this.dir = directionList.get(0);
		this.floorNo = floorNo;
		this.distance = distance;
		cubes = new ArrayList<>();
		this.x = x;
		this.y = y;
		this.u = 0;
		this.i = 0;
		this.guardVelocity = guardVelocity;
		this.guardSpeedMultiplier = guardVelocity * 10;
		this.DetectionStrength = 50;
		this.z = 0;
		this.width = width;
		this.length = length;
		this.height = height;
		this.color = c;
		this.height *= 0.8;

        // First make the legs
        int legAndArmWidth = (int) (width * 0.25); // 25 %
        int halfWidth = (int) (width * 0.5); // 25 %
        int bodyLength = (int)(length * 0.7);
        int legHeight = (int) (height * 0.5); // 50 %
        int armHeight = (int) (height * 0.40); // 40 %
        int torsoAboveArmHeight = (int) (height * 0.15); // 40 %
        int headHeight = (int) (height * 0.22); // 17 %

        // first leg
        cubes.add(new Cube(x, y + (legAndArmWidth / 2), z, legAndArmWidth, bodyLength, legHeight, PANTS_COLOR));

        // second leg
        cubes.add(new Cube(x + legAndArmWidth, y + (legAndArmWidth / 2), z, legAndArmWidth, bodyLength, legHeight, PANTS_COLOR));

        // body
        cubes.add(new Cube(x, y, z + legHeight, halfWidth, bodyLength, legHeight, TOP_COLOR));

        // arms
        cubes.add(new Cube(x + halfWidth, y, z + legHeight , legAndArmWidth, bodyLength, armHeight, SKIN_COLOR));
        cubes.add(new Cube(x - legAndArmWidth, y, z + legHeight , legAndArmWidth, bodyLength, armHeight, SKIN_COLOR));

        // above arm torso
        cubes.add(new Cube(x + halfWidth, y, z + legHeight+armHeight , legAndArmWidth, bodyLength, torsoAboveArmHeight, TOP_COLOR));
        cubes.add(new Cube(x - legAndArmWidth, y, z + legHeight +armHeight, legAndArmWidth, bodyLength, torsoAboveArmHeight, TOP_COLOR));

        // head
        cubes.add(new Cube(x, y, z + (legHeight * 2), bodyLength, bodyLength, headHeight, SKIN_COLOR));

        // above head
        cubes.add(new Cube(x, y, z + (legHeight * 2) + headHeight, bodyLength, bodyLength, headHeight*0.3, DARK_BROWN));

	}

	/*
	 *
	 * /** this method returns the level in which the guard is located
	 */
	public String getLevel() {

		return this.level;
	}

	/**
	 * this method moves the guard to a new co-ordinate on the game world
	 */
	public void move() {

		// set Guard's direction
		this.dir = directionList.get(i);

		// move the guard to new location based on strategy
		// Guard has n moves equal to distance specified in
		// strategy, however if a player is detected within the direction he is
		// facing,
		// the bot stops moving and alerts other player
		//

		if (!this.checkforIntruder()) {

			if ((u == Math.round((distance[i] - 1) * ((2 / guardVelocity)) / 2) && i == (directionList.size() - 1))) {
				// we reverse the movement directions (if
				// required
				// e.g. north south path does not require reversal
				// but a north west path would need to go back to origin
				// through
				// east south) when the guard reaches last square in path
				// and
				// run the
				// move method again

				this.reverseStrategy();

				i = 0;
				u = 0;
				return;
			} else if ((u == Math.round((distance[i] - 1) * ((2 / guardVelocity)) / 2)
					&& i < (directionList.size() - 1))) {
				// the bit has reached the final coordinate to a given
				// direction, we update the counters
				// and move the guard to a new direction

				i++;
				u = 0;

				this.updateDirection(); // this method relocates the guard bot
										// polygons towards direction he is
										// heading
				return;
			}
			// the code below moves the guard by a certain amount based on
			// direction
			if (this.dir.equals(Dir.EAST)) {

				updatePosition(guardSpeedMultiplier, 0, 0);
				this.updateDirection();
				u = u + 1;

			} else if (this.dir.equals(Dir.WEST)) {
				updatePosition(-guardSpeedMultiplier, 0, 0);
				this.updateDirection();
				u = u + 1;

			} else if (this.dir.equals(Dir.NORTH)) {
				updatePosition(0, -guardSpeedMultiplier, 0);
				this.updateDirection();
				u = u + 1;

			} else if (this.dir.equals(Dir.SOUTH)) {
				updatePosition(0, guardSpeedMultiplier, 0);
				this.updateDirection();
				u = u + 1;

			}

			;
		} else {

			// an intruder detected, so we set timer for map to display on
			// guards
			// screen, this timer is sent through network to other player
			this.screen.timer = 200;

		}

	}

	/**
	 * get the floor number
	 *
	 * @return
	 */
	public int getFloorNo() {

		return this.floorNo;
	}

	/**
	 * reverts the distance array
	 */
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
		} else if (this.moveStrategy == 10) {
			// reverse movement
			this.moveStrategy = 6;
			strategy = new GuardStrategy(6);
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
		} else if (this.moveStrategy == 17) {
			this.moveStrategy = 18;
			strategy = new GuardStrategy(18);
			this.revertDistance();
			directionList = strategy.getDirectionList();
			this.dir = directionList.get(0);
			// System.out.println(directionList.get(0));
		} else if (this.moveStrategy == 18) {

			this.moveStrategy = 17;
			strategy = new GuardStrategy(17);
			this.revertDistance();
			directionList = strategy.getDirectionList();
			this.dir = directionList.get(0);
			// System.out.println(directionList.get(0));
		}

	}

	/**
	 * this method checks whether there are any intruders within a certain range
	 * of view of the bot. the guard bot can only detect a player in front of
	 * him or in his side views, but he can not detect a player behind him,
	 * hence a player can sneak through safely if he moves behind the guard.
	 *
	 * @return
	 */
	public Boolean checkforIntruder() {
		try {
			// intrusion only works if current player is a guard and if the
			// other player is not in room
			if (!this.screen.isGuard() && !this.screen.getCurrentPlayer().isInRoom()
					&& this.screen.getCurrentPlayer().getLevelName().equals(this.level)) {
				if (dir.equals(Dir.EAST)) {

					int guardlocation = (int) Math.round(this.x);
					int playerlocation = (int) Math.round(this.screen.getPlayerView()[0]);
					int yOffset = (int) Math.round(this.screen.getPlayerView()[1]) - (int) Math.round(this.y);

					if ((playerlocation - guardlocation) > 0 && (playerlocation - guardlocation) < DetectionStrength
							& Math.abs(yOffset) < DetectionStrength) {

						return true;

					}

				} else if (dir.equals(Dir.WEST)) {

					int guardlocation = (int) Math.round(this.x);
					int playerlocation = (int) Math.round(this.screen.getPlayerView()[0]);
					int yOffset = (int) Math.round(this.screen.getPlayerView()[1]) - (int) Math.round(this.y);
					if ((guardlocation - playerlocation) > 0 && (guardlocation - playerlocation) < DetectionStrength
							& Math.abs(yOffset) < DetectionStrength) {

						return true;

					}

				} else if (dir.equals(Dir.NORTH)) {

					int guardlocation = (int) Math.round(this.y);
					int playerlocation = (int) Math.round(this.screen.getPlayerView()[1]);
					int xOffset = (int) Math.round(this.screen.getPlayerView()[0]) - (int) Math.round(this.x);

					if ((guardlocation - playerlocation) > 0 && (guardlocation - playerlocation) < DetectionStrength
							& Math.abs(xOffset) < DetectionStrength) {

						return true;

					}

				} else if (dir.equals(Dir.SOUTH)) {

					int guardlocation = (int) Math.round(this.y);
					int playerlocation = (int) Math.round(this.screen.getPlayerView()[1]);
					int xOffset = (int) Math.round(this.screen.getPlayerView()[0]) - (int) Math.round(this.x);
					if ((playerlocation - guardlocation) > 0 && (playerlocation - guardlocation) < DetectionStrength
							&& Math.abs(xOffset) < DetectionStrength) {
						return true;

					}

				}
			}

		} catch (Exception e) { // Do nothing For Guard Quadratic Vision.

		}
		return false;
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
				directionList.add(Dir.WEST);
				directionList.add(Dir.NORTH);
				directionList.add(Dir.EAST);
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
	 * this method changes the direction the bot is facing
	 */
	public void updateDirection() {
		if (this.dir.equals(Direction.Dir.EAST) || this.dir.equals(Direction.Dir.WEST)) {
			  cubes.clear();
	            // First make the legs
	            int legAndArmWidth = (int) (width * 0.25); // 25 %
	            int halfWidth = (int) (width * 0.5); // 25 %
	            int bodyLength = (int)(length * 0.7);
	            int legHeight = (int) (height * 0.5); // 50 %
	            int armHeight = (int) (height * 0.40); // 40 %
	            int torsoAboveArmHeight = (int) (height * 0.15); // 40 %
	            int headHeight = (int) (height * 0.22); // 17 %

	            // first leg
	            cubes.add(new Cube(x , y + (legAndArmWidth / 2) , z, bodyLength, legAndArmWidth, legHeight, PANTS_COLOR));

	            // second leg
	            cubes.add(new Cube(x + (legAndArmWidth / 2), y + legAndArmWidth, z, bodyLength, legAndArmWidth, legHeight, PANTS_COLOR));

	            // body
	            cubes.add(new Cube(x, y, z + legHeight, bodyLength, halfWidth, legHeight, TOP_COLOR));

	            // arms
	            cubes.add(new Cube(x, y + halfWidth, z + legHeight , bodyLength, legAndArmWidth, armHeight, SKIN_COLOR));
	            cubes.add(new Cube(x , y- legAndArmWidth, z + legHeight , bodyLength, legAndArmWidth, armHeight, SKIN_COLOR));

	            // above arm torso
	            cubes.add(new Cube(x , y+ halfWidth, z + legHeight+armHeight , bodyLength, legAndArmWidth, torsoAboveArmHeight, TOP_COLOR));
	            cubes.add(new Cube(x , y- legAndArmWidth, z + legHeight +armHeight, bodyLength, legAndArmWidth, torsoAboveArmHeight, TOP_COLOR));

	            // head
	            cubes.add(new Cube(x, y, z + (legHeight * 2), bodyLength, bodyLength, headHeight, SKIN_COLOR));

	            // above head
	            cubes.add(new Cube(x, y, z + (legHeight * 2) + headHeight, bodyLength, bodyLength, headHeight*0.3, DARK_BROWN));

	            // Eyes

	            // Eyes
		} else {
			cubes.clear();
            // First make the legs
            int legAndArmWidth = (int) (width * 0.25); // 25 %
            int halfWidth = (int) (width * 0.5); // 25 %
            int bodyLength = (int)(length * 0.7);
            int legHeight = (int) (height * 0.5); // 50 %
            int armHeight = (int) (height * 0.40); // 40 %
            int torsoAboveArmHeight = (int) (height * 0.15); // 40 %
            int headHeight = (int) (height * 0.22); // 17 %

            // first leg
            cubes.add(new Cube(x + (legAndArmWidth / 2), y , z, legAndArmWidth, bodyLength, legHeight, PANTS_COLOR));

            // second leg
            cubes.add(new Cube(x + legAndArmWidth, y + (legAndArmWidth / 2), z, legAndArmWidth, bodyLength, legHeight, PANTS_COLOR));

            // body
            cubes.add(new Cube(x, y, z + legHeight, halfWidth, bodyLength, legHeight, TOP_COLOR));

            // arms
            cubes.add(new Cube(x + halfWidth, y, z + legHeight , legAndArmWidth, bodyLength, armHeight, SKIN_COLOR));
            cubes.add(new Cube(x - legAndArmWidth, y, z + legHeight , legAndArmWidth, bodyLength, armHeight, SKIN_COLOR));

            // above arm torso
            cubes.add(new Cube(x + halfWidth, y, z + legHeight+armHeight , legAndArmWidth, bodyLength, torsoAboveArmHeight, TOP_COLOR));
            cubes.add(new Cube(x - legAndArmWidth, y, z + legHeight +armHeight, legAndArmWidth, bodyLength, torsoAboveArmHeight, TOP_COLOR));

            // head
            cubes.add(new Cube(x, y, z + (legHeight * 2), bodyLength, bodyLength, headHeight, SKIN_COLOR));

            // above head
            cubes.add(new Cube(x, y, z + (legHeight * 2) + headHeight, bodyLength, bodyLength, headHeight*0.3, DARK_BROWN));
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

	public String getName() {

		return this.itemType;
	}

	/**
	 * @param screen
	 */
	public void setScreen(Screen screen) {
		this.screen = screen;

	}

}

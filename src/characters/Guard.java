package characters;

import java.util.ArrayList;
import java.util.Arrays;
/**
 * created by hamid abubakr
 * creates a guard object that moves along  game floor map in certain path continuosly untill it detects an intruder
 */
import java.util.Collections;

import javax.swing.plaf.SliderUI;

import game.floor.EmptyTile;
import game.floor.Location;
import items.Direction;
import items.Direction.Dir;
import items.Distance;
import items.GameObject;
import model.Game;

public class Guard extends Character {

	Direction.Dir dir;
	ArrayList<Direction.Dir> directionList;
	int moveStrategy;
	GuardStrategy strategy;

	Location characterLocation;
	private int[] distance;
	private int floorNo;

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
	public Guard(int characterID, String characterName, int moveStrategy, int[] distance, int floorNo) {
		super(characterID, characterName);
		this.moveStrategy = moveStrategy;
		strategy = new GuardStrategy(moveStrategy);
		directionList = strategy.getDirectionList();
		this.dir = directionList.get(0);
		this.floorNo = floorNo;
		this.distance = distance;
	}

	public Location getCharacterLocation() {
		return super.getCharacterLocation();
	}

	public void setCharacterLocation(int x, int y) {
		super.setCharacterLocation(x, y);
	}

	/**
	 * this method moves the guard along a path specified by the move strategy,
	 * the method should keep running until an intruder is detected
	 */
	public void move(Game game) {

		while (!this.checkforIntruder(game)) {
			for (int i = 0; i < directionList.size(); i++) {

				// set Guard's direction
				this.dir = directionList.get(i);

				// now we move guard n steps
				for (int x = 0; x < distance[i]; x++) {


					// move the guard to new location
					// remove gaurd as object from previous empty tile
					((EmptyTile) game.getFloor(floorNo).getFloorMap().getFloorTiles()[this.getCharacterLocation()
							.row()][this.getCharacterLocation().column()]).resetEmptyTile();
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
					((EmptyTile) game.getFloor(floorNo).getFloorMap().getFloorTiles()[this.getCharacterLocation()
							.row()][this.getCharacterLocation().column()]).addObjectToTile(this);
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

	public Boolean checkforIntruder(Game game) {
		try {
			if (dir.equals(Dir.EAST)) {
				for (int i = 0; i < 6; i++) {
					if (game.getFloor(floorNo).getFloorMap().getFloorTiles()[this.getCharacterLocation().row() + i][this
							.getCharacterLocation().column()] instanceof EmptyTile
							&& game.getFloor(floorNo).getFloorMap().getFloorTiles()[this.getCharacterLocation().row()
									+ i][this.getCharacterLocation().column()].occupied()
							&& ((EmptyTile) game.getFloor(floorNo).getFloorMap()
									.getFloorTiles()[this.getCharacterLocation().row() + i][this.getCharacterLocation()
											.column()]).getObjectonTile() instanceof Player) {
						System.out.println("we have found an intruder");
						return true;

					}
				}

			} else if (dir.equals(Dir.WEST)) {
				for (int i = 0; i < 6; i++) {

					if (game.getFloor(floorNo).getFloorMap().getFloorTiles()[this.getCharacterLocation().row() - i][this
							.getCharacterLocation().column()] instanceof EmptyTile
							&& game.getFloor(floorNo).getFloorMap().getFloorTiles()[this.getCharacterLocation().row()
									- i][this.getCharacterLocation().column()].occupied()
							&& ((EmptyTile) game.getFloor(floorNo).getFloorMap()
									.getFloorTiles()[this.getCharacterLocation().row() - i][this.getCharacterLocation()
											.column()]).getObjectonTile() instanceof Player) {
						System.out.println("we have found an intruder");
						return true;

					}
				}

			} else if (dir.equals(Dir.NORTH)) {
				for (int i = 0; i < 6; i++) {
					if (game.getFloor(floorNo).getFloorMap().getFloorTiles()[this.getCharacterLocation().row()][this
							.getCharacterLocation().column() - 1] instanceof EmptyTile
							&& game.getFloor(floorNo).getFloorMap().getFloorTiles()[this.getCharacterLocation()
									.row()][this.getCharacterLocation().column() - 1].occupied()
							&& ((EmptyTile) game.getFloor(floorNo).getFloorMap().getFloorTiles()[this
									.getCharacterLocation().row()][this.getCharacterLocation().column() - 1])
											.getObjectonTile() instanceof Player) {
						System.out.println("we have found an intruder");
						return true;

					}
				}

			} else if (dir.equals(Dir.SOUTH)) {
				for (int i = 0; i < 6; i++) {
					if (game.getFloor(floorNo).getFloorMap().getFloorTiles()[this.getCharacterLocation().row()][this
							.getCharacterLocation().column() + i] instanceof EmptyTile
							&& game.getFloor(floorNo).getFloorMap().getFloorTiles()[this.getCharacterLocation()
									.row()][this.getCharacterLocation().column() + 1].occupied()
							&& ((EmptyTile) game.getFloor(floorNo).getFloorMap().getFloorTiles()[this
									.getCharacterLocation().row()][this.getCharacterLocation().column() + i])
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

	private boolean moveIsValid(Location p, GameObject c) {

		return false;

		// !board.squareAt(newPosition).isOccupied()
	}

	@Override
	public void characterInteraction(Character c) {
		// TODO Auto-generated method stub

	}

	@Override
	public void objectInteraction(GameObject c) {
		// TODO Auto-generated method stub

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

}

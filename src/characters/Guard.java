package characters;

import java.util.ArrayList;
import java.util.Collections;

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
	private int distance;

	public Guard(int characterID, String characterName, int moveStrategy, int distance) {
		super(characterID, characterName);
		this.distance = distance;
		this.moveStrategy = moveStrategy;
		strategy = new GuardStrategy(moveStrategy);
		directionList = strategy.getDirectionList();

	}

	public Location getCharacterLocation() {
		return super.getCharacterLocation();
	}

	public void setCharacterLocation(int x, int y) {
		super.setCharacterLocation(x, y);
	}

	/**
	 * this method moves the guard along a path specified by the move strategy
	 */
	public void move() {

		for (int i = 0; i < directionList.size(); i++) {

			// set Guard's direction
			this.dir = directionList.get(i);
			// now we move guard n steps
			for (int x = 0; x < distance; x++) {
				// move the guard one step based on direction
				if (this.dir.equals(Dir.EAST)) {
					this.setCharacterLocation(this.getCharacterLocation().row() + 1,
							this.getCharacterLocation().column());

				} else if (this.dir.equals(Dir.WEST)) {

					this.setCharacterLocation(this.getCharacterLocation().row() - 1,
							this.getCharacterLocation().column());

				} else if (this.dir.equals(Dir.NORTH)) {

					this.setCharacterLocation(this.getCharacterLocation().row(),
							this.getCharacterLocation().column() + 1);

				} else if (this.dir.equals(Dir.SOUTH)) {

					this.setCharacterLocation(this.getCharacterLocation().row(),
							this.getCharacterLocation().column() - 1);

				}
				// Guard has move n moves equal to distance specified in
				// strategy, now we reverse the movement directions and run the
				// move method again
				if (x == (distance - 1)) {

					this.reverseStrategy();
					this.move();
				}
			}

		}

	}

	/**
	 * reverse the directions of move strategy
	 */
	private void reverseStrategy() {

		if (this.moveStrategy == 5) {
			// reverse movement
			strategy = new GuardStrategy(9);
			directionList = strategy.getDirectionList();
		}
		if (this.moveStrategy == 6) {
			// reverse movement
			strategy = new GuardStrategy(10);
			directionList = strategy.getDirectionList();
		}
		if (this.moveStrategy == 7) {
			// reverse movement
			strategy = new GuardStrategy(10);
			directionList = strategy.getDirectionList();
		}

	}

	public Boolean checkforIntruder(Game game, Direction dir) {
		if (dir.getDirection().equals(Dir.EAST)) {
			for (int i = 0; i < 6; i++) {
				if (game.getGameMap().getTileMap()[this.getCharacterLocation().row() + i][this.getCharacterLocation()
						.column()] instanceof EmptyTile
						&& game.getGameMap().getTileMap()[this.getCharacterLocation().row() + i][this
								.getCharacterLocation().column()].occupied()
						&& ((EmptyTile) game.getGameMap().getTileMap()[this.getCharacterLocation().row() + i][this
								.getCharacterLocation().column()]).getObjectonTile() instanceof Player) {
					System.out.println("we have found an intruder");
					return true;

				}
			}

		} else if (dir.getDirection().equals(Dir.WEST)) {
			for (int i = 0; i < 6; i++) {

				if (game.getGameMap().getTileMap()[this.getCharacterLocation().row() - i][this.getCharacterLocation()
						.column()] instanceof EmptyTile
						&& game.getGameMap().getTileMap()[this.getCharacterLocation().row() - i][this
								.getCharacterLocation().column()].occupied()
						&& ((EmptyTile) game.getGameMap().getTileMap()[this.getCharacterLocation().row() - i][this
								.getCharacterLocation().column()]).getObjectonTile() instanceof Player) {
					System.out.println("we have found an intruder");
					return true;

				}
			}

		} else if (dir.getDirection().equals(Dir.NORTH)) {
			for (int i = 0; i < 6; i++) {
				if (game.getGameMap().getTileMap()[this.getCharacterLocation().row()][this.getCharacterLocation()
						.column() + 1] instanceof EmptyTile
						&& game.getGameMap().getTileMap()[this.getCharacterLocation().row()][this.getCharacterLocation()
								.column() + 1].occupied()
						&& ((EmptyTile) game.getGameMap().getTileMap()[this.getCharacterLocation().row()][this
								.getCharacterLocation().column() + 1]).getObjectonTile() instanceof Player) {
					System.out.println("we have found an intruder");
					return true;

				}
			}

		} else if (dir.getDirection().equals(Dir.SOUTH)) {
			for (int i = 0; i < 6; i++) {
				if (game.getGameMap().getTileMap()[this.getCharacterLocation().row()][this.getCharacterLocation()
						.column() - i] instanceof EmptyTile
						&& game.getGameMap().getTileMap()[this.getCharacterLocation().row()][this.getCharacterLocation()
								.column() - 1].occupied()
						&& ((EmptyTile) game.getGameMap().getTileMap()[this.getCharacterLocation().row()][this
								.getCharacterLocation().column() - i]).getObjectonTile() instanceof Player) {
					System.out.println("we have found an intruder");
					return true;

				}
			}

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

	private class GuardStrategy {
		private int moveStratey;
		private ArrayList<Direction.Dir> directionList;

		GuardStrategy(int moveStrategy) {
			this.moveStratey = moveStrategy;
			ArrayList<Direction.Dir> directionList = new ArrayList<Direction.Dir>();
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
		}
		public ArrayList<Direction.Dir> getDirectionList() {

			return this.directionList;
		}

	}

}

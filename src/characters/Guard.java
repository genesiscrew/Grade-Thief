package characters;

import game.floor.EmptyTile;
import game.floor.Location;
import items.Direction;
import items.Direction.Dir;
import items.Distance;
import items.GameObject;
import model.Game;

public class Guard extends Character {

	public int characterID;
	public String characterName;
	Direction dir;


	Location characterLocation;


	public Guard(int characterID, String characterName) {
			super(characterID, characterName);
			this.characterName = characterName;
			this.characterID = characterID;
	}

	public Location getCharacterLocation() {
		return super.getCharacterLocation();
	}

	public void setCharacterLocation(int x, int y) {
		super.setCharacterLocation(x,y);
	}

	@Override
	/**
	 * this method moves the guard along and hardcoded path
	 */
	public void move(Direction dir, Distance d) {
		this.dir = dir;
        // moves the guard one tile forward based on its direction
			if (dir.getDirection().equals(Dir.EAST)) {
				this.setCharacterLocation(this.getCharacterLocation().row()+1,this.getCharacterLocation().column());

			} else if (dir.getDirection().equals(Dir.WEST)) {

				this.setCharacterLocation(this.getCharacterLocation().row()-1,this.getCharacterLocation().column());

			} else if (dir.getDirection().equals(Dir.NORTH)) {

				this.setCharacterLocation(this.getCharacterLocation().row(),this.getCharacterLocation().column()+1);

			} else if (dir.getDirection().equals(Dir.SOUTH)) {

				this.setCharacterLocation(this.getCharacterLocation().row(),this.getCharacterLocation().column()-1);

			}



	}


	public Boolean checkforIntruder(Game game) {
		if (dir.equals(Dir.EAST)) {
			for (int i = 0; i < 6; i++) {
				if (game.getGameMap().getTileMap()[this.getCharacterLocation().row() + i][this.getCharacterLocation().column()] instanceof EmptyTile
						&& game.getGameMap().getTileMap()[this.getCharacterLocation().row() + i][this.getCharacterLocation().column()].occupied()) {
					// we have found an intruder
					return true;

				}
			}

		} else if (dir.equals(Dir.WEST)) {
			for (int i = 0; i < 6; i++) {
				if (game.getGameMap().getTileMap()[this.getCharacterLocation().row() - i][this.getCharacterLocation().column()] instanceof EmptyTile
						&& game.getGameMap().getTileMap()[this.getCharacterLocation().row() + i][this.getCharacterLocation().column()].occupied()) {
					// we have found an intruder
					return true;

				}
			}

		} else if (dir.equals(Dir.NORTH)) {
			for (int i = 0; i < 6; i++) {
				if (game.getGameMap().getTileMap()[this.getCharacterLocation().row() + i][this.getCharacterLocation().column()] instanceof EmptyTile
						&& game.getGameMap().getTileMap()[this.getCharacterLocation().row()][this.getCharacterLocation().column()+1].occupied()) {
					// we have found an intruder
					return true;

				}
			}

		} else if (dir.equals(Dir.SOUTH)) {
			for (int i = 0; i < 6; i++) {
				if (game.getGameMap().getTileMap()[this.getCharacterLocation().row() + i][this.getCharacterLocation().column()] instanceof EmptyTile
						&& game.getGameMap().getTileMap()[this.getCharacterLocation().row()][this.getCharacterLocation().column()-1].occupied()) {
					// we have found an intruder
					return true;

				}
			}

		}
		return false;

	}

	private boolean moveIsValid(Location p, GameObject c) {


		return false;

		//!board.squareAt(newPosition).isOccupied()
	}

	@Override
	public void characterInteraction(Character c) {
		// TODO Auto-generated method stub

	}

	@Override
	public void objectInteraction(GameObject c) {
		// TODO Auto-generated method stub

	}


}

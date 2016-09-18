package characters;

import game.floor.Location;
import items.Direction;
import items.Distance;
import items.GameObject;

public class LectureBot extends Character{

	Location characterLocation;

	public LectureBot(int characterID, String characterName) {
		super(characterID, characterName);
		// TODO Auto-generated constructor stub
	}

	public Location getCharacterLocation() {
		return super.getCharacterLocation();
	}

	public void setCharacterLocation(int x, int y) {
		super.setCharacterLocation(x,y);
	}

	public void moveBot(Direction dir, Distance d) { // change paramenters?
		// set timer
		// move from one tile to another given tile
		// wait ****ms
		// move to XYZ
		// repeat etc.
	}

	@Override
	public void move(Direction dir, Distance d) {
		// TODO Auto-generated method stub

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

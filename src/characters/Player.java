package characters;

import game.floor.Location;
import items.Direction;
import items.Distance;
import items.GameObject;

public class Player extends Character {

	Location characterLocation;
	private boolean inRoom = false;



	public Player(int characterID, String characterName) {
		super(characterID, characterName);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void move(Direction dir, Distance d) {
		// altars details in tile in world
		// altars playerLocation

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

	@Override
	public void objectInteraction(GameObject c) {
		// TODO Auto-generated method stub

	}



}

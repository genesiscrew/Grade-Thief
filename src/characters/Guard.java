package characters;

import game.floor.Location;
import items.Direction;
import items.Distance;
import items.GameObject;

public class Guard extends Character {

	public int characterID;
	public String characterName;

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

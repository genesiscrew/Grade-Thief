package characters;

import game.floor.Location;
import items.GameObject;
import items.Interactable;
import items.Movable;

// chracter class comment test
public abstract class Character {
	String characterName;
	public int characterID;
	public Location characterLocation;

	public Character(int characterID, String characterName) {
		this.characterName = characterName;
		this.characterID = characterID;
	}

	public Location getCharacterLocation() {
		return characterLocation;
	}

	public abstract void characterInteraction(Character c);
	public abstract void objectInteraction(GameObject c);

	public void setCharacterLocation(int x, int y) {
		characterLocation = new Location(x, y);

	}







	/**
	 * To make a character move --
	 * get location
	 * remove the player from the tile at that location
	 * set location of character at new tile
	 * render
	 */

}

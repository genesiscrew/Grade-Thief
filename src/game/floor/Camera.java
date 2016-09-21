package game.floor;

import characters.Player;
import items.Direction;
import items.Direction.Dir;
import model.Game;

/**
 * this class represents a camera object within the game, the camera checks for intruders with certain coordinate space.
 * if it finds intruder it alerts the second player who is guard by displaying last seen position of thif player
 * @author abubakhami
 *
 */
public class Camera {
	Location cameraLocation;
	Direction dir;
	int itemID;

	public Camera(int itemID, Direction dir) {
		this.dir = dir;
		this.itemID = itemID;


	}

	public Location getCameraLocation() {
		return this.cameraLocation;
	}

	public void setCameraLocation(int x, int y) {
		cameraLocation = new Location(x,y);
	}


	public Boolean checkforIntruder(Game game) {
		if (dir.getDirection().equals(Dir.EAST)) {
			for (int i = 0; i < 6; i++) {
				if (game.getGameMap().getTileMap()[this.getCameraLocation().row() + i][this.getCameraLocation().column()] instanceof EmptyTile
						&& game.getGameMap().getTileMap()[this.getCameraLocation().row() + i][this.getCameraLocation().column()].occupied()
						&&  ((EmptyTile)game.getGameMap().getTileMap()[this.getCameraLocation().row() + i][this.getCameraLocation().column()]).getObjectonTile() instanceof Player) {
					System.out.println("we have found an intruder");
					return true;

				}
			}

		} else if (dir.getDirection().equals(Dir.WEST)) {
			for (int i = 0; i < 6; i++) {

				if (game.getGameMap().getTileMap()[this.getCameraLocation().row() - i][this.getCameraLocation().column()] instanceof EmptyTile
						&& game.getGameMap().getTileMap()[this.getCameraLocation().row() - i][this.getCameraLocation().column()].occupied()
						&& ((EmptyTile)game.getGameMap().getTileMap()[this.getCameraLocation().row() - i][this.getCameraLocation().column()]).getObjectonTile() instanceof Player) {
					System.out.println("we have found an intruder");
					return true;

				}
			}

		} else if (dir.getDirection().equals(Dir.NORTH)) {
			for (int i = 0; i < 6; i++) {
				if (game.getGameMap().getTileMap()[this.getCameraLocation().row() ][this.getCameraLocation().column()+1] instanceof EmptyTile
						&& game.getGameMap().getTileMap()[this.getCameraLocation().row()][this.getCameraLocation().column()+1].occupied()
						&& ((EmptyTile)game.getGameMap().getTileMap()[this.getCameraLocation().row()][this.getCameraLocation().column()+1]).getObjectonTile() instanceof Player) {
					System.out.println("we have found an intruder");
					return true;

				}
			}

		} else if (dir.getDirection().equals(Dir.SOUTH)) {
			for (int i = 0; i < 6; i++) {
				if (game.getGameMap().getTileMap()[this.getCameraLocation().row()][this.getCameraLocation().column()-i] instanceof EmptyTile
						&& game.getGameMap().getTileMap()[this.getCameraLocation().row()][this.getCameraLocation().column()-1].occupied()
						&& ((EmptyTile)game.getGameMap().getTileMap()[this.getCameraLocation().row()][this.getCameraLocation().column()-i]).getObjectonTile() instanceof Player) {
					System.out.println("we have found an intruder");
					return true;

				}
			}

		}
		return false;

	}



}

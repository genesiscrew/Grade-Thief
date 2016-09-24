package game.floor;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import gui.Drawable;
import items.Container;
import items.Door;
import items.Laptop;
import items.Marker;
import model.Game;
/**
 *
 * @author Stefan Vrecic
 *
 */
public class makeRoomTest {

	public static void main(String[] args) throws IOException {
		makeRoomTest m = new makeRoomTest(null);
		Room r = m.t();
	}

	public Room room;

	public makeRoomTest(Room r) throws IOException {
		this.room = t();
	}
			public Room t() throws IOException {
		Door d = new Door(0000, "0001",0);
		Room r = new Room(null, d);

		String co237 = System.getProperty("user.dir") + "/src/game/floor/co237";

		Room room_co237 = new Room(null, null);

		room_co237.setTileMap(co237);
		room_co237.getRoomTileMap().setRoom(room_co237);

		r.setTileMap(co237);

		TileMap tileMap = r.getRoomTileMap();
		String s = "";
		for (int y = 0 ; y < tileMap.getMapHeight(); y++ ) {
			for (int x = 0 ; x < tileMap.getMapWidth() ; x++) {
				Tile t = tileMap.getTileMap()[x][y];
				if (t != null)
					s = s + (tileMap.getTileMap()[x][y].getName());
			}
			s = s + "\n";
		}

		System.out.println(s);
		System.out.println(r.getRoomTileMap().getItems());

		tileMap.populateRoom(r, tileMap.getItems(), null);

		System.out.println("got her hoooooooooooooooooooooooooooooooooooooooooorrrrrrrrrrrrray");
		s = "";
		for (int y = 0 ; y < tileMap.getMapHeight(); y++ ) {
			for (int x = 0 ; x < tileMap.getMapWidth() ; x++) {
				Tile t = tileMap.getTileMap()[x][y];
				if (t != null) {
					if (tileMap.getTileMap()[x][y].occupied())
						s = s  + (tileMap.getTileMap()[x][y].getObjectonTile().toString());
					else
						s = s + (tileMap.getTileMap()[x][y].getName());
				}
			}
			s = s + "\n";
		}
		System.out.println(s);

		System.out.println(tileMap.getTileMap()[6][4]);
		Object o = tileMap.getTileMap()[6][4].getObjectonTile();

		Container c = (Container) o;
		System.out.println("bchecking inside tile 6,4 ");
//		System.out.println(c);
//		System.out.println(c.getItems());
		System.out.println("finished checking");

		for (Drawable lp : r.getDrawableItems()) {
//			Marker laptop = (Marker) lp;
//			System.out.println(laptop.toString());
		}
		return r;

	}
	}

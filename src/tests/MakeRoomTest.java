package tests;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import characters.Player;
import game.floor.Floor;
import game.floor.FloorMap;
import game.floor.Location;
import game.floor.Room;
import game.floor.Tile;
import game.floor.TileMap;
import items.Container;
import items.Door;
import model.Game;

/**
 * @author Stefan Vrecic
 */
public class MakeRoomTest {

    public static void main(String[] args) throws IOException {


        // make a floor
        /// load the rooms and add it.
        // get file path
        // make a new room
        // put the file path into room.setTileMap(filePath)
        // this will delegate the work to TileMap.java

        List<gui.Room> floorRooms = new ArrayList<gui.Room>();
        Game game = new Game();
        int nextX = 0;
        int nextY = 0;
        final int ADJACENT = 1; // adjacent rooms, add extra wall

        Door d = new Door(0000, "0001", 0, null);

        String co237 = System.getProperty("user.dir") + "/src/game/floor/level";


        gui.Room room_co237 = new gui.Room("level", 0, 0);

        Door door_co237 = new Door(0000, "237", 0, room_co237);

        floorRooms.add(room_co237);


        TileMap tileMap = room_co237.getTileMap();

        int mapWidth = tileMap.getMapWidth();
        int mapHeight = tileMap.getMapHeight();
        String s = "";

        for (int y = 0; y < mapHeight; y++) {
            for (int x = 0; x < mapWidth; x++) {
                s = s + tileMap.getTileMap()[x][y].getName();
            }
            s = s + "\n";
        }
        System.out.println(s);
    }

}
package game.floor;

import java.io.File;

import items.Door;

public class makeRoomTest {

    public static void main(String[] args) {


        Door d = new Door(0000, 0001);
        Room r = new Room(null, d);
        String f = System.getProperty("user.dir") + "/src/game/floor/room";

        r.setTileMap(f);

        for (int y = 0; y < 15; y++) {
            for (int x = 0; x < 30; x++) {
                System.out.println(r.roomTileMap[x][y] + " location ");
            }
        }


    }

}

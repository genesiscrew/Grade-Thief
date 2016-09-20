package gui;

import java.awt.Color;
import java.io.IOException;
import java.util.Random;

import game.floor.Room;
import game.floor.TileMap;

public class Terrain {

    private Random r;
    private double roughness = 0;


    private int mapSize = 33;

    private double size = 10;
    private Color G = new Color(120, 100, 80);



    public Terrain() throws IOException {

    	String co237 = System.getProperty("user.dir") + "/src/game/floor/co237";
		Room room_co237 = new Room(null, null);
		room_co237.setTileMap(co237);

        TileMap tileMap = room_co237.getRoomTileMap();
        // mapSize = 1 + 2 * Math.max(tileMap.getMapHeight(),tileMap.getMapWidth());

		System.out.println("mapSize " + mapSize);

        r = new Random();
        double[] values1 = new double[mapSize];
        double[] values2 = new double[values1.length];

        for (int y = 0; y < values1.length / 2; y += 2) {
            for (int i = 0; i < values1.length; i++) {
                values2[i] = r.nextDouble() * roughness;
                values2[i] = r.nextDouble() * roughness;
            }

            if (y != 0) {
                for (int x = 0; x < values1.length / 2; x++) {
                    Screen.DPolygons.add(new ThreeDPolygon(
                            new double[]{(size * x),  (size * x),  size + (size * x), size + (size * x)},
                            new double[]{(size * y),  size + (size * y), size + (size * y),  (size * y)},
                            new double[]{values1[x], values2[x], values2[x + 1],  values1[x + 1]}, G, false));
                }
            }

            if (y != 0) {
                for (int x = 0; x < values1.length / 2; x++) {
                    Screen.DPolygons.add(new ThreeDPolygon(
                            new double[]{(size * x), (size * x), size + (size * x), size + (size * x)},
                            new double[]{(size * (y + 1)), size + (size * (y + 1)), size + (size * (y + 1)), (size * (y + 1))},
                            new double[]{values1[x], values2[x], values2[x + 1], values1[x + 1]}, G, false));
                }
            }
        }
    }


    public int getMapSize() {
        return mapSize;
    }

    public double getTileSize() {
        return size;
    }
}
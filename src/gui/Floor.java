package gui;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import game.floor.Room;
import game.floor.TileMap;

/**
 * This is the floorPolygons of the map
 */
public class Floor {

    private final int mapWidth = 30;
    private final int mapHeight = mapWidth;

    private double tileSize = 10;
    private Color tileColor = new Color(255, 208, 193);

    public Floor() {

    	String co237 = System.getProperty("user.dir") + "/src/game/floor/co237";
		Room room_co237 = new Room(null, null);
        try {
            room_co237.setTileMap(co237);
        } catch (IOException e) {
            e.printStackTrace();
        }

        TileMap tileMap = room_co237.getRoomTileMap();
        // mapSize = 1 + 2 * Math.max(tileMap.getMapHeight(),tileMap.getMapWidth());
		System.out.printf("Width: %d, Height: %d \n" , mapWidth, mapHeight);
    }

    /**
     * Return a new map being the size of the width and height set in the fields
     * @return
     */
    public List<ThreeDPolygon> generateMap(){
        Random r = new Random();
        double[] values1 = new double[mapWidth];
        double[] values2 = new double[mapHeight];

        List<ThreeDPolygon> polygonFloor = new ArrayList<>();

        for (int y = 0; y < mapWidth; y++) {
            for (int x = 0; x < mapHeight - 1; x++) {
            	if (x==0 && y ==0)
            		tileColor = Color.black;
            	else
            		tileColor = new Color(255, 208, 193);

               polygonFloor.add(new ThreeDPolygon(
                        new double[]{(tileSize * x),  (tileSize * x),  tileSize + (tileSize * x), tileSize + (tileSize * x)},
                        new double[]{(tileSize * y),  tileSize + (tileSize * y), tileSize + (tileSize * y),  (tileSize * y)},
                        new double[]{values1[x], values2[x], values2[x + 1],  values1[x + 1]}, tileColor, false));


            }
        }
        return polygonFloor;
    }

    public double getTileSize() {
        return tileSize;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public int getMapWidth() {
        return mapWidth;
    }
}
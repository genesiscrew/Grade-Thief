package gui;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import game.floor.Room;
import game.floor.TileMap;

/**
 * This is the floor of the games map. It can generate a new floor from the width and height specified.
 *
 */
public class Floor {

    private int mapWidth;
    private int mapHeight;
    private int xOffset;
    private int yOffset;

    private double tileSize = 10;
    private Color tileColor = new Color(255, 208, 193);

    /**
     * Make a new floor with the specified paramaters
     * @param xOffset
     * @param yOffset
     * @param width
     * @param height
     */
    public Floor(int xOffset, int yOffset, int width, int height) {
        this.mapWidth = width;
        this.mapHeight = height;
        this.xOffset = xOffset;
        this.yOffset = yOffset;

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
    public List<Polygon> generateMap(){
        List<Polygon> polygonFloor = new ArrayList<>();

        for (int x = 0; x < mapWidth; x++) {
            for (int y = 0; y < mapHeight; y++) {
               polygonFloor.add(new Polygon(
                        new double[]{(tileSize * x)+xOffset,  (tileSize * x)+xOffset,  tileSize + (tileSize * x)+ xOffset, xOffset + tileSize + (tileSize * x)},
                        new double[]{yOffset + (tileSize * y),  yOffset + tileSize + (tileSize * y), yOffset + tileSize + (tileSize * y),  yOffset + (tileSize * y)},
                        new double[]{0, 0, 0, 0}, tileColor, false));
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

    public int getxOffset() {
        return xOffset;
    }

    public int getyOffset() {
        return yOffset;
    }
}

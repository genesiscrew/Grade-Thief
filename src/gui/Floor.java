package gui;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import game.floor.*;
import game.floor.Room;
import items.Door;
import items.Item;
import items.Wall;

/**
 * @Author Adam Wareing
 * This is the floor of the games map. It can generate a new floor from the width and height specified.
 */
public class Floor {

    private int mapWidth;
    private int mapHeight;
    private int xOffset;
    private int yOffset;

    private double tileSize = 10;
    private final double WALL_HEIGHT = 20;

    private final Color WALL_COLOR = new Color(79, 200, 255);
    private final Color TILE_COLOR = new Color(255, 208, 193);
    private final Color DOOR_COLOR = new Color(0, 0, 193);

    /**
     * Make a new floor with the specified parameters
     *
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

        System.out.printf("Width: %d, Height: %d \n", mapWidth, mapHeight);
        System.out.printf("x: %d, y: %d \n", xOffset, yOffset);
    }

    /**
     * Return a new map being the size of the width and height set in the fields
     *
     * @return
     */
    public List<Polygon> generateMap() {
        List<Polygon> polygonFloor = new ArrayList<>();

        for (int x = 0; x < mapWidth; x++) {
            for (int y = 0; y < mapHeight; y++) {
                polygonFloor.add(new Polygon(
                        new double[]{(tileSize * x) + xOffset, (tileSize * x) + xOffset, tileSize + (tileSize * x) + xOffset, xOffset + tileSize + (tileSize * x)},
                        new double[]{yOffset + (tileSize * y), yOffset + tileSize + (tileSize * y), yOffset + tileSize + (tileSize * y), yOffset + (tileSize * y)},
                        new double[]{0, 0, 0, 0}, TILE_COLOR, false));
            }
        }
        return polygonFloor;
    }

    /**
     *
     */
    public List<Item> parseWalls(Tile[][] tileMap) {
        List<Item> walls = new ArrayList<>();

        for (int x = 0; x < tileMap.length; x++) {
            for (int y = 0; y < tileMap[x].length; y++) {
                if (tileMap[x][y] instanceof WallTile) {
                    Wall wall = new Wall(0, "", x * tileSize, y * tileSize, 0, tileSize, tileSize, WALL_HEIGHT, WALL_COLOR);
                    walls.add(wall);
                }
            }
        }
        return walls;
    }

    /**
     *
     */
    public List<Door> parseDoors(Tile[][] tileMap) {
        List<Door> doors = new ArrayList<>();

        for (int x = 0; x < tileMap.length; x++) {
            for (int y = 0; y < tileMap[x].length; y++) {
                if (tileMap[x][y] instanceof DoorTile) {
                    Door door = new Door(0, "", x * tileSize, y * tileSize, 0, tileSize, tileSize, WALL_HEIGHT, DOOR_COLOR);
                    doors.add(door);
                }
            }
        }
        return doors;
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
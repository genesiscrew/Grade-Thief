package view;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.floor.*;
import model.items.Door;
import model.items.Item;
import model.items.Wall;
import model.rendering.Polygon;

/**
 * @Author Adam Wareing
 * This is the floor of the games map. It can generate a new floor from the width and height specified, as well as
 * parse walls and doors from a 2D array of Tile.
 */
public class Floor {

    private int mapWidth;
    private int mapHeight;
    private int xOffset;
    private int yOffset;

    private double tileSize = 10;
    private final double WALL_HEIGHT = 20;

    private final Color WALL_COLOR = new Color(183, 184, 182);
    private final Color TILE_COLOR = new Color(78, 84, 0);
    private final Color DOOR_COLOR = new Color(52,103 , 92);

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

        System.out.printf("Width: %d, Height: %d \n", mapWidth, mapHeight);
        System.out.printf("x: %d, y: %d \n", xOffset, yOffset);
    }

    /**
     * Generate a new map being the size of the width and height set in the fields
     * @return - all polygon objects that make up the floor
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
     *This generates a list of doors from the tile map. For every door in the tile map a door object with the
     * position, size and colour properties is created.
     * @return - all items in the tile map generated into objects
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
     * This generates a list of doors from the tile map. For every door in the tile map a door object with the
     * position, size and colour properties is created.
     * @return - all doors in the tile map generated into objects
     */
    public List<Door> parseDoors(Tile[][] tileMap) {
        List<Door> doors = new ArrayList<>();
        int doorCount = 0;
        for (int x = 0; x < tileMap.length; x++) {
            for (int y = 0; y < tileMap[x].length; y++) {
                if (tileMap[x][y] instanceof DoorTile) {
                    Door door = new Door(doorCount++, "", x * tileSize, y * tileSize, 0, tileSize, tileSize, WALL_HEIGHT, DOOR_COLOR);
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
package gui;

import game.floor.TileMap;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import items.Player;
import tests.MakeRoomTest;

/**
 * @Author Adam Wareing
 *
 */
public class Room {

    /**
     * This is all the individual polygons that will be drawn
     */
    List<Polygon> polygons = new ArrayList<>();

    /**
     * The polygons floorPolygons
     */
    List<Polygon> floorPolygons;

    /**
     * The roomObjects in the room
     */
    ArrayList<Drawable> roomObjects = new ArrayList<Drawable>();

    Floor floor = new Floor(0, 0, 10, 10);
    TileMap tileMap = null;
    /**
     * The padding on the inside of the wall. It stops the player from getting too close and preventing
     * the graphics to not draw
     */
    private final double ROOM_PADDING = 5;

    /**
     *
     */
    public Room(String roomName, int xOffset, int yOffset) {
        floor = new Floor(xOffset, yOffset, 20, 20);
        this.floorPolygons = floor.generateMap();
        this.polygons = new ArrayList<>();

        try {
            addObjectsToMap(roomName);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        floor = new Floor(0, 0, tileMap.getMapWidth(), tileMap.getMapHeight());
        this.floorPolygons = floor.generateMap();
    }

    private void addObjectsToMap(String roomName) throws IOException {
        //try {System.out.println("room1"); Thread.sleep(5000); } catch (Exception e) {}
        //System.out.println("called once?????/");
        int wallLength = (int) (floor.getMapHeight() * floor.getTileSize()) - 5;
        int wallHeight = 50;

        MakeRoomTest m = new MakeRoomTest();
        m.createRoom(this, roomName);
        roomObjects.add(new Player(20, 20, 0, 5, 3, 12, Color.green));
    }

    /**
     * Is position x, y, z outside of the floorPolygons.
     * Currently it doesn't take the z value into account, as its not required.
     *
     * @param x
     * @param y
     * @param z
     * @param startX
     * @param startY
     */
    public boolean positionOutOfBounds(double x, double y, double z, int startX, int startY) {
        double mapWidth = floor.getMapWidth() * floor.getTileSize();
        double mapHeight = floor.getMapHeight() * floor.getTileSize();

        if (x <  floor.getxOffset() + ROOM_PADDING || y < floor.getyOffset() + ROOM_PADDING)
            return true;
        if ((x + ROOM_PADDING) > (startX + mapWidth - floor.getxOffset()))
            return true;
        if((y + ROOM_PADDING) > (startY + mapHeight - floor.getyOffset()))
            return true;
        return false;
    }


    /**
     * Check to see if the player is moving into an object.
     *
     * @return True if the player is moving into an object, false otherwise.
     */
    public boolean movingIntoAnObject(double x, double y, double z) {
        for (Drawable o : roomObjects) {
            if (o.containsPoint((int) x, (int) y, (int) z)) {
                return true;
            }
        }
        return false;
    }


    public void setTileMap(String f) throws IOException {
        TileMap t = new TileMap(null, this);
        this.tileMap = t.createTileMap(f);
    }

    public ArrayList<Drawable> getRoomObjects() {
        return roomObjects;
    }

    public void setRoomObjects(ArrayList<Drawable> roomObjects) {
        this.roomObjects = roomObjects;
    }

    public List<Polygon> getPolygons() {
        return polygons;
    }

    public void setPolygons(List<Polygon> polygons) {
        this.polygons = polygons;
    }

    public List<Polygon> getFloorPolygons() {
        return floorPolygons;
    }

    public void setFloorPolygons(List<Polygon> floorPolygons) {
        this.floorPolygons = floorPolygons;
    }

    public Floor getFloor() {
        return floor;
    }

    public void setFloor(Floor floor) {
        this.floor = floor;
    }

    public void addDrawableItems(Drawable drawableItems) {
        this.roomObjects.add(drawableItems);
    }

    public TileMap getTileMap() {
        return tileMap;
    }

    public void setTileMap(TileMap tileMap) {
        this.tileMap = tileMap;
    }
}
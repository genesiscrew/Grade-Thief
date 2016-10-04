package gui;

import game.floor.TileMap;
import items.Door;
import items.Item;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import characters.GuardBot;

/**
 * @Author Adam Wareing
 */
public class Room {
    private Door door;
    private int sx = -1;
    private int sy = -1;
    private int w = -1;
    private int h = -1;
    List<GuardBot> guardList;

    private List<Door> doors = new ArrayList<>();

    /**
     * This is all the individual polygons that will be drawn
     */
    private List<Polygon> polygons = new ArrayList<>();

    /**
     * The polygons floorPolygons
     */
    private List<Polygon> floorPolygons;

    /**
     * The roomObjects in the room
     */
    private ArrayList<Item> roomObjects = new ArrayList<>();

    /**
     * The Walls in the room
     */
    private List<Item> walls = new ArrayList<>();

    Floor floor = new Floor(0, 0, 10, 10);

    Color floorColor = new Color(0,0,0);
    TileMap tileMap;


    /**
     * The padding on the inside of the wall. It stops the player from getting too close and preventing
     * the graphics to not draw
     */

    private final double ROOM_PADDING = 10;

    /**
     *
     */
    public Room(String roomName, int xOffset, int yOffset) {
        floor = new Floor(xOffset, yOffset, 20, 20);
        this.floorPolygons = floor.generateMap();
        this.polygons = new ArrayList<>();
        this.guardList = new ArrayList<GuardBot>();

        this.setTileMap(System.getProperty("user.dir") + "/src/game/floor/" + roomName);

      //  System.out.println("" + tileMap.getItems());
        tileMap.populateRoom(this, tileMap.getItems(), null);

        floor = new Floor(0, 0, tileMap.getMapWidth(), tileMap.getMapHeight());
        this.floorPolygons = floor.generateMap();
        this.walls = floor.parseWalls(this.tileMap.getTileMap());
        this.doors = floor.parseDoors(this.tileMap.getTileMap());
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

        if (x < floor.getxOffset() + ROOM_PADDING || y < floor.getyOffset() + ROOM_PADDING)
            return true;
        if ((x + ROOM_PADDING) > (startX + mapWidth - floor.getxOffset()))
            return true;
        if ((y + ROOM_PADDING) > (startY + mapHeight - floor.getyOffset()))
            return true;
        return false;
    }


    /**
     * Check to see if the player is moving into an object.
     *
     * @return True if the player is moving into an object, false otherwise.
     */
    public boolean movingIntoAnObject(double x, double y, double z) {
        if(isPointInAnyObjects(x,y,z, doors))
            return true;
        if(isPointInAnyObjects(x,y,z,walls))
            return true;
        if(isPointInAnyObjects(x,y,z, roomObjects))
            return true;

        return false;
    }

    private boolean isPointInAnyObjects(double x, double y, double z, List<? extends Item> objects){
        for (Drawable o : objects) {
            if (o.containsPoint((int) x, (int) y, (int) z))
                return true;
        }
        return false;
    }

    public void removeRoomObject(Item item) {
    	roomObjects.remove(item);
    }
    public void setTileMap(String f) {
        System.out.println("generating tileMap for " + f);
        TileMap t = new TileMap(null, this);
        this.tileMap = t.createTileMap(f);
    }

    public ArrayList<Item> getRoomObjects() {
        return roomObjects;
    }

    public void setRoomObjects(ArrayList<Item> roomObjects) {
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

    public void addItemToRoom(Item drawableItems) {
        this.roomObjects.add(drawableItems);
    }

    public TileMap getTileMap() {
        return tileMap;
    }

    public void setTileMap(TileMap tileMap) {
        this.tileMap = tileMap;
    }

    /**
     * @param sx -- start x,y
     * @param sy
     * @param w  -- width, height
     * @param h  this sets the dimensions of the room RELATIVE to the floor
     *           for malleability; the room will know it's global position on the floor and the floor will also know the bounding
     *           box of the room.
     */
    public void setBoundingBox(int sx, int sy, int w, int h) {
        this.sx = sx;
        this.sy = sy;
        this.w = w;
        this.h = h;
    }

    public List<Door> getDoors() {
        return this.doors;
    }

    public int[] getBoundingBox() {
        return new int[]{sx, sy, w, h};
    }

    public int roomGetCode() {
        return this.door.code;
    }

    public Door getDoor() {
        return door;
    }

    public int getSx() {
        return sx;
    }

    public int getSy() {
        return sy;
    }

    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }

    public List<Item> getWalls() {
        return walls;
    }

	public void addGuardtoRoom(GuardBot guard) {

		guardList.add(guard);

	}
	  public List<Door> getGuards() {
	        return this.doors;
	    }
}
package view;

import model.characters.GuardBot;
import model.floor.Location;
import model.floor.TileMap;
import model.items.Door;
import model.items.Item;
import model.rendering.Drawable;
import model.rendering.Polygon;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Adam Wareing
 * The room
 */
public class Room {
    private int width = 20;
    List<GuardBot> guardList;
    Location playerStart;

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

    /**
     * The floor of the room
     */
    private Floor floor = new Floor(0, 0, 10, 10);

    /**
     * The tileMap which stores the 2D array of the floor, and provides util methods
     */
    private TileMap tileMap;

    /**
     * Add a door to the room
     *
     * @param d
     */
    public void addDoor(Door d) {
        this.doors.add(d);
    }

    /**
     * The padding on the inside of the wall. It stops the player from getting too close and preventing
     * the graphics to not draw
     */

    public final double ROOM_PADDING = 10;

    /**
     * Create a new instance
     *  @param roomName
     *
     */
    public Room(String roomName) {
        this.setTileMap(System.getProperty("user.dir") + "/src/model/floor/" + roomName);
        this.floorPolygons = floor.generateMap();
        this.polygons = new ArrayList<>();
        this.guardList = new ArrayList<>();
        tileMap.populateRoom(this, tileMap.getItems(), null);

        floor = new Floor(0, 0, tileMap.getMapWidth(), tileMap.getMapHeight());
        this.width = tileMap.getMapWidth();
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
     */
    public boolean positionOutOfBounds(double x, double y, double z) {
        double mapWidth = floor.getMapWidth() * floor.getTileSize();
        double mapHeight = floor.getMapHeight() * floor.getTileSize();

        if (x < floor.getxOffset() + ROOM_PADDING || y < floor.getyOffset() + ROOM_PADDING)
            return true;
        if ((x + ROOM_PADDING) > (Screen.startX + mapWidth - floor.getxOffset()))
            return true;
        if ((y + ROOM_PADDING) > (Screen.startY + mapHeight - floor.getyOffset()))
            return true;
        return false;
    }


    /**
     * Check to see if the player is moving into an object.
     *
     * @return True if the player is moving into an object, false otherwise.
     */
    public boolean movingIntoAnObject(double x, double y, double z) {
        if (isPointInAnyObjects(x, y, z, doors))
            return true;
        if (isPointInAnyObjects(x, y, z, walls))
            return true;
        if (isPointInAnyObjects(x, y, z, roomObjects))
            return true;

        return false;
    }

    /**
     * Do any objects in the room contain the point x,y,z
     *
     * @param x
     * @param y
     * @param z
     * @param objects
     * @return - true if any object contains the point
     */
    private boolean isPointInAnyObjects(double x, double y, double z, List<? extends Item> objects) {
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
        TileMap t = new TileMap(null, this);
        this.tileMap = t.createTileMap(f);
    }

    public ArrayList<Item> getRoomObjects() {
        return roomObjects;
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

    public List<Door> getDoors() {
        return this.doors;
    }

    public int getWidth() {
        return width;
    }

    public List<Item> getWalls() {
        return walls;
    }

    public void setPlayerStart(int x, int y) {
        playerStart = new Location(x, y);
    }

    public Location getPlayerStart() {
        return playerStart;
    }
}
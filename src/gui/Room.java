package gui;

import game.floor.Location;
import game.floor.TileMap;
import items.Door;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import tests.MakeRoomTest;

/**
 * @Author Adam Wareing
 *
 */
public class Room {
	// a room should be laid out in a grid
	//Tile[][] roomTileMap; // this contains the arrangement of the room
	private TileMap roomTileMap;
	private Door door;
	private int sx = -1;
	private int sy = -1;
	private int w = -1;
	private int h = -1;
	List<Location> doorLocations = new ArrayList<Location>();
	private List<Drawable> drawableItems = new ArrayList<Drawable>();

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
    private final double ROOM_PADDING = 10;

    /**
     * @throws IOException
     */
    public Room(String roomName) throws IOException {
        floor = new Floor(0, 0, 20, 20);
        this.floorPolygons = floor.generateMap();
        this.polygons = new ArrayList<>();

        // Added by Stefan
        this.setTileMap(System.getProperty("user.dir") + "/src/game/floor/" + roomName);
        tileMap.populateRoom(this,	tileMap.getItems(), null);

        addObjectsToMap(roomName);
        floor = new Floor(0, 0, tileMap.getMapWidth(), tileMap.getMapHeight());
        this.floorPolygons = floor.generateMap();
    }

    private void addObjectsToMap(String roomName) throws IOException {
        //try {System.out.println("room1"); Thread.sleep(5000); } catch (Exception e) {}
        //System.out.println("called once?????/");
        int wallLength = (int) (floor.getMapHeight() * floor.getTileSize()) - 5;
        int wallHeight = 50;

//        MakeRoomTest m = new MakeRoomTest();
//        m.createRoom(this, roomName);
    }

//    private void addObjectsToMap() {
//        int wallLength = (int) (floor.getMapHeight() * floor.getTileSize()) - 5;
//        int wallHeight = 50;
//
//        roomObjects.add(new Cube(0, 0, 0, 5, wallLength, wallHeight, Color.blue));
//        roomObjects.add(new Cube(5, 0, 0, wallLength - 10, 5, wallHeight, Color.blue));
//        roomObjects.add(new Cube(wallLength - 5, 0, 0, 5, wallLength, wallHeight, Color.blue));
//        roomObjects.add(new Cube(0, wallLength, 0, wallLength - 5, 5, wallHeight, Color.blue));
//
//        roomObjects.add(new Table(50, 50, 0, 20, 50, 7, Color.red));
//        roomObjects.add(new Laptop(55, 50, 7, 5, 3, 4, Color.black));
//
//        // Chairs
//        roomObjects.add(new Chair(75, 60, 0, 5, 5, 5, Color.red));
//        roomObjects.add(new Chair(75, 70, 0, 5, 5, 5, Color.red));
//        roomObjects.add(new Chair(75, 80, 0, 5, 5, 5, Color.red));
//
//        roomObjects.add(new Chair(40, 60, 0, 5, 5, 5, Color.red));
//        roomObjects.add(new Chair(40, 70, 0, 5, 5, 5, Color.red));
//        roomObjects.add(new Chair(40, 80, 0, 5, 5, 5, Color.red));
//
//        roomObjects.get(0).updateDirection(60,60);
//
//
//        roomObjects.add(new Player(20, 20, 0, 5, 3, 12, Color.green));
//
//    }
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

	/**

	 * @param sx -- start x,y
	 * @param sy
	 * @param w -- width, height
	 * @param h
	 *  this sets the dimensions of the room RELATIVE to the floor
	 * for malleability; the room will know it's global position on the floor and the floor will also know the bounding
	 * box of the room.
	 */
	public void setBoundingBox(int sx, int sy, int w, int h) {
		this.sx = sx;
		this.sy = sy;
		this.w = w;
		this.h = h;
	}

	public List<Location> getDoorLocations() {
		return this.doorLocations;
	}

	public void addDoorLocation(Location loc) {
		this.doorLocations.add(loc);
	}

	public int[] getBoundingBox() {
		return new int[] { sx, sy, w, h };
	}

	public int roomGetCode() {
		return this.door.code;
	}

	public TileMap getRoomTileMap() {
		return roomTileMap;
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





}
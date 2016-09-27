package gui;

import items.Chair;
import items.Laptop;
import items.Table;
import tests.MakeRoomTest;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



/**
 * Created by wareinadam on 24/09/16.
 */
public class Room {

    /**
     * This is all the individual polygons that will be drawn
     */
    List<ThreeDPolygon> polygons = new ArrayList<>();

    /**
     * The polygons floorPolygons
     */
    List<ThreeDPolygon> floorPolygons;

    /**
     * The roomObjects in the room
     */
    ArrayList<Drawable> roomObjects = new ArrayList<Drawable>();

    Floor floor;

    /**
     * @throws IOException
     *
     */
    public Room(int type) throws IOException{
        floor = new Floor();
        this.floorPolygons = floor.generateMap();
        this.polygons = new ArrayList<>();

        if(type == 1)
            addObjectsToMap();
        else
            addObjectsToMap2();

    }

    private void addObjectsToMap() {
        int wallLength = (int) (floor.getMapHeight() * floor.getTileSize()) - 5;
        int wallHeight = 50;

        roomObjects.add(new Cube(0, 0, 0, 5, wallLength, wallHeight, Color.blue));
        roomObjects.add(new Cube(5, 0, 0, wallLength - 10, 5, wallHeight, Color.blue));
        roomObjects.add(new Cube(wallLength - 5, 0, 0, 5, wallLength, wallHeight, Color.blue));
        roomObjects.add(new Cube(0, wallLength, 0, wallLength - 5, 5, wallHeight, Color.blue));

        roomObjects.add(new Table(50, 50, 0, 20, 50, 7, Color.red));
        roomObjects.add(new Laptop(55, 50, 7, 5, 3, 4, Color.black));

        // Chairs
        roomObjects.add(new Chair(40, 60, 0, 5, 5, 5, Color.red));
        roomObjects.add(new Chair(40, 70, 0, 5, 5, 5, Color.red));
        roomObjects.add(new Chair(40, 80, 0, 5, 5, 5, Color.red));

        roomObjects.add(new Chair(75, 60, 0, 5, 5, 5, Color.red));
        roomObjects.add(new Chair(75, 70, 0, 5, 5, 5, Color.red));
        roomObjects.add(new Chair(75, 80, 0, 5, 5, 5, Color.red));
    }

    private void addObjectsToMap2() throws IOException {
    	//try {System.out.println("room1"); Thread.sleep(5000); } catch (Exception e) {}
    	//System.out.println("called once?????/");
        int wallLength = (int) (floor.getMapHeight() * floor.getTileSize()) - 5;
        int wallHeight = 50;

    	makeRoomTest m = new MakeRoomTest(null);
		game.floor.Room r = m.t();
		for (Drawable d : r.getDrawableItems()) {
			roomObjects.add(d);
		}

//        roomObjects.add(new Cube(0, 0, 0, 5, wallLength, wallHeight, Color.blue));
//        roomObjects.add(new Cube(5, 0, 0, wallLength - 10, 5, wallHeight, Color.blue));
//        roomObjects.add(new Cube(wallLength - 5, 0, 0, 5, wallLength, wallHeight, Color.blue));
//        roomObjects.add(new Cube(0, wallLength, 0, wallLength - 5, 5, wallHeight, Color.blue));
//
//        roomObjects.add(new Table(50, 50, 0, 20, 50, 7, Color.red));
//        roomObjects.add(new Laptop(55, 50, 7, 5, 3, 4, Color.black));
    }

    public ArrayList<Drawable> getRoomObjects() {
        return roomObjects;
    }

    public void setRoomObjects(ArrayList<Drawable> roomObjects) {
        this.roomObjects = roomObjects;
    }

    public List<ThreeDPolygon> getPolygons() {
        return polygons;
    }

    public void setPolygons(List<ThreeDPolygon> polygons) {
        this.polygons = polygons;
    }

    public List<ThreeDPolygon> getFloorPolygons() {
        return floorPolygons;
    }

    public void setFloorPolygons(List<ThreeDPolygon> floorPolygons) {
        this.floorPolygons = floorPolygons;
    }

    public Floor getFloor() {
        return floor;
    }

    public void setFloor(Floor floor) {
        this.floor = floor;
    }
}

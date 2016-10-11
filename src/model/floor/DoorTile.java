package model.floor;

import model.items.Door;

/**
 * @author Stefan Vrecic
 *         DoorTile.java instance of Tile which represents a space occupied by a door. Useful for the floor map.
 */

public class DoorTile extends EmptyTile {

    private Door door;
    private int doorCode = 0;
    Location location;
    private boolean isOccupied = false;


/**
 * returns the door attached to the tile
 * @return
 */
    public Door getDoor() {
        return this.door;
    }

    /**
     * attachs the door to the doorTIle
     * @param door
     */
    public void setDoor(Door door) {
        this.door = door;
    }


/**
 * returns the location of the DoorTile
 */
    @Override
    public Location tileLocation() {

        return location;
    }

    public Object getObjectonTile() {
        return null;
    }

    /**
     * returns whether a player is on the dorTIle
     */
    @Override
    public boolean occupied() {
        return isOccupied;
    }

    /**
     * sets the location of the doorTIle
     */
    @Override
    public void setLocation(Location l) {
        this.location = l;

    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return "D";
    }


    public String toString() {
        return this.getClass().getSimpleName() + " " + location.toString();
    }


    @Override
    public String name() {
        if (occupied())
            return "viewTo";
        return "D";
    }

    @Override
    public void setOccupied() {
        isOccupied = true;

    }

    @Override
    public void setUnoccupied() {
        isOccupied = false;
    }
}


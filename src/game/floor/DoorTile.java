package game.floor;

import items.Door;

/**
 * @author Stefan Vrecic
 *         DoorTile.java instance of Tile which represents a space occupied by a door. Useful for the floor map.
 */

public class DoorTile extends EmptyTile {

    private Door door;
    Location location;
    private boolean isOccupied = false;


    public Door getDoor() {
        return this.door;
    }

    public void setDoor(Door door) {
        this.door = door;
    }

    @Override
    public Location tileLocation() {

        return location;
    }

    public Object getObjectonTile() {
        return null;
    }

    @Override
    public boolean occupied() {
        return isOccupied;
    }

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


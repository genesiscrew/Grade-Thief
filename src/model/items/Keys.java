package model.items;

import java.util.List;

import view.Drawable;
import view.Polygon;

public class Keys implements Interactable, Movable, Drawable {

    String description = " a key";
    int keyID;

	public Keys(int itemID, String itemType, int keyID) {
		//super(itemID, itemType);
		this.keyID = keyID;
	}

	public String toString() {
		return "K";
	}
    public String itemType() {
        return null;
    }

    @Override
    public void pickUp(){
        // remove item from scene
        // add to inventory
    }

    @Override
    public void useItem(GameObject j) {
        System.out.println("test");
        System.out.println(j.itemType);
        System.out.println("test");

        if (j.itemType().equals("Door")) {
            j = (Door) j;
            Door jDoor = (Door) j;
            if (keyID == jDoor.itemID)
                System.out.println("unlocked the door");
            else
                System.out.println("Door still locked");

        } else if (j instanceof Container) {
            Container e = (Container) j;
            if (keyID == e.itemID)
                System.out.println("unlocked the container");
            else
                System.out.println("container still locked");
        }

    }

    @Override
    public void useItem() {

    }

    @Override
    public void move(Direction dir, Distance d) {
        // TODO Auto-generated method stub
    }

    @Override
    public void updateDirection(double toX, double toY) {

    }

    @Override
    public void updatePoly() {

    }

    @Override
    public void setRotAdd() {

    }

    @Override
    public void removeCube() {

    }

    @Override
    public boolean containsPoint(int x, int y, int z) {
        return false;
    }

    @Override
    public List<Polygon> getPolygons() {
        return null;
    }
}

package model.items;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import model.items.Item.Interaction;
import model.rendering.Cube;
import model.rendering.Polygon;

public class Container extends Item implements Interactable, Movable {

    public List<Item> containedItems = new ArrayList<Item>();
    private Container containedContainer;

    int keyID;
	private boolean locked;


    public Container(int itemID, String itemType, int keyID, double x, double y, double z,
                     double width, double length, double height, Color c) {

        super(itemID, itemType, x, y, z, width, length, height, c);
        if (containedItems != null) {
            this.containedItems = containedItems;
        } else {
            this.containedItems = new ArrayList<Item>();
        }
        this.keyID = keyID;
        cubes.add(new Cube(x, y, z, width, length, height, c));
        cubes.add(new Cube(x, y + (length / 4), z + height, width, length / 2, height, c.darker().darker()));
    }


    public void setItems(List<Item> containedItems) {
        this.containedItems = containedItems;
    }

    public List<Item> getItems() {
       
            System.out.println("container " + this.containedContainer);
            return this.containedItems;
    
       
    }

    @Override
    public void addInteractions() {
        interactionsAvailable = new ArrayList<>();
        interactionsAvailable.add(Interaction.UNLOCK);
        interactionsAvailable.add(Interaction.OPEN);
        interactionsAvailable.add(Interaction.CLOSE);
    }



        @Override
        public void pickUp() {

        }

        public String toString() {
        	int extra = 0;
            if (containedItems == null)
                    extra = 0;
            else
            	extra += containedItems.size();

            if (getContainedContainer() != null)
            	extra += getContainedContainer().containedItems.size() + 1; // container within container is considered item

            return String.valueOf(extra);
        }

        @Override
        public void useItem(GameObject j) {
            // TODO Auto-generated method stub

        }

        @Override
        public void useItem() {

        }

        @Override
        public void move(Direction dir, Distance d) {
            // TODO Auto-generated method stub

        }
        /**
         * add item into container
         */
        public void addItem(Item item) {
            System.out.println("adding item to container" + item.toString());
            this.containedItems.add(item);

        }






    public Container getContainedContainer() {
        return containedContainer;
    }

    public void setContainedContainer(Container containedContainer) {
        this.containedContainer = containedContainer;
    }




    @Override
    public void setRotAdd() {
        cubes.forEach(i -> i.setRotAdd());
    }

    @Override
    public void updateDirection(double toX, double toY) {
        cubes.forEach(i -> i.updateDirection(toX, toY));
    }

    @Override
    public void updatePoly() {
        cubes.forEach(i -> i.updatePoly());
    }

    @Override
    public void removeCube() {
        cubes.forEach(i -> i.removeCube());
    }

    @Override
    public boolean containsPoint(int x, int y, int z) {
        return (this.x + this.width) > x && (this.y + this.length) > y && this.x < x && this.y < y;
    }

    @Override
    public List<model.rendering.Polygon> getPolygons() {
        List<Polygon> allPolys = new ArrayList<>();
        // Add all the cubes polygons
        cubes.forEach(c -> allPolys.addAll(c.getPolygons()));
        return allPolys;
    }



    public void setLock() {
        this.locked = true;
    }

    public void unlock() {
        this.locked = false;
    }


	public int getKeyID() {
		// TODO Auto-generated method stub
		return this.keyID;
	}



}

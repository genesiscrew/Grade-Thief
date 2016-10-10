package model.items;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import model.rendering.Polygon;

public class Container extends Item implements Interactable, Movable {

    public List<Item> containedItems = new ArrayList<Item>();
    private Container containedContainer;

    int keyID;


    public Container(int itemID, List<Item> containedItems, String itemType, int keyID, double x, double y, double z,
                     double width, double length, double height, Color c) {

        super(itemID, itemType, x, y, z, width, length, height, c);
        if (containedItems != null) {
            this.containedItems = containedItems;
        } else {
            this.containedItems = new ArrayList<Item>();
        }
        this.keyID = keyID;
    }


    public void setItems(List<Item> containedItems) {
        this.containedItems = containedItems;
    }

    public List<Item> getItems() {
        if (this.containedContainer != null) {
            System.out.println("container " + this.containedContainer);
            return this.containedItems;
        }
        return null;
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

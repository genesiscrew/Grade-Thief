package model.items;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import model.rendering.Cube;
import model.rendering.Drawable;

/**
 * @Author Adam Wareing
 * This is an item in the game, it provides util methods that all items in the game world utilise.
 */
public abstract class Item extends GameObject implements Drawable {

    public final double DETECT_PLAYER_BOUNDARY = 10;

    protected double x;
    protected double y;
    protected double z;
    protected double width;
    protected double length;
    protected double height;
    protected Color color;
    protected List<Interaction> interactionsAvailable;
    protected boolean draw = true;
    protected java.util.List<Cube> cubes;
    protected boolean canPickup = false;

    /**
     * This sets up the itemID, itemType, position and size of the item to be rendered in the 3D world.
     *
     * @param itemID
     * @param itemType
     * @param x
     * @param y
     * @param z
     * @param width
     * @param length
     * @param height
     * @param c
     */
    public Item(int itemID, String itemType, double x, double y, double z, double width, double length, double height,
                Color c) {
        super(itemID, itemType);
        this.x = x;
        this.y = y;
        this.z = z;
        this.width = width;
        this.length = length;
        this.height = height;
        this.color = c;
        this.cubes = new ArrayList<>();
        addInteractions();
    }

    /**
     * Move an item by the specified amount.
     *
     * @param dx
     * @param dy
     * @param dz
     */
    public void moveItemBy(double dx, double dy, double dz) {
        this.x += dx;
        this.y += dy;
        this.z += dz;

        cubes.forEach(c -> c.updatePosition(dx, dy, dz));
    }

    /**
     * This adds all the interactions possible for an item. They should only be added here, so we can easily
     * determine what interactions an item can perform.
     */
    public void addInteractions() {
        interactionsAvailable = new ArrayList<>();
    }

    /**
     * Is the specified point near an object? NEAR is specified by 'DETECT_PLAYER_BOUNDARY' which is the tolerance of
     * the boundary.
     *
     * @param x
     * @param y
     * @param z
     * @return true if the player is near, false otherwise.
     */
    public boolean pointNearObject(double x, double y, double z) {
        if ((this.x + DETECT_PLAYER_BOUNDARY + this.width) > x && (this.y + DETECT_PLAYER_BOUNDARY + this.length) > y
                && this.x - DETECT_PLAYER_BOUNDARY < x && this.y - DETECT_PLAYER_BOUNDARY < y) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Perform an interaction on an item. This is declared final because ALL interaction logic should be performed here
     *
     * @param interaction
     */
    public final void performAction(Interaction interaction) {
        switch (interaction) {
            case PICK_UP:
                draw = false;
                break;
            case OPEN:
                draw = false;
                break;
            case CLOSE:
                draw = true;
                break;
            case SIT:
                System.out.println("sit down");
                break;
            case TAKE:
                draw = false;
                break;
            case ACCESS:
                break;
            case UNLOCK:
                break;
        }
    }

    /**
     * returns the onScreen x of the item
     * @return
     */
    public double getX() {
        return x;
    }

    /**
     * returns the onscreen y of the item
     * @return
     */
    public double getY() {
        return y;
    }

    /**
     * returns the on screen z of the item
     * @return
     */
    public double getZ() {
        return z;
    }

    /**
     * returns the on screen width of the item
     * @return
     */
    public double getWidth() {
        return width;
    }

    /**
     * sets the on screen width of the item
     * @param width
     */
    public void setWidth(double width) {
        this.width = width;
    }

    /**
     * gets the onscreen length of the item
     * @return
     */
    public double getLength() {
        return length;
    }

    /**
     * sets the onscreen length of the item
     * @param length
     */
    public void setLength(double length) {
        this.length = length;
    }

/**
 * gets the on screen height of the item
 * @return
 */
    public double getHeight() {
        return height;
    }

    /**
     * sets the onscreen height of the item
     * @param height
     */
    public void setHeight(double height) {
        this.height = height;
    }

    /**
     * gets the onscreen color of the item
     * @return
     */
    public Color getColor() {
        return color;
    }

    /**
     * sets the onscreen color of the item
     * @param color
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * gets the onscreen cuibes of the item ; this is useful for rendering the item as iotems are rendered in a lsit of cubges
     * @return
     */
    public java.util.List<Cube> getCubes() {
        return cubes;
    }

    /**
     * sets the onscreen cubes for the item
     * @param cubes
     */
    public void setCubes(java.util.List<Cube> cubes) {
        this.cubes = cubes;
    }

    /**
     * sets the onscreen x positio0n of the item
     * @param x
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * sets the onscreen y positio0n of the item
     * @param y
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * sets the onscreen z positio0n of the item
     * @param z
     */

    public void setZ(double z) {
        this.z = z;
    }

    /**
     * changes the drawability of the item; whether it is drawn onscreen or not
     */
    public void canDraw() {
        draw = !draw;
    }

    /**
     * retursn whether the item is being drawn onscreen or not
     * @return
     */
    public boolean isDraw() {
        return draw;
    }
    /**
     * sets the drawability
     * @param draw
     */

    public void setDraw(boolean draw) {
        this.draw = draw;
    }

    /**
     * returns the class of the item + it's id; useful for finding out which item is necessaRY TO CONTINUE
     * @return
     */
    public String getName() {
        return getClass().getSimpleName() + " " + getItemID();
    }

    public List<Interaction> getInteractionsAvailable() {
        return interactionsAvailable;
    }


    /**
     * These are all the interactions that can be performed by items.
     * Not all items can perform all actions. What action an item can perform is determined by whats added to the
     * interactionsAvaliable field.
     *
     * @see Item.addInteractions()
     */
    public enum Interaction {
        OPEN, CLOSE, PICK_UP, SIT, TAKE, DROP, ACCESS, HACK, UNLOCK
    }

    /**
     * returns whether the item can be picked up or not -- furniture cannot
     * @return
     */
    public boolean isCanPickup() {
        return canPickup;
    }
}

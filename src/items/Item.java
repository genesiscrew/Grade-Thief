package items;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import gui.Drawable;


/**
 * @Author Adam Wareing
 */
public abstract class Item extends GameObject implements Drawable {

    public final double DETECT_PLAYER_BOUNDARY = 20;

    public int itemID;
    protected double x;
    protected double y;
    protected double z;
    protected double width;
    protected double length;
    protected double height;
    protected Color color;
    protected List<Interaction> interactionsAvaliable;
    protected boolean draw = true;

    public Item(int itemID, String itemType, double x, double y, double z, double width, double length, double height, Color c) {
        super(itemID, itemType);
        this.x = x;
        this.y = y;
        this.z = z;
        this.width = width;
        this.length = length;
        this.height = height;
        this.color = c;
        addInteractions();
    }

    public void addInteractions() {
        interactionsAvaliable = new ArrayList<>();
        interactionsAvaliable.add(Interaction.OPEN);
        interactionsAvaliable.add(Interaction.CLOSE);
    }

    public boolean pointNearObject(double x, double y, double z) {
        if ((this.x + DETECT_PLAYER_BOUNDARY + this.width) > x && (this.y + DETECT_PLAYER_BOUNDARY + this.length) > y
                && this.x - DETECT_PLAYER_BOUNDARY< x && this.y - DETECT_PLAYER_BOUNDARY< y){
        return true;
        }else{
            return false;
        }
    }

    public void performAction(Interaction interaction){
        switch (interaction){
            case PICK_UP:
                draw = false;
                break;
            case OPEN:
                draw = false;
                break;
            case CLOSE:
                draw = true;
                break;
        }
    }

    // Model file
    public enum Interaction {
        OPEN,
        UNLOCK,
        CLOSE, PICK_UP
    }

    public List<Interaction> getInteractionsAvaliable() {
        return interactionsAvaliable;
    }
}

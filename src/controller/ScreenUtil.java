package controller;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by wareinadam on 5/10/16.
 */
public class ScreenUtil {


    public final double AIM_SIGHT = 4; // Changes the size of the center-cross.

    // Rotation speed, the lower the value, the faster the camera rotation
    public final double HORIZONTAL_ROT_SPEED = 900;
    public final double VERTICAL_ROT_SPEED = 2200;

    private double verticalLook = -0.9;  // Goes from 0.999 to -0.999, minus being looking down and + looking up
    private double horizontalLook = 0;     // takes any number and goes round in radians

    /**
     * This hides the mouse cursor so we can use the cross hairs instead
     */
    public Cursor invisibleMouse() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        BufferedImage cursorImage = new BufferedImage(1, 1, BufferedImage.TRANSLUCENT);
        return toolkit.createCustomCursor(cursorImage, new Point(0, 0), "InvisibleCursor");
    }

    /**
     * This aims the mouse on the graphics
     *
     * @param g
     */
    public void drawMouseAim(Graphics g) {
        g.setColor(Color.black);
        g.drawLine((int) (Main.ScreenSize.getWidth() / 2 - AIM_SIGHT), (int) (Main.ScreenSize.getHeight() / 2),
                (int) (Main.ScreenSize.getWidth() / 2 + AIM_SIGHT), (int) (Main.ScreenSize.getHeight() / 2));
        g.drawLine((int) (Main.ScreenSize.getWidth() / 2), (int) (Main.ScreenSize.getHeight() / 2 - AIM_SIGHT),
                (int) (Main.ScreenSize.getWidth() / 2), (int) (Main.ScreenSize.getHeight() / 2 + AIM_SIGHT));
    }

    /**
     * Called when the mouse is moved, calculates the amount it was moved and sets the vert and horizontal looking angles
     * It also updates the view
     *
     * @param NewMouseX
     * @param NewMouseY
     */
    public void mouseMovement(double NewMouseX, double NewMouseY) {
        double difX = (NewMouseX - Main.ScreenSize.getWidth() / 2);
        double difY = (NewMouseY - Main.ScreenSize.getHeight() / 2);
        difY *= 6 - Math.abs(verticalLook) * 5;
        verticalLook -= difY / VERTICAL_ROT_SPEED;
        horizontalLook += difX / HORIZONTAL_ROT_SPEED;

        if (verticalLook > 0.999)
            verticalLook = 0.999;

        if (verticalLook < -0.999)
            verticalLook = -0.999;
    }

    public double getVerticalLook() {
        return verticalLook;
    }

    public double getHorizontalLook() {
        return horizontalLook;
    }

}

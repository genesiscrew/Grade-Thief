package gui;

import gui.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wareinadam on 22/09/16.
 */
public interface Drawable {

    void setRotAdd();

    /**
     * Update the direction the polygon is facing
     *
     * @param toX
     * @param toY
     */
    void updateDirection(double toX, double toY);

    /**
     *
     */
    void updatePoly();

    /**
     * Remove the cube
     */
    void removeCube();

}

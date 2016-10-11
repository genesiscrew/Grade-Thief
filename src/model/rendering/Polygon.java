package model.rendering;

import java.awt.Color;
import java.awt.Graphics;

import controller.Main;
import view.Calculator;
import view.Screen;

/**
 * @Author Adam Wareing
 * This is used to store all the x,y,z points of a single polygon.
 *
 */
public class Polygon {
	public double[] x, y, z;
	boolean draw = true;
	public boolean seeThrough = false;
	public double[] calcPos, newX, newY;
	public double averageDistance; // average distance to the camera
	java.awt.Polygon polygon; // the polygon we use to draw it on screen
	Color color;
	boolean visible = true;
	double lighting = 1;

	public Polygon(double[] x, double[] y, double[] z, Color c, boolean seeThrough) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.color = c;
		this.seeThrough = seeThrough;
		createPolygon();
	}

	/**
	 * Create a java.awt polygon and add all the x and y points to it
	 */
	void createPolygon() {
		polygon = new java.awt.Polygon();
		for (int i = 0; i < x.length; i++) {
			polygon.addPoint((int) x[i], (int) y[i]);
		}

	}

	/**
	 * Update the polygon for the light direction and where the player is viewing from
	 *
	 * @param screen
	 */
	public void updatePolygon(double[] lightDirection, double[] viewFrom) {
		newX = new double[x.length];
		newY = new double[x.length];
		draw = true;
		for (int i = 0; i < x.length; i++) {
			// Cal
			calcPos = Calculator.calculateProjectionFromPosition(viewFrom, x[i], y[i], z[i]);
			newX[i] = (Main.ScreenSize.getWidth() / 2 - Calculator.calculatorFocusPosition[0])
					+ calcPos[0] * Screen.zoom;
			newY[i] = (Main.ScreenSize.getHeight() / 2 - Calculator.calculatorFocusPosition[1])
					+ calcPos[1] * Screen.zoom;
			// If its behind the camera don't draw it
			if (Calculator.t < 0)
				draw = false;
		}

		calcLighting(lightDirection);
		updateAWTPolygonPoints(newX, newY);
		averageDistance = getDist(viewFrom);
	}

	/**
	 * Calculate the lighting for the polygon based on the current light source
	 * @param - lightDir [x,y,z] position of the sun
	 */
	public void calcLighting(double[] lightDir) {
		Plane lightingPlane = new Plane(this);
		double angle = Math.acos(((lightingPlane.normalVector.x * lightDir[0])
				+ (lightingPlane.normalVector.y * lightDir[1]) + (lightingPlane.normalVector.z * lightDir[2]))
				/ (Math.sqrt(lightDir[0] * lightDir[0] + lightDir[1] * lightDir[1] + lightDir[2] * lightDir[2])));

		lighting = 0.2 + 1 - Math.sqrt(Math.toDegrees(angle) / 180);

		if (lighting > 1)
			lighting = 1;
		if (lighting < 0)
			lighting = 0;
	}

	/**
	 * Moves the player the specified amount. 0 means no change will be made.
	 *
	 * @param dx
	 * @param dy
	 * @param dz
	 */
	public void updatePosition(double dx, double dy, double dz) {
		for (int i = 0; i < x.length; i++) {
			this.x[i] += dx;
			this.y[i] += dy;
			this.z[i] += dz;
		}
	}

	/**
	 * Set the position of the player
	 * @param dx
	 * @param dy
	 * @param dz
	 */
	public void updatePosition2(double dx, double dy, double dz) {
		for (int i = 0; i < x.length; i++) {
			this.x[i] = dx;
			this.y[i] = dy;
			this.z[i] = dz;
		}
	}

	/**
	 * Calculates the average distance to the viewer
	 *
	 * @return
	 */
	double getDist(double[] viewFrom) {
		double total = 0;
		for (int i = 0; i < x.length; i++)
			total += GetDistanceToP(i, viewFrom);
		return total / x.length;
	}

	double GetDistanceToP(int i, double[] viewFrom) {
		return Math.sqrt((viewFrom[0] - x[i]) * (viewFrom[0] - x[i]) + (viewFrom[1] - y[i]) * (viewFrom[1] - y[i])
				+ (viewFrom[2] - z[i]) * (viewFrom[2] - z[i]));
	}

	/**
	 * Update the polygons x and y points
	 *
	 * @param x
	 * @param y
	 */
	void updateAWTPolygonPoints(double[] x, double[] y) {
		polygon.reset();
		for (int i = 0; i < x.length; i++) {
			polygon.xpoints[i] = (int) x[i];
			polygon.ypoints[i] = (int) y[i];
			polygon.npoints = x.length;
		}
	}

	/**
	 * Draw the polygon onto the canvas
	 *
	 * @param g
	 *
	 * @param guard
	 * @param timer
	 * @param Y
	 * @param X
	 *
	 */
	public void drawPolygon(Graphics g) {
		if (draw && visible) {
			g.setColor(new Color((int) (color.getRed() * lighting), (int) (color.getGreen() * lighting),
					(int) (color.getBlue() * lighting)));
			if (seeThrough)
				g.drawPolygon(polygon);
			else
				g.fillPolygon(polygon);
			if (Screen.drawOutlines) {
				g.setColor(new Color(0, 0, 0));
				g.drawPolygon(polygon);
			}

			if (Screen.polygonOver == this) {
				g.setColor(new Color(255, 255, 255, 100));
				g.fillPolygon(polygon);
			}
		}

	}


	/**
	 * Is the mouse over the currently selected polygon?
	 *
	 * @return
	 */
	public boolean mouseOver() {
		return polygon.contains(Main.ScreenSize.getWidth() / 2, Main.ScreenSize.getHeight() / 2);
	}

	/**
	 * creates a map on the screen for the guard only. it only appears when the
	 * player is detected by a guard and is only shown for a certain amount of
	 * time.
	 *
	 * @param g
	 * @param guard
	 *            : a boolean value to specify whether the player is guardd or
	 *            not
	 * @param timer
	 *            : timer to determine the period the map will appear for
	 * @param X
	 *            : x coordinate of other player, this is needed to determine
	 *            where to draw the red circle
	 * @param Y
	 *            : y coordinate of other player, this is needed to determine
	 *            where to draw the red circle
	 */
	public void drawMap(Graphics g, boolean guard, int timer, double X, double Y, double X1, double Y1) {

		int xOffset = Screen.screenSize.width-500;
		int yOffset = 50;
       if (timer > 0) {
		if (guard) {
		for (int i = 0; i < x.length; i++) {
			String s = "*";
			double xoffset = x[i] -  X1;
			double yoffset = y[i] -  Y1;
			if ((int) x[i] - (int) X == 0 && (int) y[i] - (int) Y == 0) {
				g.setColor(Color.blue);
				s = "\u25CF";
				 g.drawString(s, xOffset + (int) x[i] / 2, yOffset + (int) y[i] / 2);
				//g.drawRect(xOffset + (int) x[i] / 2, yOffset +  (int) y[i] / 2, 5, 5);


			}
			else if (xoffset >= 0 && xoffset <= 10 && yoffset >= 0 && yoffset <= 10) {
				g.setColor(Color.green);
				s = "\u25CF";
				g.drawString(s, xOffset + (int) x[i] / 2, yOffset +  (int) y[i] / 2);
				//g.drawRect(xOffset + (int) x[i] / 2, yOffset +  (int) y[i] / 2, 5, 5);


			} else {
				g.setColor(Color.black);
				//g.drawString(s, xOffset + (int) x[i] / 2, yOffset + (int) y[i] / 2);
				g.drawRect(xOffset + (int) x[i] / 2, yOffset +  (int) y[i] / 2, 5, 5);

			}
		}
	}

	 }

	}
}

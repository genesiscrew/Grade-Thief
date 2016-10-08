package gui;

import java.awt.*;

public class Polygon {
	public double[] x, y, z;
	boolean draw = true;
	boolean seeThrough = false;
	double[] calcPos, newX, newY;
	double averageDistance; // average distance to the camera
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
	 * Update the polygon for
	 *
	 * @param screen
	 */
	void updatePolygon(double[] lightDirection, double[] viewFrom) {
		newX = new double[x.length];
		newY = new double[x.length];
		draw = true;
		for (int i = 0; i < x.length; i++) {
			calcPos = Calculator.calculatePositionP(viewFrom, x[i], y[i], z[i]);
			newX[i] = (Main.ScreenSize.getWidth() / 2 - Calculator.calculatorFocusPosition[0])
					+ calcPos[0] * Screen.zoom;
			newY[i] = (Main.ScreenSize.getHeight() / 2 - Calculator.calculatorFocusPosition[1])
					+ calcPos[1] * Screen.zoom;
			if (Calculator.t < 0)
				draw = false;
		}

		calcLighting(lightDirection);
		updateAWTPolygonPoints(newX, newY);
		averageDistance = getDist(viewFrom);
	}

	void calcLighting(double[] lightDir) {
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
	 */
	void drawPolygon(Graphics g, boolean guard, int timer) {
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
		// creates a map on the screen for the guard only
		System.out.println("timer is now:" + timer);
		if (timer > 0) {


			if (guard) {
				for (int i = 0; i < x.length; i++) {
					String s = "*";
					//System.out.println("timer is now: " + timer);

					g.drawString(s, (int) x[i] / 2, (int) y[i] / 2);
				}
			}

		}
	}
	/*
	 * public static void wrapTextToPolygon(Graphics g, String text, Font font,
	 * Color color, java.awt.Polygon shape, int x, int y, int border) {
	 * FontMetrics m = g.getFontMetrics(font); java.awt.Shape poly = shape; int
	 * num = 0; String[] words = new String[1]; if(text.contains(" ")) { words =
	 * text.split(" "); } else words[0] = text; int yi = m.getHeight() + border;
	 * num = 0; while(num != words.length) { String word = words[num]; Rectangle
	 * rect = new Rectangle((poly.getBounds().width / 2) - (m.stringWidth(word)
	 * / 2) + x - border - 1, y + yi, m.stringWidth(word) + (border * 2) + 2,
	 * m.getHeight()); while(!poly.contains(rect)) { yi += m.getHeight(); rect.y
	 * = y + yi; if(yi >= poly.getBounds().height) break; } int i = 1;
	 * while(true) { if(words.length < num + i + 1) { num += i - 1; break; }
	 * rect.width += m.stringWidth(words[num + i]) + (border * 2); rect.x -=
	 * m.stringWidth(words[num + i]) / 2 - border; if(poly.contains(rect)) {
	 * word += " " + words[num + i]; } else { num += i - 1; break; } i = i + 1;
	 * } if(yi < poly.getBounds().height) { g.drawString(word,
	 * (poly.getBounds().width / 2) - (m.stringWidth(word) / 2) + x, y + yi); }
	 * else { break; } yi += m.getHeight(); num += 1; } }
	 */

	/**
	 * Is the mouse over the currently selected polygon?
	 *
	 * @return
	 */
	public boolean mouseOver() {
		return polygon.contains(Main.ScreenSize.getWidth() / 2, Main.ScreenSize.getHeight() / 2);
	}
}

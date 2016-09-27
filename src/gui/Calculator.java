package gui;

/**
 * This performs calculations for the 3D graphics using vectors
 */
public class Calculator {
	static double t = 0;
	static Vector W1, W2, ViewVector, RotationVector, DirectionVector, PlaneVector1, PlaneVector2;
	static Plane plane;
	static double[] calculatorFocusPosition = new double[2];

	static double[] CalculatePositionP(double[] ViewFrom, double x, double y, double z) {
		double[] projP = getProjection(ViewFrom, x, y, z, plane);
		double[] drawP = getDrawP(projP[0], projP[1], projP[2]);
		return drawP;
	}

	/**
	 * Get the vector the player is looking from to the
	 *
	 * @param viewFrom
	 * @param x
	 * @param y
	 * @param z
     * @param plane
     * @return
     */
	static double[] getProjection(double[] viewFrom, double x, double y, double z, Plane plane) {
		Vector ViewToPoint = new Vector(x - viewFrom[0], y - viewFrom[1], z - viewFrom[2]);
		double[] viewTo = plane.getViewTo();

		t = (plane.normalVector.x*viewTo[0] + plane.normalVector.y*viewTo[1] +  plane.normalVector.z*viewTo[2]
				- (plane.normalVector.x*viewFrom[0] + plane.normalVector.y*viewFrom[1] + plane.normalVector.z*viewFrom[2]))
				/ (plane.normalVector.x*ViewToPoint.x + plane.normalVector.y*ViewToPoint.y + plane.normalVector.z*ViewToPoint.z);

		x = viewFrom[0] + ViewToPoint.x * t;
		y = viewFrom[1] + ViewToPoint.y * t;
		z = viewFrom[2] + ViewToPoint.z * t;

		return new double[] {x, y, z};
	}

	/**
	 *
	 * @param x
	 * @param y
	 * @param z
     * @return
     */
	static double[] getDrawP(double x, double y, double z) {
		double DrawX = W2.x * x + W2.y * y + W2.z * z;
		double DrawY = W1.x * x + W1.y * y + W1.z * z;
		return new double[]{DrawX, DrawY};
	}

	/**
	 *
	 * @param ViewFrom
	 * @param ViewTo
     * @return
     */
	static Vector getRotationVector(double[] ViewFrom, double[] ViewTo) {
		double dx = Math.abs(ViewFrom[0]-ViewTo[0]);
		double dy = Math.abs(ViewFrom[1]-ViewTo[1]);
		double xRot, yRot;
		xRot=dy/(dx+dy);
		yRot=dx/(dx+dy);

		if(ViewFrom[1]>ViewTo[1])
			xRot = -xRot;
		if(ViewFrom[0]<ViewTo[0])
			yRot = -yRot;

		Vector V = new Vector(xRot, yRot, 0);
		return V;
	}

	/**
	 * Set the information from screen for the current view from and view to. This also adjusts the plane, rotation vector
	 * and zoom levels
	 * @param screen
	 */
	static void setPredeterminedInfo(Screen screen) {
		ViewVector = new Vector(screen.ViewTo[0] - screen.ViewFrom[0], screen.ViewTo[1] - screen.ViewFrom[1], screen.ViewTo[2] - screen.ViewFrom[2]);
		DirectionVector = new Vector(1, 1, 1);
		PlaneVector1 = ViewVector.CrossProduct(DirectionVector);
		PlaneVector2 = ViewVector.CrossProduct(PlaneVector1);
		plane = new Plane(PlaneVector1, PlaneVector2, screen.ViewTo);

		RotationVector = Calculator.getRotationVector(screen.ViewFrom, screen.ViewTo);
		W1 = ViewVector.CrossProduct(RotationVector);
		W2 = ViewVector.CrossProduct(W1);

		calculatorFocusPosition = Calculator.CalculatePositionP(screen.ViewFrom, screen.ViewTo[0], screen.ViewTo[1], screen.ViewTo[2]);
		calculatorFocusPosition[0] = Screen.zoom * calculatorFocusPosition[0];
		calculatorFocusPosition[1] = Screen.zoom * calculatorFocusPosition[1];
	}
}

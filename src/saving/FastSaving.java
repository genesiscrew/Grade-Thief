package saving;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import gui.GameController;

public class FastSaving {

	PrintStream out;

	public FastSaving(String fileName) throws FileNotFoundException {
		out = new PrintStream(new File(fileName));
	}

	public void save() {
		double[] tmp = GameController.getPlayerPosition();
		double x = tmp[0];
		double y = tmp[1];
		double z = tmp[2];

		out.println(x + "," + y + "," + z);

		out.println("Yellow" + "," + "100" + "," + "1000" + "," + "1000" + "," + "Drawable" + "," + "1000" + "," + "50" + ","
				+ "50" + "," + "5");

		out.println("====================================================================");

	}

}

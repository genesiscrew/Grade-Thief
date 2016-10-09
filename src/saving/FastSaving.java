package saving;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.List;

import gui.GameController;
import items.Item;

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

		List<Item> items = GameController.getPlayer().getCurrentPlayer().getInventory();

		out.println(x + "," + y + "," + z);

		for (int i = 0; i < items.size(); i++) {

			// Item Color
			int red = items.get(i).getColor().getRed();
			int green = items.get(i).getColor().getGreen();
			int blue = items.get(i).getColor().getBlue();

			double itemHeight = items.get(i).getHeight();
			double itemWidth = items.get(i).getWidth();
			double length = items.get(i).getLength();

			double X = items.get(i).getX();
			double Y = items.get(i).getY();
			double Z = items.get(i).getZ();

			int itemID = items.get(i).getItemID();
			String itemType = items.get(i).getItemType();


			out.println(red + "," + green + "," + blue + "," + itemHeight + "," + itemID + "," + length + ","
					+ itemType + "," + itemWidth + "," + X + "," + Y + "," + Z);

		}

		/*
		 *
		 * <item itemColor="NULL" itemHeight="NULL" itemID="NULL"
		 * itemLength="NULL" itemType="NULL" itemWidth="NULL" itemX="NULL"
		 * itemY="NULL" itemZ="NULL" />
		 *
		 * out.println("Yellow" + "," + "100" + "," + "1000" + "," + "1000" +
		 * "," + "Drawable" + "," + "1000" + "," + "50" + "," + "50" + "," +
		 * "5");
		 */

		out.println("====================================================================");

	}

}

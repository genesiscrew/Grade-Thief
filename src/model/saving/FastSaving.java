package model.saving;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.List;

import model.items.Item;
import view.GameController;

/**
 *
 * This class is responsible for saving the game and player. It gets the name of
 * the file that user wants to save the game into it and then, if the file was
 * there it will re-write the file and if the file is not there it will create
 * new file with that name. This class had been encapsulated via private fields,
 * setters and getters. This class has better performance and functionality than
 * SaveGame which use XML.
 *
 *
 * @author Mansour Javaher
 * @see SaveGame
 * @see LoadGame
 * @see FastSaving
 */
public class FastSaving {

	/**
	 * Creating an printStream for writing to file
	 */
	PrintStream out;

	/**
	 * getting the file name from user in order to save save the game and
	 * gameworld
	 *
	 * @param fileName
	 * @throws FileNotFoundException
	 */
	public FastSaving(String fileName) throws FileNotFoundException {
		out = new PrintStream(new File(fileName));
	}

	public void save() {
		double[] tmp = GameController.getPlayerPosition();
		double x = tmp[0];
		double y = tmp[1];
		double z = tmp[2];

		// getting the items from player inventory for saving purpose
		List<Item> items = GameController.getPlayer().getCurrentPlayer().getInventory();

		// Writing the player position
		out.println(x + "," + y + "," + z);

		for (int i = 0; i < items.size(); i++) {

			// Saving the Items attribute for loading later.
			/*
			 * itemColor itemHeight itemID itemLength itemType
			 * itemWidth itemX itemY itemZ
			 *
			 */

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

			//writing the items attributes
			out.println(red + "," + green + "," + blue + "," + itemHeight + "," + itemID + "," + length + "," + itemType
					+ "," + itemWidth + "," + X + "," + Y + "," + Z);

		}
		//This line should be here for saving puprose because in loading = means that this is the end of the saved
		//Version of the file
		out.println("====================================================================");

	}

}

package model.saving;

import java.awt.Color;

import java.io.File;
import java.util.List;
import java.util.Scanner;

import javax.swing.JOptionPane;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import model.items.Chair;
import model.items.Dog;
import model.items.Item;
import model.items.KeyDraw;
import model.items.Laptop;
import model.items.MapDraw;
import model.items.Marker;
import model.items.MetalSheet;
import model.items.Table;
import view.GameController;
import view.Screen;

/**
 *
 * This class is responsible for loading the game and game files. It gets and
 * file and try to parse the file in order to load the game. This class had been
 * encapsulated via private fields, setters and getters. This class has better
 * performance and functionality than LoadGame.
 *
 *
 * @author Mansour Javaher
 * @see SaveGame
 * @see LoadGame
 * @see FastSaving
 */
public class FastLoad {

	String myFile;
	Screen sc;

	/**
	 * Constructor which gets the fileName and a screen object in order to pop
	 * up the error message if there were any problem.
	 *
	 * @param myFile
	 * @param sc
	 */
	public FastLoad(String myFile, Screen sc) {
		super();

		// Precondition of using the Loading ability,
		// The fileName and sc shouldn't be null.
		if (myFile == null || sc == null)
			JOptionPane.showMessageDialog(sc, "Please Select a file in order to Load.", "Grade-Thief Loading",
					JOptionPane.ERROR_MESSAGE);

		this.myFile = myFile;
		this.sc = sc;
	}

	/**
	 * Loading Function This function use the instance fields when the object
	 * gets created in order to load the game file and game. It load the file
	 * based on the file name and wrap a scanner around the file for parsing the
	 * file. Then it setts the values and object for game. It will check if the
	 * object was in the game world to remove it because that Item had been
	 * saved in different position or inventory.
	 */
	public void load() {

		Scanner scan;
		try {

			// Creating Scanner
			scan = new Scanner(new File(myFile));

			int i = 0;

			// Getting Player Inventory Reference in order to Load the Item in
			// player inventory
			List<Item> items = GameController.getPlayer().getCurrentPlayer().getInventory();

			while (scan.hasNextLine()) {
				String line = scan.nextLine();

				/*
				 * All the Print out lines are the TESTING and DEBUGGING purpose
				 * code.
				 */

				if (i == 0) {

					System.out.println("================== New Player: ================== ");
					// Parsing a line of well formatted into a single array.
					String parsedLine[] = line.split(",");

					System.out.println(" X= " + parsedLine[0] + " Y= " + parsedLine[1] + " Z= " + parsedLine[2]);

					/*
					 * Setting Saved Position for player.
					 */
					double x = Double.parseDouble(parsedLine[0]);
					double y = Double.parseDouble(parsedLine[1]);
					double z = Double.parseDouble(parsedLine[2]);

					GameController.getPlayer().setX(x);
					GameController.getPlayer().setY(y);

					i++;
				} else if (line.startsWith("=")) {
					i = 0;
					continue;
				} else if (line.startsWith("EOF")) {
					break;
				} else {

					System.out.println("======= Item: ========");
					String itemTokens[] = line.split(",");
					// getting the red attribute of the item.
					System.out.println(" itemRed :" + itemTokens[0]);
					int ItemRed = Integer.parseInt(itemTokens[0]);

					// getting the green attribute of the item.
					System.out.println(" itemGreen :" + itemTokens[1]);
					int ItemGreen = Integer.parseInt(itemTokens[1]);

					// getting the blue attribute of the item.
					System.out.println(" itemBlue :" + itemTokens[2]);
					int ItemBlue = Integer.parseInt(itemTokens[2]);

					// getting the height attribute of the item.
					System.out.println(" itemHeight :" + itemTokens[3]);
					double ItemHeight = Double.parseDouble(itemTokens[3]);

					// getting the itemID attribute of the item.
					System.out.println(" itemID :" + itemTokens[4]);
					int ItemID = Integer.parseInt(itemTokens[4]);

					// getting the length attribute of the item.
					System.out.println(" itemLength :" + itemTokens[5]);
					double ItemLength = Double.parseDouble(itemTokens[5]);

					// getting the type attribute of the item.
					System.out.println(" itemType :" + itemTokens[6]);
					String ItemType = itemTokens[6];

					// getting the width attribute of the item.
					System.out.println(" itemWidth :" + itemTokens[7]);
					double ItemWidth = Double.parseDouble(itemTokens[7]);

					// getting the X attribute of the item.
					System.out.println(" itemX :" + itemTokens[8]);
					double ItemX = Double.parseDouble(itemTokens[8]);

					// getting the Y attribute of the item.
					System.out.println(" itemY :" + itemTokens[9]);
					double ItemY = Double.parseDouble(itemTokens[9]);

					// getting the Z attribute of the item.
					System.out.println(" itemZ :" + itemTokens[10]);
					double ItemZ = Double.parseDouble(itemTokens[10]);

					// Generating and preparing the Color for Item.
					Color itemColor = new Color(ItemRed, ItemGreen, ItemBlue);

					/*
					 * Checking and preparing Item Object for player
					 */
					switch (ItemType) {

					case "Chair":
						Chair tmp = new Chair(ItemID, ItemType, ItemX, ItemY, ItemZ, ItemWidth, ItemLength, ItemHeight,
								itemColor);
						items.add(tmp);
						break;

					case "Container":
						Chair tmp1 = new Chair(ItemID, ItemType, ItemX, ItemY, ItemZ, ItemWidth, ItemLength, ItemHeight,
								itemColor);
						items.add(tmp1);
						break;

					case "Dog":
						Dog tmp2 = new Dog(ItemID, ItemType, ItemX, ItemY, ItemZ, ItemWidth, ItemLength, ItemHeight,
								itemColor);
						items.add(tmp2);
						break;

					case "KeyDraw":
						KeyDraw tmp3 = new KeyDraw(ItemID, ItemType, ItemX, ItemY, ItemZ, ItemWidth, ItemLength,
								ItemHeight, itemColor);
						items.add(tmp3);
						break;

					case "Keys":
						/*
						 * Currently Game doesn't have Keys
						 */
						break;
					case "Laptop":
						Laptop tmp5 = new Laptop(ItemID, ItemType, ItemX, ItemY, ItemZ, ItemWidth, ItemLength,
								ItemHeight, itemColor);
						items.add(tmp5);
						break;

					case "Map":
						/*
						 * There is not Pickable Map
						 */
						break;

					case "MapDraw":
						MapDraw tmp7 = new MapDraw(ItemID, ItemType, ItemX, ItemY, ItemZ, ItemWidth, ItemLength,
								ItemHeight, itemColor);
						items.add(tmp7);
						break;

					case "Marker":
						Marker tmp8 = new Marker(ItemID, ItemType, ItemX, ItemY, ItemZ, ItemWidth, ItemLength,
								ItemHeight, itemColor);
						items.add(tmp8);
						break;

					case "MetalSheet":
						MetalSheet tmp9 = new MetalSheet(ItemID, ItemType, ItemX, ItemY, ItemZ, ItemWidth, ItemLength,
								ItemHeight, itemColor);
						items.add(tmp9);
						break;

					case "Table":
						Table tmp10 = new Table(ItemID, ItemType, ItemX, ItemY, ItemZ, ItemWidth, ItemLength,
								ItemHeight, itemColor);
						items.add(tmp10);
						break;

					default:
						break;
					}

				}

			}

			// checking the existing objects and items in the gameWorld
			List<Item> roomObj = sc.getRoom().getRoomObjects();
			for (int j = 0; j < items.size(); j++) {
				// Checking if there is an existing item in the saved file
				// and that item exist in the game world
				for (int j2 = 0; j2 < roomObj.size(); j2++) {
					if (roomObj.get(j2).getItemID() == items.get(j).getItemID()) {
						// at this point "j2" exist in the player inventory and
						// we need to remove it from gameWorld
						roomObj.remove(j2);
					}
				}

				// Addint and moving the Item from player inventory to Room
				double[] viewFrom = sc.getViewFrom();
				items.get(j).moveItemBy(viewFrom[0] - items.get(j).getX(), viewFrom[1] - items.get(j).getY(), 0);
				sc.getRoom().addItemToRoom(items.get(j));
				items.get(j).canDraw();

			}
			System.out.println("-----------End of Loading--------------");

		} catch (Exception e) {
			// custom title, error icon for uncompatable chema of lodaed file.
			JOptionPane.showMessageDialog(sc,
					"Please select saved version of the Grade Thief Game, This file is not belong to Grade-Thief",
					"Grade-Thief Loading", JOptionPane.ERROR_MESSAGE);
		}

	}
}

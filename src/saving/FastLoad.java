package saving;

import java.awt.Color;
import java.io.File;
import java.util.List;
import java.util.Scanner;

import javax.swing.JOptionPane;

import gui.GameController;
import gui.Screen;
import items.Chair;
import items.Dog;
import items.Item;
import items.KeyDraw;
import items.Laptop;
import items.MapDraw;
import items.Marker;
import items.MetalSheet;
import items.Table;

public class FastLoad {

	String myFile;
	Screen sc;

	public FastLoad(String myFile, Screen sc) {
		super();
		this.myFile = myFile;
		this.sc = sc;
	}

	public void load() {

		Scanner scan;
		try {
			scan = new Scanner(new File(myFile));

			int i = 0;

			List<Item> items = GameController.getPlayer().getCurrentPlayer().getInventory();

			while (scan.hasNextLine()) {
				String line = scan.nextLine();

				if (i == 0) {
					System.out.println("================== New Player: ================== ");
					String l[] = line.split(",");
					System.out.println(" X= " + l[0] + " Y= " + l[1] + " Z= " + l[2]);

					double x = Double.parseDouble(l[0]);
					double y = Double.parseDouble(l[1]);
					double z = Double.parseDouble(l[2]);

					GameController.getPlayer().setX(x);
					GameController.getPlayer().setY(y);

					// GameController.getPlayer().setViewFrom(new double[] {
					// 100, 100, 100 });
					i++;
				} else if (line.startsWith("=")) {
					i = 0;
					continue;
				} else if (line.startsWith("EOF")) {
					break;
				} else {

					System.out.println("======= Item: ========");
					String itemTokens[] = line.split(",");

					// <item itemColor="NULL" itemHeight="NULL" itemID="NULL"
					// itemLength="NULL" itemType="NULL" itemWidth="NULL"
					// itemX="NULL" itemY="NULL" itemZ="NULL" />

					System.out.println(" itemRed :" + itemTokens[0]);
					int ItemRed = Integer.parseInt(itemTokens[0]);

					System.out.println(" itemGreen :" + itemTokens[1]);
					int ItemGreen = Integer.parseInt(itemTokens[1]);

					System.out.println(" itemBlue :" + itemTokens[2]);
					int ItemBlue = Integer.parseInt(itemTokens[2]);

					System.out.println(" itemHeight :" + itemTokens[3]);
					double ItemHeight = Double.parseDouble(itemTokens[3]);

					System.out.println(" itemID :" + itemTokens[4]);
					int ItemID = Integer.parseInt(itemTokens[4]);

					System.out.println(" itemLength :" + itemTokens[5]);
					double ItemLength = Double.parseDouble(itemTokens[5]);

					System.out.println(" itemType :" + itemTokens[6]);
					String ItemType = itemTokens[6];

					System.out.println(" itemWidth :" + itemTokens[7]);
					double ItemWidth = Double.parseDouble(itemTokens[7]);

					System.out.println(" itemX :" + itemTokens[8]);
					double ItemX = Double.parseDouble(itemTokens[8]);

					System.out.println(" itemY :" + itemTokens[9]);
					double ItemY = Double.parseDouble(itemTokens[9]);

					System.out.println(" itemZ :" + itemTokens[10]);
					double ItemZ = Double.parseDouble(itemTokens[10]);

					/*
					 * (int itemID, String itemType, double x, double y, double
					 * z, double width, double length, double height, Color c)
					 */

					Color itemColor = new Color(ItemRed, ItemGreen, ItemBlue);

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
						 * Keys tmp4 = new Keys(ItemID, ItemType, ItemX, ItemY,
						 * ItemZ, ItemWidth, ItemLength, ItemHeight, itemColor);
						 */
						break;
					case "Laptop":
						Laptop tmp5 = new Laptop(ItemID, ItemType, ItemX, ItemY, ItemZ, ItemWidth, ItemLength,
								ItemHeight, itemColor);
						items.add(tmp5);
						break;
					case "Map":
						/*
						 * Map tmp6 = new Map(ItemID, ItemType, ItemX, ItemY,
						 * ItemZ, ItemWidth, ItemLength, ItemHeight, itemColor);
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

					/*
					 * out.println(red + ", " + green + ", " + blue + ", " +
					 * itemHeight + ", " + itemID + ", " + length + ", " +
					 * itemType + ", " + itemWidth + ", " + X + ", " + Y + ", "
					 * + Z);
					 */
				}

			}

			double[] pos = GameController.getPlayer().getViewFrom();

			System.out.println("-----------nd of input.txt--------------");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			// custom title, error icon
			JOptionPane.showMessageDialog(sc,
					"Please select saved version of the Grade Thief Game, This file is not belong to Grade-Thief",
					"Grade-Thief Loading", JOptionPane.ERROR_MESSAGE);
		}

	}
}

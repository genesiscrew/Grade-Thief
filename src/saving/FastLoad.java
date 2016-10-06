package saving;

import java.io.File;
import java.util.Scanner;

import javax.swing.JOptionPane;

import gui.GameController;
import gui.Screen;

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


					//GameController.getPlayer().setViewFrom(new double[] { 100, 100, 100 });
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
					System.out.println(" itemColor :" + itemTokens[0]);
					System.out.println(" itemHeight :" + itemTokens[1]);
					System.out.println(" itemID :" + itemTokens[2]);
					System.out.println(" itemLength :" + itemTokens[3]);
					System.out.println(" itemType :" + itemTokens[4]);
					System.out.println(" itemWidth :" + itemTokens[5]);
					System.out.println(" itemX :" + itemTokens[6]);
					System.out.println(" itemY :" + itemTokens[7]);
					System.out.println(" itemZ :" + itemTokens[8]);

				}

			}

			double[] pos = GameController.getPlayer().getViewFrom();

			System.out.println("-----------nd of input.txt--------------");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			//custom title, error icon
			JOptionPane.showMessageDialog(sc,
			    "Please select saved version of the Grade Thief Game, This file is not belong to Grade-Thief",
			    "Grade-Thief Loading",
			    JOptionPane.ERROR_MESSAGE);
		}

	}
}

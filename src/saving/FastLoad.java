package saving;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import gui.GameController;

public class FastLoad {

	String myFile;

	public FastLoad(String myFile) {
		super();
		this.myFile = myFile;
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
					System.out.println("X=" + l[0] + "Y=" + l[1] + "Z=" + l[2]);

					double x = Double.parseDouble(l[0]);
					double y = Double.parseDouble(l[1]);
					double z = Double.parseDouble(l[2]);

					GameController.getPlayer().setStartX(x);
					GameController.getPlayer().setStartY(y);
					GameController.getPlayer().setStartY(z);

					GameController.getPlayer().setViewFrom(new double[] { x, y, z });
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

			System.out.println("-----------nd of input.txt--------------");

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

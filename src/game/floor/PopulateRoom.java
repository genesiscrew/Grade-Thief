package game.floor;

import java.util.Scanner;

public class PopulateRoom {

	public void populateRoom(Room room, String items) {

		Scanner sc = new Scanner(items);
		String line = "";
		int roomX;
		int roomY;
		int ID;
		String itemType;
		int keyID;
		int numberOfItems;
		
		x y id name itemType (keyID, numberOfItems)

		
		while (sc.hasNextLine()) {
			line = sc.nextLine();
			roomX = sc.nextInt();
			roomY = sc.nextInt();
			ID = sc.nextInt();
			itemType = sc.next();
			if (itemType.equals("C")) {
				
			}else if (itemType.equals(("K")) {
				
			}
			


		}

	}

}

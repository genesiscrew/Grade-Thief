package items;

import java.util.ArrayList;
import java.util.List;

public class Main {

	static List<GameObject> containedItems = new ArrayList<GameObject>();

	public static void main(String[] args) {
		Keys oneKey = new Keys( 9999, 1111);
		Door door = new Door( 1111, 1111);
		oneKey.useItem(door);


		containedItems.add(oneKey);
		containedItems.add(door);
		Container c = new Container(0, containedItems);
		c.useItem();



	}

}

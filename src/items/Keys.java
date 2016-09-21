package items;

public class Keys extends Item implements Interactable, Movable {

<<<<<<< HEAD
	String description = "a key";
	public int keyID;
	public String itemType = "K";
=======
    String description = " a key";
>>>>>>> c205558933e0cfa0a631cbd8a4d66be5234bba3d

	public Keys(int itemID, String itemType) {
		super(itemID, itemType);
	}

<<<<<<< HEAD
	public Keys(int itemID, String itemType, int keyID) {
		super(itemID, itemType);
		this.keyID = keyID;
	}

	@Override
	public void pickUp() {
		// remove item from scene
		// add to inventory


	}

	@Override
	public void useItem(GameObject j) {
		System.out.println("test");
		System.out.println(j.itemType);
		System.out.println("test");

		if (j.itemType().equals("Door")) {
				j = (Door) j;
				Door jDoor = (Door) j;
				if (keyID == jDoor.keyID)
						System.out.println("unlocked the door");
				else
						System.out.println("Door still locked");

		}
		else if (j instanceof Container) {
			Container e = (Container) j;
			if (keyID == e.keyID)
				System.out.println("unlocked the container");
		else
				System.out.println("container still locked");

}

		}
		// TODO Auto-generated method stub

		// check to see if charger is being usedON a wall socket



	@Override
	public void useItem() {	}

	@Override
	public void move(Direction dir, Distance d) {
		// TODO Auto-generated method stub

	}

=======
	public String toString() {
		return "K";
	}
    public String itemType() {
        return itemType;
    }

    @Override
    public void pickUp(){
        // remove item from scene
        // add to inventory
    }

    @Override
    public void useItem(GameObject j) {
        System.out.println("test");
        System.out.println(j.itemType);
        System.out.println("test");

        if (j.itemType().equals("Door")) {
            j = (Door) j;
            Door jDoor = (Door) j;
            if (itemID == jDoor.itemID)
                System.out.println("unlocked the door");
            else
                System.out.println("Door still locked");

        } else if (j instanceof Container) {
            Container e = (Container) j;
            if (itemID == e.itemID)
                System.out.println("unlocked the container");
            else
                System.out.println("container still locked");
        }

    }


    @Override
    public void useItem() {

    }

    @Override
    public void move(Direction dir, Distance d) {
        // TODO Auto-generated method stub
    }
>>>>>>> c205558933e0cfa0a631cbd8a4d66be5234bba3d
}

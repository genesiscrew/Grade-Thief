package items;

public class Keys extends Item implements Interactable, Movable {

    String description = " a key";

	public Keys(int itemID, String itemType) {
		super(itemID, itemType);
	}

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
}

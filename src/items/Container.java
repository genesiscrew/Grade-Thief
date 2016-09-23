package items;

    import java.util.ArrayList;
import java.util.List;

    public class Container extends Item implements Interactable, Movable{

    	public List<Item> containedItems = new ArrayList<Item>();
    	int keyID;
    	public Container(int itemID, List<Item> containedItems, String itemType, int keyID) {
    		super(itemID, itemType);
    		if (containedItems != null) {
    		this.containedItems = containedItems;
    		} else {
    			this.containedItems = new ArrayList<Item>();
    		}
            this.keyID = keyID;
    	}

    	public void setItems(List<Item> containedItems) {
    		this.containedItems = containedItems;
    	}

    	public List<Item> getItems() {
    		for (Item g : containedItems) {
    			System.out.println(g.itemID);
    		}
    		return containedItems;
    	}

    	@Override
    	public void pickUp() {

    	}

    	public String toString() {
    		if (containedItems == null)
    				return "0";
    		//return "C";
    		return String.valueOf(containedItems.size());
    	}

    	@Override
    	public void useItem(GameObject j) {
    		// TODO Auto-generated method stub

    	}

    	@Override
    	public void useItem() {

    	}

    	@Override
    	public void move(Direction dir, Distance d) {
    		// TODO Auto-generated method stub

    	}
    	/**
    	 * add item into container
    	 */
    	public void addItem(Item item) {
    		System.out.println("adding item to container" + item.toString());
    		this.containedItems.add(item);

    	}

    }

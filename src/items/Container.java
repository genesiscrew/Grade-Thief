package items;

    import java.util.ArrayList;
import java.util.List;

    public class Container extends Item implements Interactable, Movable{

        public List<Item> containedItems = new ArrayList<Item>();
        private Container containedContainer;

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
        	if (this.containedContainer != null) {
        		System.out.println("container " + this.containedContainer);
        		this.containedContainer.getItems();
        	}

            int i = 0;

            for (Item g : this.containedItems) {
            	i++;
                System.out.println("item "); // g.toString());
            }

            if (containedItems == null) // quick test hack
            	return containedContainer.getItems();

            return containedItems;
        }

        @Override
        public void pickUp() {

        }

        public String toString() {
        	int extra = 0;
            if (containedItems == null)
                    extra = 0;
            else
            	extra += containedItems.size();

            if (getContainedContainer() != null)
            	extra += getContainedContainer().containedItems.size() + 1; // container within container is considered item

            return String.valueOf(extra);
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

		public Container getContainedContainer() {
			return containedContainer;
		}

		public void setContainedContainer(Container containedContainer) {
			this.containedContainer = containedContainer;
		}


    }

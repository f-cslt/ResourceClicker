package model;


import static utils.FileHandler.getBufferedReader;
import static utils.FileHandler.writeToSaveFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.SortedSet;
import java.util.TreeSet;

import controller.InventoryController;
import utils.GameConstants.ObjectConstants;

public class InventoryModel extends AbstractModel {
    private static final int DEFAULT_CAPACITY = 30; 

    private final String title;
    private final SortedSet<InventoryItem> inventory;
    private int capacity;
    private int itemCount;

    public void saveData() {
    	// Data de chaque item de l'inventaire
		writeToSaveFile("## INVENTORY ITEM DATA:");
		for(InventoryItem item : inventory) {
			// On stock just l'id car on appellera GetRootObject(spriteId)
			writeToSaveFile(item.entity.spriteId + ";" + item.quantity);	
		}
    }
    
    public void loadData() {
		BufferedReader bufferedReader = getBufferedReader("/sauvegarde.txt");
		String line;
		try {
			while ((line = bufferedReader.readLine()) != null) {
			    switch (line) {
			        case "## INVENTORY ITEM DATA:":
			        	inventory.clear();	// On vide l'inventaire de la partie en cours
			            while ((line = bufferedReader.readLine()) != null && !line.startsWith("##")) {
			                String[] itemLine = line.split(";");	// Les données sont parsées avec des ';'
			                RootObject rootObject = ObjectConstants.getRootObject(Integer.parseInt(itemLine[0]));
							int quantity = Integer.parseInt(itemLine[1]);
							addItem(rootObject, quantity);
			            }
			            break;
			        default:
			            break;
			    }
			}
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * Constructs an {@code InventoryModel} with default capacity.
     * 
     * @param title the inventory title.
     */
    public InventoryModel(String title) {
        this(title, DEFAULT_CAPACITY);
    }

    /**
     * Constructs an {@code InventoryModel}.
     * 
     * @param title the inventory title.
     * @param capacity the inventory capacity.
     */
    public InventoryModel(String title, int capacity) {
        this.title      = title;
        this.inventory  = new TreeSet<>();
        this.capacity   = capacity;
        this.itemCount  = 0;
    }

    /**
     * Gets the inventory capacity.
     * 
     * @return the capacity
     */
    public int getCapacity() {
        return capacity;
    }

    public int getRemainingCapacity() {
        return capacity - itemCount;
    }

    /**
     * Sets the inventory capacity. This is not retroactive.
     * 
     * @param capacity the new capacity.
     */
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    /**
     * Gets the inventory title.
     * 
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the underlying set of items.
     * 
     * @return a copy of the items.
     */
    public SortedSet<InventoryItem> getInventory() {
        return new TreeSet<>(inventory);
    }

    /**
     * Tries to Add the specified object to the inventory.
     * 
     * @param entity object to add.
     * @return quantity added
     */
    public int addItem(RootObject entity) {
        return addItem(entity, 1, null);
    }

    /**
     * Tries to Add the specified object to the inventory.
     * 
     * @param entity object to add.
     * @param quantity quantity of the specified object to add.
     * @return quantity added
     */
    public int addItem(RootObject entity, int quantity) {
        return addItem(entity, quantity, null);
    }

    /**
     * Adds as much as possible of specified object to the inventory.
     * 
     * @param entity object to add.
     * @param quantity quantity of the specified object to add.
     * @param userData any kind of data.
     * @return quantity added
     */
    public int addItem(RootObject entity, int quantity, Object userData) {
        if (capacity != Integer.MAX_VALUE && getRemainingCapacity() <= 0) {
            return 0;
        }
        
        boolean itemExists = false;
        
        if (capacity != Integer.MAX_VALUE) {
            quantity = Math.min(getRemainingCapacity(), Math.max(0, quantity));
        }

        for (InventoryItem e : inventory) {
            if (e.entity.equals(entity)) {
                e.quantity+=quantity;
                itemExists = true;
                break;
            }
        }

        if (!itemExists) {
            inventory.add(new InventoryItem(entity, quantity, userData));
        }

        itemCount += quantity;
        firePropertyChange(InventoryController.ELEMENT_ADD_ITEM_PROPERTY, null, this);

        return quantity;
    }

    /**
     * Gets the first inventory item containing the specified object.
     * 
     * @param entity object to find.
     * @return an inventory item or {@code null}
     */
    public InventoryItem getItem(RootObject entity) {
        for (InventoryItem e : inventory) {
            if (e.entity.equals(entity)) {
                return e;
            }
        }

        return null;
    }

    /**
     * Removes one unit of the specified object.
     * 
     * @param entity object to remove.
     * @return quantity removed
     */
    public int removeItem(RootObject entity) {
        return removeItem(entity, 1);
    }

    /**
     * Removes the specified quantity of the object.
     * 
     * @param entity object to remove.
     * @param quantity quantity to remove.
     * @return quantity removed
     */
    public int removeItem(RootObject entity, int quantity) {
        int res = 0;

        for (InventoryItem e : inventory) {
            if (e.entity.equals(entity)) {
                int oldQt = e.quantity;

                e.quantity = Math.max(e.quantity-quantity, 0);
                firePropertyChange(InventoryController.ELEMENT_REMOVE_ITEM_PROPERTY, null, this);

                res = oldQt - e.quantity;
                break;
            }
        }

        itemCount -= res;
        assert itemCount >= 0;

        return res;
    }

    public static class InventoryItem implements Comparable<InventoryItem> {
        public final RootObject entity;
        public final Object userData;
        private int quantity;
    
        public InventoryItem(RootObject entity, int quantity) {
            this(entity, quantity, null);
        }
        
        public InventoryItem(RootObject entity, int quantity, Object userData) {
            this.entity     = entity;
            this.userData   = userData;
            this.quantity   = quantity;
        }

        public Integer getQuantity() {
            return quantity;
        }

        @Override
        public int compareTo(InventoryItem o) {
            return entity.name.compareTo(o.entity.name);
        }
    }
}

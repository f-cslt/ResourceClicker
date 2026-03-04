package model;

import static utils.GameConstants.ObjectConstants.MARKET_ID;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.SortedSet;

import utils.InventoryUtils;
import utils.GameConstants.ObjectConstants;
import controller.InventoryController;
import model.GameState.States;
import view.MarketView;

public class Market extends GameEntity {
    public static final String INVENTORY_SELL_GROUP = "Sell";
    public static final String INVENTORY_BUY_GROUP = "Buy";

    private final InventoryModel buyInventory;
    
    /**
     * Constructs a {@code Market}.
     * 
     * @param x coordinate of this object on the map.
     * @param y coordinate of this object on the map.
     */
    public Market(int x, int y) {
        super(x, y, false, ObjectConstants.getRootObject(MARKET_ID));
        this.buyInventory = new InventoryModel(INVENTORY_BUY_GROUP, Integer.MAX_VALUE);

        InventoryUtils.initInventoryWithAllAvailableResources(buyInventory, Integer.MAX_VALUE);
    }

    @Override
    public void triggerAction(PlayerModel player) {
        GameState
            .getGameState()
                .pushState(
                    States.CUSTOM, 
                    new InventoryController(
                        player,
                        new MarketView(
                            "Market"
                        ),
                        buyInventory,
                        new InventoryWrapper(INVENTORY_SELL_GROUP, player.getInventory())
                    )
                );
    }

    /**
     * Computes the price for the specified item.
     * 
     * @param item item to compute the price of.
     * @param isForSale whether the item is to be sold or not.
     * @return the price
     */
    public static double getItemPrice(RootObject item, boolean isForSale) {
        return item.freq * 10 * (isForSale ? 0.8 : 1);
    }

    private class InventoryWrapper extends InventoryModel {
        private final InventoryModel inventoryModel;
        private final PropertyChangeListener propertyChangeListener = this::propertyChange;

        private InventoryWrapper(String title, InventoryModel inventoryModel) {
            super(title);
            this.inventoryModel = inventoryModel;
        }

        @Override
        public SortedSet<InventoryItem> getInventory() {
            return inventoryModel.getInventory();
        }

        @Override
        public int addItem(RootObject entity) {
            return inventoryModel.addItem(entity);
        }

        @Override
        public int addItem(RootObject entity, int quantity, Object userData) {
            return inventoryModel.addItem(entity, quantity, userData);
        }

        @Override
        public InventoryItem getItem(RootObject entity) {
            return inventoryModel.getItem(entity);
        }

        @Override
        public int removeItem(RootObject entity) {
            return inventoryModel.removeItem(entity);
        }
        
        @Override
        public int removeItem(RootObject entity, int quantity) {
            return inventoryModel.removeItem(entity, quantity);
        }

        @Override
        public void addPropertyChangeListener(PropertyChangeListener listener) {
            inventoryModel.addPropertyChangeListener(propertyChangeListener);
            super.addPropertyChangeListener(listener);
        }

        @Override
        public void removePropertyChangeListener(PropertyChangeListener listener) {
            super.removePropertyChangeListener(listener);
            inventoryModel.removePropertyChangeListener(propertyChangeListener);
        }

        private void propertyChange(PropertyChangeEvent evt) {
            firePropertyChange(evt.getPropertyName(), null, this);
        }
    }
}

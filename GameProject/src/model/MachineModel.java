package model;

import controller.InventoryController;
import main.Ticker;
import model.GameState.States;

public abstract class MachineModel extends GameEntity {
    public static final String INVENTORY_STOCK_GROUP = "Stock";
    protected static final int INVENTORY_XXL_CAPACITY = 100;

    protected final InventoryModel stockInventory;

    public MachineModel(int x, int y, RootObject rootObject) {
        this(x, y, rootObject, GameEntityStatus.NOTHING);
    }

    public MachineModel(int x, int y, RootObject rootObject, GameEntityStatus status) {
        super(x, y, false, rootObject, status);
        this.stockInventory = new InventoryModel(INVENTORY_STOCK_GROUP);
        Ticker.addListener(getTickerListenerTag(), this::update);
    }

    private String getTickerListenerTag() {
        return getClass().getSimpleName() + hashCode();
    }

    /**
     * Creates the inventory for this machine.
     * 
     * @param player the player to interact with.
     * @return the inventory
     */
    protected abstract InventoryController makeInventory(PlayerModel player);

    /**
     * Create the configuration for this machine. 
     * 
     * @param player the player to interact with.
     * @return the configuration
     */
    public abstract InventoryController makeConfigInventory(PlayerModel player);


    @Override
    public void triggerAction(PlayerModel player) {
        GameState
            .getGameState()
                .pushState( 
                    States.INVENTORY, 
                    getStatus() != GameEntityStatus.REQUIRES_ACTION ? makeInventory(player) : makeConfigInventory(player)
                );
    }

    @Override
    public void close() {
        Ticker.removeListener(getTickerListenerTag());
        super.close();
    }
}

package utils;

import static utils.GameConstants.ObjectConstants.OBJECTS_CONFIG;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;

import model.InventoryModel;
import model.PlayerModel;
import model.InventoryModel.InventoryItem;
import utils.GameConstants.RecipeContants;
import view.InventoryView.InventoryItemInfo;

public abstract class InventoryUtils {
    /**
     * Initializes the specified inventory with all available resources.
     * 
     * @param inventory the inventory to initialize.
     * @param quantity the quantity each item should contain.
     */
    public static void initInventoryWithAllAvailableResources(InventoryModel inventory, int quantity) {
        OBJECTS_CONFIG.values().forEach((v) -> {
            if (v.rootObject.targetedClass != null) {
                inventory.addItem(v.rootObject, quantity, null);
            }
        });
    }

    public static void initInventoryWithAllAvailableRecipies(InventoryModel inventory, int quantity) {
        RecipeContants.recipies.values().forEach((e) -> {
            inventory.addItem(e.getCookedResource(), quantity, e);
        });
    }

    /**
     * Transfers an item to the player's inventory.
     * 
     * @param mode
     * @param group
     * @param item
     * @param userData
     */
    public static void giveItem(PlayerModel player, InventoryModel dst, InventoryItem item) {
        int rv = player.getInventory().removeItem(item.entity, item.getQuantity());
        dst.addItem(item.entity, rv, null);
    }

    /**
     * Tranfers an item from the player's inventory.
     * 
     * @param mode
     * @param group
     * @param item
     * @param userData
     */
    public static void collectItem(InventoryModel src, InventoryModel dst, InventoryItem item) {
        int qt = Math.min(item.getQuantity(), dst.getRemainingCapacity());
        
        if ((qt = src.removeItem(item.entity, qt)) != 0) {   
            dst.addItem(item.entity, qt, null);
        }
    }

    /**
     * Creates a "Collect" and "Collect all" button.
     * 
     * @param player the player to interact with.
     * @param inventory the inventory to collect the item from.
     * @param item the item to collect.
     * @return the component
     */
    @SuppressWarnings("unused")
    public static JComponent makeCollectButtons(InventoryItemInfo info) {
        JButton collectButton       = new JButton("Collect");
        JButton collectAllButton    = new JButton("Collect all");

        InventoryItem singleItem = new InventoryItem(info.item.entity, 1);
        InventoryItem allItem = new InventoryItem(info.item.entity, Integer.MAX_VALUE);

        if (info.item.getQuantity() > 0) {
            collectButton.addActionListener(e -> InventoryUtils.collectItem(info.inventory, info.player.getInventory(), singleItem));
            collectAllButton.addActionListener(e -> InventoryUtils.collectItem(info.inventory, info.player.getInventory(), allItem));
        } else {
            collectButton.setEnabled(false);
            collectAllButton.setEnabled(false);
        }

        Box box = Box.createHorizontalBox();
        box.add(collectButton);
        box.add(collectAllButton);
        return box;
    }
}

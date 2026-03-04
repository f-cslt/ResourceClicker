package model;

import java.util.Arrays;

import model.InventoryModel.InventoryItem;

public class Recipe {
    private final InventoryItem[] ingredients;
    private final RootObject cookedResource;
    private final int cookingTime;
    private long nextReadyTime;
    private boolean hasBeenCollected = true;

    /**
     * Constructs a {@code Recipe} with the specified ingredients.
     * 
     * @param cookedResource the resulting object.
     * @param ingredients the ingredients for this recipe.
     */
    public Recipe(RootObject cookedResource, InventoryItem... ingredients) {
        this.ingredients    = ingredients;
        this.cookedResource = cookedResource;
        this.cookingTime    = Math.max(0, cookedResource.freq-2) * 3000; // resources with a freq <= 2 are instantaneous
    }

    /**
     * Gets the ingredients for this recipe.
     * 
     * @return a copy of this recipe's ingredients
     */
    public InventoryItem[] getIngredients() {
        return Arrays.copyOf(ingredients, ingredients.length);
    }

    /**
     * Gets the resource produced by this recipe.
     * 
     * @return the final resource
     */
    public RootObject getCookedResource() {
        return cookedResource;
    }

    /**
     * Gets the cooking time for this recipe.
     * 
     * @return the cooking time
     */
    public int getCookingTime() {
        return cookingTime;   
    }

    /**
     * Compute the time remaining before this recipe is finished.
     * 
     * @return the remaining time
     */
    public long getRemainingCookingTime() {
        return Math.max(0, nextReadyTime - System.currentTimeMillis());
    }

    /**
     * Collects the cooked resource.
     * 
     * @return the cooked resource or {@code null} if not yet ready.
     */
    public RootObject collect() {
        if (!hasBeenCollected && System.currentTimeMillis() > nextReadyTime) {
            hasBeenCollected = true;
            return cookedResource;
        }

        return null;
    }

    public boolean canCookRecipe(InventoryModel inventory) {
        for (InventoryItem e : ingredients) {
            InventoryItem item = inventory.getItem(e.entity);
            if (item == null || item.getQuantity() < e.getQuantity()) {
                return false;
            }
        }
        
        return true;
    }

    /**
     * Starts cooking if not already busy and all ingredients are provided.
     * 
     * @param inventory the inventory containing the ingredients.
     * @return whether a new recipe was started or not.
     */
    public boolean cook(InventoryModel inventory) {
        if (!hasBeenCollected)
            return false;

        if (!canCookRecipe(inventory)) {
            return false;
        }

        hasBeenCollected = false;
        nextReadyTime = System.currentTimeMillis() + cookingTime;

        for (InventoryItem e : ingredients) {
            inventory.removeItem(e.entity, e.getQuantity());
        }

        return true;
    }
}

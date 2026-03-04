package model;

import static utils.GameConstants.ObjectConstants.FACTORY_ID;

import controller.InventoryController;
import model.InventoryModel.InventoryItem;
import utils.InventoryUtils;
import utils.GameConstants.ObjectConstants;
import view.FactoryConfigurationView;
import view.FactoryInventoryView;

public class FactoryModel extends MachineModel {
    public static final String INVENTORY_INGREDIENTS_GROUP = "Ingredients";

    private Recipe recipe;
    protected final InventoryModel ingredientsInventory;

    public FactoryModel(Integer x, Integer y, RootObject rootObject) {
        super(x, y, ObjectConstants.getRootObject(FACTORY_ID), GameEntityStatus.REQUIRES_ACTION);
        this.ingredientsInventory = new InventoryModel("Ingredients");
    }

    @Override
    public long getTimeLeftUntilReady() {
        if (recipe != null) {
            return recipe.getRemainingCookingTime();
        }

        return super.getTimeLeftUntilReady();
    }

    @Override
    public long getTimeToComplete() {
        if (recipe != null) {
            return recipe.getCookingTime();
        }

        return super.getTimeToComplete();
    }

    /**
     * Gets the recipe this factory is configured to execute.
     * 
     * @return the recipe
     */
    public Recipe getRecipe() {
        return recipe;
    }

    /**
     * Sets the recipe this factory has to executes.
     * 
     * @param recipe a recipe tot execute
     */
    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;

        if (recipe != null) {
            for (InventoryItem e : recipe.getIngredients()) {
                ingredientsInventory.addItem(e.entity, 0, null);
            }

            stockInventory.addItem(recipe.getCookedResource(), 0, null);
        }
        
        setStatus(recipe != null ? GameEntityStatus.BUSY : GameEntityStatus.REQUIRES_ACTION);
    }

    @Override
    public void update() {
        if (recipe == null) return;

        RootObject resource = recipe.collect();
        if (resource != null) {
            stockInventory.addItem(resource);
        }

        recipe.cook(ingredientsInventory);
    }

    @Override
    protected InventoryController makeInventory(PlayerModel player) {
        return new InventoryController(
            player,
            new FactoryInventoryView(this, player),
            ingredientsInventory, 
            stockInventory
        );
    }

    @Override
    public InventoryController makeConfigInventory(PlayerModel player) {
        InventoryModel recipeInventory = new InventoryModel("Recipes available", Integer.MAX_VALUE);
        InventoryUtils.initInventoryWithAllAvailableRecipies(recipeInventory, Integer.MAX_VALUE);

        return new InventoryController(
            player, 
            new FactoryConfigurationView(this), 
            recipeInventory
        );
    }

    public static class FactoryXXL extends FactoryModel {
        public FactoryXXL(Integer x, Integer y, RootObject rootObject) {
            super(x, y, rootObject);
            stockInventory.setCapacity(INVENTORY_XXL_CAPACITY);
            ingredientsInventory.setCapacity(INVENTORY_XXL_CAPACITY);
        }
    }
}

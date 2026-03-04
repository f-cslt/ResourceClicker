package view;

import model.FactoryModel;
import model.Recipe;
import model.InventoryModel.InventoryItem;

public class FactoryConfigurationView extends MachineConfigurationView {
    public FactoryConfigurationView(FactoryModel model) {
        super("Factory configuration", model);
    }

    private FactoryModel factory() { return (FactoryModel)userData; }

    @Override
    protected boolean isButtonEnabled(InventoryItem item) {
        Recipe recipe = factory().getRecipe();
        return (recipe == null) || (recipe.getCookedResource().spriteId != item.entity.spriteId);
    }

    @Override
    protected void onItemSelected(InventoryItem item) {
        if (!(item.userData instanceof Recipe)) {
            throw new IllegalArgumentException("Item user data must be a recipe");
        }

        factory().setRecipe((Recipe)(item.userData));        
    }
}
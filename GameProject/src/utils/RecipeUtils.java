package utils;

import java.util.Collection;

import javax.swing.JButton;
import javax.swing.JComponent;

import main.Ticker;
import model.InventoryModel;
import model.Recipe;
import model.RootObject;

public abstract class RecipeUtils {
    public static JComponent makeCookSelectionButton(InventoryModel targetInventory, Recipe recipe, Collection<String> listenerTagsRegistery) {
        JButton cookingTimeButton = new JButton();
        setupCookButton(new CookButtonInfo(cookingTimeButton, targetInventory, recipe, listenerTagsRegistery), true);

        return cookingTimeButton;
    }   

    private static String getCookingTimeToDisplay(Recipe recipe) {
        long cookingTime = recipe.getRemainingCookingTime();
        if (cookingTime == 0) {
            cookingTime = recipe.getCookingTime();
        }

        return String.format("%.2fs", cookingTime/1000.);
    }

    @SuppressWarnings("unused")
    private static void setupCookButton(CookButtonInfo info, boolean firstInit) {
        info.button.setText(getCookingTimeToDisplay(info.recipe));

        if (info.recipe.getRemainingCookingTime() > 0) {
            animateButton(info);
        } else if (!info.recipe.canCookRecipe(info.dstInventory)) {
            info.button.setEnabled(false);
        } else {
            info.button.setEnabled(true);
        }

        if (firstInit) {
            info.button.addActionListener(e -> onButtonClicked(info));
        }
    }

    private static void onButtonClicked(CookButtonInfo info) {
        boolean rv = info.recipe.cook(info.dstInventory);
        assert rv;

        animateButton(info);
    }
    
    private static void animateButton(CookButtonInfo info) {
        String tag = "CookButtonUpdate" + info.button.hashCode();
        
        info.button.setEnabled(false);
        info.listenerTagsRegistery.add(tag);

        Ticker.removeListener(tag);
        Ticker.addListener(tag, () -> updateCookButtonTime(tag, info));
    }

    private static void updateCookButtonTime(String listenerTag, CookButtonInfo info) {
        RootObject cookedResource = info.recipe.collect();

        if (cookedResource != null) {
            Ticker.removeListener(listenerTag);
            info.dstInventory.addItem(cookedResource);

            setupCookButton(info, false);
            return;
        }
        
        info.button.setText(getCookingTimeToDisplay(info.recipe));
    }

    private static class CookButtonInfo {
        final JButton button;
        final InventoryModel dstInventory;
        final Recipe recipe;
        final Collection<String> listenerTagsRegistery;

        CookButtonInfo(JButton button, InventoryModel dstInventory, Recipe recipe, Collection<String> listenerTagsRegistery) {
            this.button                 = button;
            this.dstInventory           = dstInventory;
            this.recipe                 = recipe;
            this.listenerTagsRegistery  = listenerTagsRegistery;
        }
    }
}

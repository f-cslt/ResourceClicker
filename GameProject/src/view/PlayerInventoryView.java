package view;

import static utils.GameConstants.ObjectConstants.OBJECTS_CONFIG;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;

import main.Ticker;
import model.AtlasSprite;
import model.GameEntity;
import model.GameState;
import model.MapModel;
import model.PlayerModel;
import model.Recipe;
import model.InventoryModel.InventoryItem;
import utils.RecipeUtils;

public class PlayerInventoryView extends InventoryView {
    private final Set<String> tickerListenersTags = new HashSet<>();

    public PlayerInventoryView() {
        this(null);
    }

    public PlayerInventoryView(Point point) {
        super("Player inventory", null, point);
    }

    @SuppressWarnings("unused")
    @Override
    protected JComponent makeSelectionButtons(InventoryItemInfo info) {
        JButton button = new JButton("Place");
        Point point;

        if (userData instanceof Point) {
            point = (Point)userData;
        } else {
            return null;
        }

        if (info.item.getQuantity() == 0) {
            button.setEnabled(false);
        } else {
            button.addActionListener(e -> {
                placeItem(info.player, info.item, point);
            });
        }

        return button;
    }

    private void placeItem(PlayerModel player, InventoryItem item, Point point) {
        try {
            GameEntity entity = item.entity.constructEntity(point.x, point.y);

            if (entity.canBePlacedAt(entity.getX(), entity.getY())) {
                player.getInventory().removeItem(item.entity);
                MapModel.getActiveMap().place(entity, point.x, point.y);
                GameState.getGameState().popState();
            }

        } catch (NoSuchMethodException | SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    protected JComponent makeItem(InventoryItemInfo info) {
        switch (info.inventory.getTitle()) {
            case PlayerModel.INVENTORY_CRAFT_GROUP:
                return makeCraftItem(info);
        }

        return super.makeItem(info);
    }

    private JComponent makeCraftItem(InventoryItemInfo info) {
        Box hbox = Box.createHorizontalBox();
        Recipe recipe = (Recipe)(info.item.userData);

        for (InventoryItem e : recipe.getIngredients()) {
            AtlasSprite sprite = OBJECTS_CONFIG.get(e.entity.spriteId).atlasSprite;
            JLabel qt = new JLabel("" + e.getQuantity());
            JLabel img = getLabelFromBufferedImage(sprite.getImg());

            hbox.add(qt);
            hbox.add(createHorizontalStrut(4));
            hbox.add(img);
            hbox.add(createHorizontalStrut(8));
        }

        AtlasSprite sprite = OBJECTS_CONFIG.get(recipe.getCookedResource().spriteId).atlasSprite;
        JComponent cookingTimeButton = RecipeUtils.makeCookSelectionButton(info.player.getInventory(), recipe, tickerListenersTags);

        hbox.add(Box.createHorizontalGlue());
        hbox.add(getLabelFromBufferedImage(sprite.getImg()));
        hbox.add(createHorizontalStrut(4));
        hbox.add(new JLabel(recipe.getCookedResource().name));
        hbox.add(createHorizontalStrut(8));
        hbox.add(cookingTimeButton);
        hbox.add(createHorizontalStrut(8));

        return hbox;
    }

    @Override
    public void unregisterUIComponents() {
        for (String e : tickerListenersTags) {
            Ticker.removeListener(e);
        }

        super.unregisterUIComponents();
    }
}

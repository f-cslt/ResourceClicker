package view;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;

import model.FactoryModel;
import model.PlayerModel;
import model.InventoryModel.InventoryItem;
import utils.InventoryUtils;

public class FactoryInventoryView extends MachineInventoryView {
    public FactoryInventoryView(FactoryModel model, PlayerModel player) {
        super("Factory", model, player);
    }

    @Override
    protected JComponent makeSelectionButtons(InventoryItemInfo info) {
        String tabTitle = info.inventory.getTitle();

        switch (tabTitle) {
            case FactoryModel.INVENTORY_INGREDIENTS_GROUP:
                return makeGiveCollectButtons(info);
            case FactoryModel.INVENTORY_STOCK_GROUP:
                return InventoryUtils.makeCollectButtons(info);
            default:
                throw new IllegalArgumentException("Unknown inventory `" + tabTitle + "`");
        }
    }

    @SuppressWarnings("unused")
    private JComponent makeGiveCollectButtons(InventoryItemInfo info) {
        JButton giveButton          = new JButton("Give");
        JButton giveAllButton       = new JButton("Give all");
        JButton collectButton       = new JButton("Collect");
        JButton collectAllButton    = new JButton("Collect all");

        InventoryItem singleItem = new InventoryItem(info.item.entity, 1);
        InventoryItem allItem = new InventoryItem(info.item.entity, Integer.MAX_VALUE);

        giveButton.addActionListener(e -> InventoryUtils.giveItem(info.player, info.inventory, singleItem));
        giveAllButton.addActionListener(e -> InventoryUtils.giveItem(info.player, info.inventory, allItem));
        collectButton.addActionListener(e -> InventoryUtils.collectItem(info.inventory, info.player.getInventory(), singleItem));
        collectAllButton.addActionListener(e -> InventoryUtils.collectItem(info.inventory, info.player.getInventory(), allItem));

        Box box = Box.createHorizontalBox();
        box.add(giveButton);
        box.add(giveAllButton);
        box.add(collectButton);
        box.add(collectAllButton);

        return box;
    }
}

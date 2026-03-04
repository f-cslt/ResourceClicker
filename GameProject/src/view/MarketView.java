package view;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;

import model.Market;
import model.PlayerModel;
import model.InventoryModel.InventoryItem;

public class MarketView extends InventoryView {
    public MarketView(String title) {
        super(title, null, null);
    }

    @Override
    protected JComponent makeHeader(PlayerModel player) {
        JLabel label = new JLabel("Current balance: ");
        JLabel money = new JLabel("" + player.getMoney());

        Box box = Box.createHorizontalBox();
        box.add(Box.createHorizontalGlue());
        box.add(label);
        box.add(createHorizontalStrut(8));
        box.add(money);
        box.add(createHorizontalStrut(8));

        return box;
    }

    @Override
    protected JComponent makeSelectionButtons(InventoryItemInfo info) {
        String tabTitle = info.inventory.getTitle();

        switch (tabTitle) {
            case Market.INVENTORY_BUY_GROUP:
            case Market.INVENTORY_SELL_GROUP:
                return makeButton(info.player, info.item, tabTitle.equals(Market.INVENTORY_BUY_GROUP));
            default:
                throw new IllegalArgumentException("Unknown inventory `" + tabTitle + "`");
        }

    }

    @SuppressWarnings("unused")
    private JComponent makeButton(PlayerModel player, InventoryItem item, boolean isBuy) {
        double price = Market.getItemPrice(item.entity, !isBuy);
        JButton button = new JButton("" + price);
        button.setFocusable(false);
        
        if (item.getQuantity() == 0 || (isBuy && player.getMoney() < price)) {
            button.setEnabled(false);
        } else {
            button.addActionListener(isBuy ? e -> onBuy(player, item) : e -> onSell(player, item));
        }

        return button;
    }

    private void onBuy(PlayerModel player, InventoryItem item) {
        double price = Market.getItemPrice(item.entity, false);

        if (player.getMoney() >= price) {
            if (player.getInventory().addItem(item.entity) > 0) {
                player.setMoney(player.getMoney()-price);
            }
        }

        reloadUI(player);
    }

    private void onSell(PlayerModel player, InventoryItem item) {
        double price = Market.getItemPrice(item.entity, true);

        if (player.getInventory().removeItem(item.entity) == 1) {
            player.setMoney(player.getMoney() + price);
            reloadUI(player);
        }
    }
}

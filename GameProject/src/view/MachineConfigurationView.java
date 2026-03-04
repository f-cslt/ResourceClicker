package view;

import javax.swing.JButton;
import javax.swing.JComponent;

import model.InventoryModel.InventoryItem;

public abstract class MachineConfigurationView extends InventoryView {
    public MachineConfigurationView(String title, Object userData) {
        super(title, null, userData);
    }

    /**
     * Whether the button should be enabled for this item.
     * 
     * @param item the item concerned.
     * @return button enabled or not
     */
    protected abstract boolean isButtonEnabled(InventoryItem item);

    /**
     * Callback to trigger when this item is selected.
     * 
     * @param item the item concerned.
     */
    protected abstract void onItemSelected(InventoryItem item);

    @SuppressWarnings("unused")
    @Override
    protected JComponent makeSelectionButtons(InventoryItemInfo info) {
        JButton button = new JButton("Select");

        if (!isButtonEnabled(info.item)) {
            button.setEnabled(false);
        } else {
            button.addActionListener(e -> {
                onItemSelected(info.item);
                reloadUI(info.player, info.inventory);
            });
        }

        return button;
    }
}

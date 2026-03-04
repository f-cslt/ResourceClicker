package view;

import javax.swing.JComponent;

import model.HarvesterModel;
import model.PlayerModel;
import utils.InventoryUtils;

public class HarvesterInventoryView extends MachineInventoryView {
    public HarvesterInventoryView(HarvesterModel model, PlayerModel player) {
        super("Harvester", model, player);
    }

    @Override
    protected JComponent makeSelectionButtons(InventoryItemInfo info) {
        return InventoryUtils.makeCollectButtons(info);
    }
}

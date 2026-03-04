package view;

import model.HarvesterModel;
import model.InventoryModel.InventoryItem;

public class HarvesterConfigurationView extends MachineConfigurationView {
    public HarvesterConfigurationView(HarvesterModel model) {
        super("Harvester configuration", model);
    }

    private HarvesterModel harvester() { return (HarvesterModel)userData; }

    @Override
    protected boolean isButtonEnabled(InventoryItem item) {
        return harvester().getTargetResource() == null || !item.entity.equals(harvester().getTargetResource());
    }

    @Override
    protected void onItemSelected(InventoryItem item) {
        harvester().setTargetResource(item.entity);
    }
}

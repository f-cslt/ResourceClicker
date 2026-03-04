package model;

import static utils.GameConstants.ObjectConstants.HARVESTER_ID;

import java.util.ArrayList;
import java.util.List;

import controller.InventoryController;
import model.PrimitiveResource.ResourceHarvestToken;
import utils.GameConstants.ObjectConstants;
import view.HarvesterConfigurationView;
import view.HarvesterInventoryView;

public class HarvesterModel extends MachineModel {
    private RootObject targetResource;
    private final MapModel map;
    private final List<ResourceHarvestToken> harvestTokens;

    /**
     * Constructs a {@code Harvester}.
     * 
     * @param x coordinates on the map.
     * @param y coordinates on the map.
     * @param map a map model.
     */
    public HarvesterModel(Integer x, Integer y, RootObject rootObject) {
        super(x, y, ObjectConstants.getRootObject(HARVESTER_ID), GameEntityStatus.REQUIRES_ACTION);
        this.map = MapModel.getActiveMap();
        this.harvestTokens = new ArrayList<>();
    }

    @Override
    public boolean canBePlacedAt(int x, int y) {
        GameEntity[] adj = map.getAdjacent(x, y);
        for (GameEntity e : adj) {
            if (e instanceof PrimitiveResource) {
                return true;
            }
        }

        return false;
    }

    /**
     * Gets the resource this harvester is configured to handle.
     * 
     * @return the resource id
     */
    public RootObject getTargetResource() {
        return targetResource;
    }

    /**
     * Sets the resource this harvester handles.
     * 
     * @param resourceId a resource id.
     */
    public void setTargetResource(RootObject resource) {
        assert resource == null || (resource.targetedClass != null && resource.targetedClass.equals(PrimitiveResource.class));
        this.targetResource = resource;
        setStatus(resource != null ? GameEntityStatus.NOTHING : GameEntityStatus.REQUIRES_ACTION);
    }

    @Override
    public void update() {
        recoltTokens();

        if (stockInventory.getRemainingCapacity() > 0) {
            harvestResources();
        }
    }

    private boolean idMatchTargetResource(RootObject resource) {
        return targetResource != null ? (targetResource.equals(resource)) : false;
    }

    private void recoltTokens() {
        GameEntity[] adj = map.getAdjacent(getX(), getY());
        for (GameEntity e : adj) {
            if (idMatchTargetResource(e.getRootObject()) && e instanceof PrimitiveResource) {
                PrimitiveResource r = (PrimitiveResource)e;
                ResourceHarvestToken token = r.getHarvestToken();
    
                if (token != null) {
                    harvestTokens.add(token);
                }
            }
        }
    }

    private void harvestResources() {
        List<ResourceHarvestToken> tmp = new ArrayList<>(harvestTokens);
        for (ResourceHarvestToken e : tmp) {
            RootObject resource = e.harvest();

            if (resource != null) {
                harvestTokens.remove(e);
                stockInventory.addItem(resource);
            }
        }
    }

    @Override
    public InventoryController makeConfigInventory(PlayerModel player) {
        InventoryModel resourceInventory = new InventoryModel("Targetable resources", Integer.MAX_VALUE);

        GameEntity[] adj = map.getAdjacent(getX(), getY());
        for (GameEntity e : adj) {
            if (e != null && e instanceof PrimitiveResource) {
                resourceInventory.addItem(e.getRootObject(), Integer.MAX_VALUE);
            }
        }

        return new InventoryController(
            player,
            new HarvesterConfigurationView(this),
            resourceInventory
        );
    }

    @Override
    protected InventoryController makeInventory(PlayerModel player) {
        return new InventoryController(
            player,
            new HarvesterInventoryView(this, player),
            stockInventory
        );
    }

    public static class HarvesterXXL extends HarvesterModel {
        public HarvesterXXL(Integer x, Integer y, RootObject rootObject) {
            super(x, y, rootObject);
            stockInventory.setCapacity(INVENTORY_XXL_CAPACITY);
        }
    }
}

package view;

import java.util.Map;

import model.GameState;
import model.GameState.States;
import model.MachineModel;
import model.PlayerModel;

public class MachineInventoryView extends InventoryView {
    public MachineInventoryView(String title, MachineModel model, PlayerModel player) {
        super(
            title,
            Map.of(
                "Configure", 
                (o)-> GameState.getGameState().pushReplaceState(States.INVENTORY, model.makeConfigInventory((PlayerModel)o))
            ),
            player
        );
    }
}

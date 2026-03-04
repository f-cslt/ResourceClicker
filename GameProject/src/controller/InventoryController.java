package controller;

import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;

import model.GameState;
import model.InventoryModel;
import model.PlayerModel;
import utils.KeyAdapter;
import utils.MouseAdapter;
import view.InventoryView;

/**
 * Il s'agit de la classe qui gère les actions dans l'inventaire. On considère l'inventaire comme un état de jeu.
 * Cela nous permettra plus de flexibilité. Tu peux par exemple sortir de l'état de jeu INVENTORY en cliquant sur la 
 * touche i du clavier. Je n'ai pas implémenter cette classe ni le changement d'état vers cet état INVENTORY mais le
 * principe est le même que pour MENU et PLAYING.
 * 
 */

public class InventoryController extends AbstractController<InventoryModel, InventoryView> implements KeyAdapter, MouseAdapter {
	public static final String ELEMENT_ADD_ITEM_PROPERTY = "AddItem";
	public static final String ELEMENT_REMOVE_ITEM_PROPERTY = "RemoveItem";

	private final PlayerModel playerModel;
	
	public InventoryController(PlayerModel playerModel, InventoryView inventoryView, InventoryModel... inventories) {
		super(inventories, new InventoryView[]{ inventoryView });
		this.playerModel = playerModel;

		inventoryView.initUI(playerModel, inventories);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (!(evt.getNewValue() instanceof InventoryModel)) return;
		
		for (InventoryView e : views) {
			e.reloadUI(playerModel, (InventoryModel)evt.getNewValue());
		}
	}

	@Override
	public void update() {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			GameState.getGameState().popState();
		}
	}
}

package controller;

import java.awt.event.KeyEvent;

import model.GameState.States;
import model.GameState;
import model.PlayerModel;
import utils.KeyAdapter;
import utils.MouseAdapter;
import view.PlayerInventoryView;
import view.PlayerView;

public class PlayerController extends AbstractController<PlayerModel, PlayerView> implements KeyAdapter, MouseAdapter {
	public static final String ELEMENT_MONEY_PROPERTY = "Money";
	
	public PlayerController(PlayerModel playerModel, PlayerView playerView) {
		super(playerModel, playerView);
	}

	@Override
	public void update() {
		getFirstModel().update();
	}

	private void showPlayerInventory() {
		PlayerModel player = getFirstModel();
		InventoryController controller = 
			new InventoryController(
				player,
				new PlayerInventoryView(),
				player.getInventory(),
				player.getCraftInventory()
			);

			GameState.getGameState().pushState(States.INVENTORY, controller);
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
			/*
			 * Mouvements du joueur
			 */
	        case KeyEvent.VK_DOWN: 
	        	getFirstModel().setDown(true); 
	        	break;
	        case KeyEvent.VK_UP: 
	        	getFirstModel().setUp(true);   
	        	break;
	        case KeyEvent.VK_LEFT:  
	        	getFirstModel().setLeft(true);
	        	break;
	        case KeyEvent.VK_RIGHT:
	        	getFirstModel().setRight(true);
	        	break;
			case KeyEvent.VK_SPACE:
				showPlayerInventory();	
				break;
	   	        
	        /*
	         * Autres touches potentielles ?	
	         */
	        default:
	        	break;
		}		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
	        case KeyEvent.VK_DOWN:  getFirstModel().setDown(false);  break;
	        case KeyEvent.VK_UP:    getFirstModel().setUp(false);    break;
	        case KeyEvent.VK_LEFT:  getFirstModel().setLeft(false);  break;
	        case KeyEvent.VK_RIGHT: getFirstModel().setRight(false); break;
	        default:
	        	break;
    	}	
	}
}

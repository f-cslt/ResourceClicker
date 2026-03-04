package controller;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import model.GameModel;
import model.GameState.States;
import utils.KeyAdapter;
import utils.MouseAdapter;
import view.GameView;


/**
 * Cette classe contrôle les actions en jeu. Elle sera plus conséquente que MenuController.
 * Elle modifie le model en fonction des entrées par le joueur.
 * 
 */
public class PlayingController extends AbstractController<GameModel, GameView> implements KeyAdapter, MouseAdapter {

	private GameModel gameModel;
	private GameView gameView;
	private PlayerController playerController;
	private MapController mapController;
	
	public PlayingController(GameModel gameModel, GameView gameView) {
		super(gameModel, gameView);
		
		this.gameModel = gameModel;
		this.gameView = gameView;
		
		this.playerController = new PlayerController(gameModel.getPlayerModel(), gameView.getPlayerView());
		this.mapController = new MapController(gameModel.getMap(), gameView.getMapView(), gameModel.getPlayerModel());
	}
	
	/**
	 * Méthode update appellé par GameStateController lorsque l'état de jeu est PLAYING.
	 * On update tout les models pour l'instant. Plus tard on updatera peut-être juste les models 
	 * voulues, je pense notamment à l'appel de update dans InventoryController par exemple qui
	 * devra peut-être éviter d'update certains models.
	 */
	@Override
	public void update() {
		playerController.update();
		mapController.update();
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
        /*
         * Ici on peut voir que la PlayingController est utile. En effet, changer le gamestate
         * ne releve pas de PlayerController ou de MapController. 
         */
		//Ouvrir le menu de jeu
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) 
			gameModel.getGameState().pushState(States.MENU);
		else {
			playerController.keyPressed(e);
			mapController.keyPressed(e);
		}	
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		playerController.keyReleased(e);
		mapController.keyReleased(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		playerController.mouseMoved(e);
		mapController.mouseMoved(e);	
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		playerController.mouseClicked(e);
		mapController.mouseClicked(e);
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		playerController.mouseDragged(e);
		mapController.mouseDragged(e);
	}
}

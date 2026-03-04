package view;

import java.awt.Graphics2D;
import java.beans.PropertyChangeEvent;
import model.GameModel;

/**
 * Classe principale des vues. Toutes les vues secondaires sont instanciées par
 * cette classe. Une vue doit avoir accès au model et seulement au model. Un
 * controller a accès à la vue et au model et le model n'a accès à aucun des
 * deux.
 */
public class GameView extends AbstractView {

	private final GameModel gameModel;
	private final PlayerView playerView;
	private final MapView mapView;
	private final MenuView menuView;
	
	public GameView(GameModel gameModel) {
		this.gameModel = gameModel;
		this.playerView = new PlayerView(gameModel.getPlayerModel());
		this.mapView = new MapView(gameModel.getMap());
		this.menuView = new MenuView(gameModel.getMenuModel());
		
	}

	/**
	 * Dans cette méthode on décide ce qu'on doit dessiner par rapport à l'état de
	 * jeu dans lequel on se trouve. La méthode est appellé par paintComponent de
	 * GamePanelView qui est appellé par draw() de GameController toutes les X
	 * millisecondes.
	 */
	@Override
	public void draw(Graphics2D g2) {
		// Dessiner la map avant le joueur sinon le joueur sera caché.
		mapView.draw(g2);
		playerView.draw(g2);
		
		switch (gameModel.getGameState().getState()) {
		case SETTINGS_MENU:
		case EXIT_MENU:
		case MENU:
			/*
			 * Dans le cas du Menu je pense qu'on veut un overlay qui se dessine par dessus
			 * le jeu qui serait mit en pause.
			 */
			menuView.draw(g2);
			break;
		default:
			break;
		}
	}

	public MapView getMapView() {
		return mapView;
	}

	public PlayerView getPlayerView() {
		return playerView;
	}

	public GameModel getGameModel() {
		return gameModel;
	}

	public MenuView getMenuView() {
		return menuView;
	}

	@Override
	public void modelPropertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub

	}
}

package model;

/* La classe GameModel est le conteneur principale de tous les model.
 * Elle sera donné au conteneur principal des vues GameView.
 */

public class GameModel extends AbstractModel {

	private PlayerModel player;
	private MapModel map;

	// Model qui stocke l'état de jeu
	private GameState gameState;
	private MenuModel menuModel;

	public GameModel() {
		this.map = new MapModel();
		this.player = new PlayerModel();
		this.gameState = new GameState();
		this.menuModel = new MenuModel();
	}

	public PlayerModel getPlayerModel() {
		return this.player;
	}

	public MapModel getMap() {
		return map;
	}

	public GameState getGameState() {
		return gameState;
	}

	public MenuModel getMenuModel() {
		return menuModel;
	}

}

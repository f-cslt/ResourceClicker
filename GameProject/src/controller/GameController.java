package controller;

import model.GameModel;
import view.GamePanelView;
import view.GameWindow;

public class GameController implements Runnable {

	private final GameModel gameModel;
	private final GamePanelView gamePanelView;
	private Thread gameThread;
	private static final int UPS = 160;
	
	// Non final car c'est un paramètre qu'on pourra changer possiblement dans le menu.
	private static int FPS = 60;
	
	/** Il s'agit d'un contrôleur secondaire qui délègue les actions aux états de jeu.
	 *  En réalité on pourrait effectuer son travail dans la classe GameController.
	 *  Cependant séparer les tâches de cette façon rend plus propre le rôle de chaque 
	 *  classe. Cette classe s'occupe plutôt de gérer la boucle de jeu et sert d'entrée dans
	 *  le programme.
	 * 
	 */
	private final GameStateController gameStateController;
	

	public GameController() {
		this.gameModel 			= new GameModel();
		this.gamePanelView 			= new GamePanelView(gameModel);
		
		/* J'ai séparé GamePanelView et GameView. GameView gère la vue principale du jeu et GamePanelView
		 * est juste la classe étendant JPanel.
		 */
		this.gameStateController = new GameStateController(gameModel, gamePanelView.getGameView());
		
		gamePanelView.addKeyListener(gameStateController);
		gamePanelView.addMouseListener(gameStateController);
		gamePanelView.addMouseMotionListener(gameStateController);
		new GameWindow(gamePanelView);
		startGameThread();
	}

	private void startGameThread() {
		this.gameThread = new Thread(this);
		this.gameThread.start();
	}

	private void update() {
		// La classe GameStateController décidera selon l'état de jeu quels composants devront être update
		gameStateController.update();
	}

	private void draw() {
		gamePanelView.repaint();
	}

	@Override
	public void run() {

		/*
		 * UPS sera une constante pour mettre à jour la logique de notre jeu. Tandis que
		 * FPS peut-être changé par le joueur. On sépare la gestion de la logique de la
		 * vue.
		 */

		double frameInterval = 1000000000.0 / FPS;
		double updateInterval = 1000000000.0 / UPS;

		// On utilise des nano secondes pour être précis
		long oneSecondCheck = System.currentTimeMillis(); // Check si une seconde s'est écoulé
		long lastTime = System.nanoTime();
		long currentTime;
		// Compteur du nombre de UPS et de FPS
		int frames = 0;
		int updates = 0;

		/*
		 * Ces variables servent à stocker le temps "en trop". Par exemple s'il s'est
		 * écoulé 1.08 secondes alors on va stocker ces 0.08 secondes et quand elles
		 * atteindront 1 seconde on fera une update.
		 */

		double deltaUpdate = 0;
		double deltaFrame = 0;

		while (gameThread != null) {

			currentTime = System.nanoTime();

			deltaUpdate += (currentTime - lastTime) / updateInterval;
			deltaFrame += (currentTime - lastTime) / frameInterval;
			lastTime = currentTime;

			// On update la logique de jeu
			if (deltaUpdate >= 1) {
				update();
				updates++;
				deltaUpdate--;
			}

			// On update la vue
			if (deltaFrame >= 1) {
				draw();
				frames++;
				deltaFrame--;
			}

			if (System.currentTimeMillis() - oneSecondCheck >= 1000) {
				oneSecondCheck = System.currentTimeMillis();
				System.out.println("FPS = " + frames + "  UPS = " + updates);
				frames = 0;
				updates = 0;
			}
		}

	}

}

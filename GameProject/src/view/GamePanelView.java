package view;

import static utils.GameConstants.TilesConstants.*;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import javax.swing.JPanel;
import model.GameModel;

/**
 * Cette classe est le Panel du jeu. J'ai dû faire diviser le travail entre
 * GamePanelView et GameView car je dois hériter à la fois de JPanel et de
 * AbstractView. GamePanelView crée alors juste un Panel et délègue les
 * décisions de dessin à Gameview qui gère toutes les vues. On se servira donc
 * de GameView dans l'intégralité du code et non de GamePanelView qui est
 * instancié au départ dans GameController.
 *
 */

public class GamePanelView extends JPanel {
	private static GamePanelView gamePanelView;

	private GameView gameView;

	public GamePanelView(GameModel gameModel) {

		this.gameView = new GameView(gameModel);

		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setDoubleBuffered(true);
		this.setFocusable(true);
		this.setLayout(new GridBagLayout());

		gamePanelView = this;
	}

	public void paintComponent(Graphics g) {
		// Très important : cette simple ligne evite les lags (essaie de l'enlever tu
		// comprendras).
		Toolkit.getDefaultToolkit().sync();

		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		/*
		 * GameView décide de ce qui sera dessiné par rapport à l'état de jeu stocké
		 * dans le model.
		 */
		gameView.draw(g2);
	}

	public GameView getGameView() {
		return gameView;
	}

	public static GamePanelView getGamePanelView() {
		return gamePanelView;
	}
}

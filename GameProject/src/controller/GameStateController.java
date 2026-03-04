package controller;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.util.EmptyStackException;
import java.util.Stack;

import model.GameModel;
import model.GameState;
import utils.KeyAdapter;
import utils.MouseAdapter;
import view.DummyView;
import view.GamePanelView;
import view.GameView;

/** Classe visant à délèguer les actions selon l'état de jeu.
 *  GameStateController agit comme un écouteur de clavier et souris.
 *  A chaque état de jeu on ajoutera un controller dedié (à part pour exit)
 *  qui nous permettra de diviser les tâches.
 * 
 */

public class GameStateController  extends AbstractController<GameState, DummyView> implements KeyAdapter, MouseAdapter {
	public static final String ELEMENT_PUSH_STATE_EVENT = "PushState";
	public static final String ELEMENT_POP_STATE_EVENT = "PopState";
	
	private final MenuController menuController;
	private final PlayingController playingController;

	@SuppressWarnings("rawtypes")
	private final Stack<AbstractController> controllerStack;
	// CraftMenuController, etc

	public GameStateController(GameModel gameModel, GameView gameView) {
		super(gameModel.getGameState(), new DummyView());

		// On donne à tout contrôleur un model global et une vue globale 
		this.playingController = new PlayingController(gameModel, gameView);
		this.menuController = new MenuController(gameModel, gameView);
		this.controllerStack = new Stack<>();

		// init stack with the current gamestate
		pushController(null);
	}

	/** update est appelé par le contrôleur principale de la boucle de jeu.
	 * La méthode décide quel contrôleur doit éxécuter sa méthode update selon le gameState.
	 */
	@Override
	public void update() {
		// TODO: keep updating background tasks like harvest token
		controllerStack.peek().update();
	}

	private void pushController(Object userData) {
		@SuppressWarnings("rawtypes")
		AbstractController selectedController;

		switch (getFirstModel().getState()) {
			case PLAYING:
				selectedController = playingController;
				break;
			case SETTINGS_MENU:
			case EXIT_MENU:
			case MENU:
				selectedController = menuController;
				break;
			case INVENTORY:
			case CUSTOM:
				selectedController = (AbstractController<?, ?>)userData;
				break;
			default:
				return;
		}

		controllerStack.push(selectedController);
		selectedController.activate();
	}

	private void popController() {
		try {
			@SuppressWarnings("rawtypes")
			AbstractController controller = controllerStack.pop();
			controller.close();
		} catch (EmptyStackException e) {
			GamePanelView.getGamePanelView().removeAll();
			e.printStackTrace();

			System.err.println("HINT: Maybe you forgot to remove a change property listener");
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		switch (evt.getPropertyName()) {
			case ELEMENT_PUSH_STATE_EVENT:
				pushController(evt.getNewValue());
				break;
			case ELEMENT_POP_STATE_EVENT:
				popController();
			default:
				break;
		}
	}
	
	void delegateUserInputHandling(String funcName, InputEvent evt) {	    
	    invokeUserInputHandling(controllerStack.peek(), funcName, evt);
	}


	@Override
	public void keyPressed(KeyEvent e) {
		delegateUserInputHandling("keyPressed", e);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		delegateUserInputHandling("keyReleased", e);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		delegateUserInputHandling("mouseDragged", e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		delegateUserInputHandling("mouseMoved", e);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		delegateUserInputHandling("mouseClicked", e);
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		delegateUserInputHandling("mouseReleased", e);
	}
	
	/*
	 * Un tout petit peu plus probable qu'on se serve de cette méthode comparé aux méthodes ci-dessous
	 */

	@Override
	public void mousePressed(MouseEvent e) {
		delegateUserInputHandling("mousePressed", e);
	}
}

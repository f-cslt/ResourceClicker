package model;

import java.util.Stack;
import controller.GameStateController;

/**
 * La classe GameState est un model qui stock l'état de jeu actuel du jeu.
 * On considère que le jeu est de base dans l'état de jeu PLAYING.
 */
public class GameState extends AbstractModel {
	private static GameState gameState;
	
	private Stack<States> stateStack; // état de jeu dans lequel on se trouve actuellemen
	private Object userData;
	
	public GameState() {
		this.stateStack = new Stack<>(); 
		this.stateStack.push(States.PLAYING);	// état de base

		gameState = this;
	}

	public static GameState getGameState() {
		return gameState;
	}
	
	public States getState() {
		return this.stateStack.peek();
	}

	public Object getUserData() {
		return this.userData;
	}
	
	public void pushState(States state) {
		pushState(state, null);
	}

	public void pushState(States state, Object userData) {
		this.stateStack.push(state);
		this.userData = userData;
		firePropertyChange(GameStateController.ELEMENT_PUSH_STATE_EVENT, state, userData);
	}

	public void pushReplaceState(States state) { 
		pushReplaceState(state, null);
	}

	public void pushReplaceState(States state, Object userData) {
		popState();
		pushState(state, userData);
	}

	public void popState() {
		this.stateStack.pop();
		firePropertyChange(GameStateController.ELEMENT_POP_STATE_EVENT, null, null);
	}
	
	/*
	 * Enum qui contient tout les états de jeu possible.
	 */
	public enum States{
		
		MENU, 
		PLAYING,
		INVENTORY,
		CUSTOM,
		EXIT_MENU,
		SETTINGS_MENU
		
	}
}

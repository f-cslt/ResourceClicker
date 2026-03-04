package model;

import static utils.GameConstants.TilesConstants.*;
import static utils.GameConstants.SCALE;
import static utils.GameConstants.UIConstants.MenuButtons.*;
import static utils.FileHandler.*;
import java.awt.image.BufferedImage;

/**
 * MenuModel est la classe qui stocke les données du menu à afficher au joueur
 * lorsqu'il appuie sur ESC. Cette classe est composé de boutons (GameButton) et
 * les initialise aux positions choisis.
 */
public class MenuModel extends AbstractModel {

	private GameButton[] buttonModels;
	private GameButton[] exitButtonModels;
	private GameButton[] settingsButtonModels;
	private GameState gameState;
	private BufferedImage exitMenuSentence;

	public MenuModel() {
		this.buttonModels = new GameButton[4]; // 4 Boutons dans le menu.
		this.exitButtonModels = new GameButton[2];
		this.settingsButtonModels = new GameButton[2];
		this.gameState = GameState.getGameState();
		exitMenuSentence = getSpriteSheet(EXIT_MENU_BACKGROUND);
		loadButtonModels();
	}

	private void loadButtonModels() {
		/*
		 * Disposition au hasard des boutons pour l'instant. la position y est à améliorer.
		 */
		buttonModels[0] = new GameButton(SCREEN_WIDTH / 2, 150, LOAD_BUTTON, 2);
		buttonModels[1] = new GameButton(SCREEN_WIDTH / 2, 230, NEWGAME_BUTTON, 2);
		buttonModels[2] = new GameButton(SCREEN_WIDTH / 2, 310, SETTINGS_BUTTON, 2);
		buttonModels[3] = new GameButton(SCREEN_WIDTH / 2, 390, EXIT_BUTTON, 2);
		
		exitButtonModels[0] = new GameButton(SCREEN_WIDTH/2 - BUTTON_WIDTH, SCREEN_HEIGHT/2 , EXIT_YES_BUTTON, 2);
		exitButtonModels[1] = new GameButton(SCREEN_WIDTH/2 + BUTTON_WIDTH, SCREEN_HEIGHT/2, EXIT_NO_BUTTON, 2);

		settingsButtonModels[0] = new GameButton(SCREEN_WIDTH / 2, SCREEN_HEIGHT/2 - SETTINGS_HEIGHT/2, SOUND_ON_BUTTON, 2);
		settingsButtonModels[1] = new GameButton(SCREEN_WIDTH / 2, SCREEN_HEIGHT/2 + SETTINGS_HEIGHT/2, SOUND_OFF_BUTTON, 2);
	}

	/**
	 * Update the buttons
	 */
	public void update() {
		switch(gameState.getState()) {
		case MENU:
			updateMenuButtons();
			break;
		case EXIT_MENU:
			updateExitButtons();
			break;
		case SETTINGS_MENU:
			updateSettingsButtons();
			break;
		default:
			break;
		}	
	}
	
	private void updateSettingsButtons() {
		for (GameButton buttons : settingsButtonModels) {
			buttons.update();
		}
	}

	public void updateMenuButtons() {
		for (GameButton buttons : buttonModels) {
			buttons.update();
		}
	}
	
	public void updateExitButtons() {
		for (GameButton buttons : exitButtonModels) {
			buttons.update();
		}
	}

	public GameButton[] getButtonModels() {
		return buttonModels;
	}
	
	public GameButton[] getExitButtonModels() {
		return exitButtonModels;
	}

	public GameState getGameState() {
		return gameState;
	}
	
	public BufferedImage getExitMenuSentence() {
		return exitMenuSentence;
	}

	public GameButton[] getSettingsButtonModels() {
		return settingsButtonModels;
	}
}

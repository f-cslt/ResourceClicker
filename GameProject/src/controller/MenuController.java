package controller;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import model.GameButton;
import model.GameModel;
import model.GameState;
import model.MenuModel;
import utils.KeyAdapter;
import utils.MouseAdapter;
import view.GameView;
import view.MenuView;

import static utils.GameConstants.UIConstants.MenuButtons.*;
import static utils.FileHandler.clearDataFromSaveFile;

/**
 * La classe MenuController nous permettra de detecter les entrées utilisateur sur les boutons du menu 
 * ou sur les touches du clavier. 
 * 
 */
public class MenuController extends AbstractController<GameModel,GameView> implements KeyAdapter, MouseAdapter {

	private GameModel gameModel;
	private GameView gameView;
	private MenuModel menuModel;
	private MenuView menuView;
	private GameState gameState;
	
	public MenuController(GameModel gameModel, GameView gameView) {
		super(gameModel, gameView);
		this.gameModel = gameModel;
		this.gameView = gameView;
		this.menuModel = gameModel.getMenuModel();
		this.menuView = gameView.getMenuView();
		this.gameState = gameModel.getGameState();
	}
	
	@Override
	public void update() {
		menuModel.update();
	}
	
	private boolean mouseIsInHitbox(MouseEvent e, GameButton button) {
		return button.getHitbox().contains(e.getPoint());
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		/*
		 * Appuyer sur Echap nous sort du menu. On aura probablement aussi un bouton cliquable
		 * à la souris pour cette action.
		 */
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			gameModel.getGameState().popState();
		}
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		switch(gameState.getState()) {
		case SETTINGS_MENU:
			// Simple reset du booléen pour chaque bouton
			for(GameButton button : menuModel.getSettingsButtonModels()) {
				button.setMouseOver(false);
			}
			/*
			 * On regarde pour chaque bouton du menu si notre curseur de souris est sur le bouton.
			 * Si c'est le cas alors on met à true le booléen.
			 */
			for(GameButton button : menuModel.getSettingsButtonModels()) {
				if(mouseIsInHitbox(e, button)) {
					button.setMouseOver(true);
					break;
				}
			}
			break;
		case EXIT_MENU:
			// Simple reset du booléen pour chaque bouton
			for(GameButton button : menuModel.getExitButtonModels()) {
				button.setMouseOver(false);
			}
			/*
			 * On regarde pour chaque bouton du menu si notre curseur de souris est sur le bouton.
			 * Si c'est le cas alors on met à true le booléen.
			 */
			for(GameButton button : menuModel.getExitButtonModels()) {
				if(mouseIsInHitbox(e, button)) {
					button.setMouseOver(true);
					break;
				}
			}
			break;
		case MENU:
			// Simple reset du booléen pour chaque bouton
				for(GameButton button : menuModel.getButtonModels()) {
					button.setMouseOver(false);
				}
				/*
				 * On regarde pour chaque bouton du menu si notre curseur de souris est sur le bouton.
				 * Si c'est le cas alors on met à true le booléen.
				 */
				for(GameButton button : menuModel.getButtonModels()) {
					if(mouseIsInHitbox(e, button)) {
						button.setMouseOver(true);
						break;
					}
				}
				break;
		default:
			break;
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		switch(gameState.getState()) {
		case SETTINGS_MENU:
			for(GameButton button : menuModel.getSettingsButtonModels()) {
				if(mouseIsInHitbox(e, button)) {
					button.setMousePressed(true);
				}
			}
			break;
		case EXIT_MENU:
			for(GameButton button : menuModel.getExitButtonModels()) {
				if(mouseIsInHitbox(e, button)) {
					button.setMousePressed(true);
				}
			}
			break;
		case MENU:
			for(GameButton button : menuModel.getButtonModels()) {
				if(mouseIsInHitbox(e, button)) {
					button.setMousePressed(true);
				}
			}
			break;
		default:
			break;
		}
	}


	@Override
	public void mouseReleased(MouseEvent e) {
		switch (gameState.getState()) {
		case SETTINGS_MENU:
			for(GameButton button : menuModel.getSettingsButtonModels()) {
				if(mouseIsInHitbox(e, button)) {
					if(button.isMousePressed()) {
						switch (button.getButtonType()) {
						case SOUND_ON_BUTTON:
							System.out.println("SOUND IS ON");
							break;
						case SOUND_OFF_BUTTON:
							System.out.println("SOUND IS OFF");
							break;
						default:
							break;
						}
					}
				}
			}
			break;
		/*
		 * Si on a appuyé sur le bouton EXIT dans le menu.
		 */
		case EXIT_MENU:
			for(GameButton button : menuModel.getExitButtonModels()) {
				if(mouseIsInHitbox(e, button)) {
					if(button.isMousePressed()) {
						
						switch(button.getButtonType()) {
						
						case EXIT_YES_BUTTON:
							// On efface d'abord l'ancienne sauvegarde en l'écrasant.
							clearDataFromSaveFile();
							// On enregistre les data des models dans le fichier de sauvegarde
							gameModel.getPlayerModel().saveData();
							gameModel.getMap().saveData();
							gameModel.getPlayerModel().getInventory().saveData();
							System.exit(0);
							break;
						case EXIT_NO_BUTTON:
							System.exit(0);
							break;
						default:
							break;
						}
					}
				}
			}
			break;
			
		/*
		 * Pour chaque bouton du menu, on check si le lâché du bouton de souris
		 * a été fait sur le bouton. Puis on vérifie si on a déjà appuyé sur le bouton.
		 * Finalement on décide les actions à faire selon le type de bouton.
		 */
		case MENU:
			for(GameButton button : menuModel.getButtonModels()) {
				if(mouseIsInHitbox(e, button)) {
					if(button.isMousePressed()) {
						
						switch (button.getButtonType()) {
						
						case LOAD_BUTTON:
							//On charge les données de sauvegarde du fichier de sauvegarde dans nos model.
							gameModel.getPlayerModel().loadData();
							gameModel.getMap().loadData();
							gameModel.getPlayerModel().getInventory().loadData();
							break;
						case NEWGAME_BUTTON:
							gameModel.getGameState().popState();		
							break;
						case SETTINGS_BUTTON:
							gameModel.getGameState().pushState(GameState.States.SETTINGS_MENU);
							break;
						case EXIT_BUTTON:
							gameModel.getGameState().pushState(GameState.States.EXIT_MENU);
						default:
							break;
						}
					}
				}
			}
			break;
		default:
			break;
		}
	}
}

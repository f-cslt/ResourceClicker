package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.beans.PropertyChangeEvent;

import javax.swing.JButton;
import javax.swing.JPanel;

import model.GameState;
import model.MenuModel;
import static utils.GameConstants.TilesConstants.*;
import static utils.GameConstants.UIConstants.MenuButtons.*;

/**
 * MenuView se charge de dessiner le menu.
 */
public class MenuView extends AbstractView {

	private MenuModel menuModel;
	/*
	 * On a besoin des vues de tout les GameButton
	 */
	private ButtonView[] buttonViews;
	private ButtonView[] exitButtonViews;
	private ButtonView[] settingsButtonViews;

	public MenuView(MenuModel menuModel) {
		this.menuModel = menuModel;

		// On a autant de vue que de bouton dans le menu
		this.buttonViews = new ButtonView[menuModel.getButtonModels().length];
		//Les vues des boutons doivent être liés aux models des boutons crées dans MenuModel
		for (int i = 0; i < menuModel.getButtonModels().length; i++) {
			buttonViews[i] = new ButtonView(menuModel.getButtonModels()[i]);
		}
		
		this.exitButtonViews = new ButtonView[menuModel.getExitButtonModels().length];
		for (int i = 0; i < menuModel.getExitButtonModels().length; i++) {
			exitButtonViews[i] = new ButtonView(menuModel.getExitButtonModels()[i]);
		}
		
		this.settingsButtonViews = new ButtonView[menuModel.getSettingsButtonModels().length];
		for (int i = 0; i < menuModel.getSettingsButtonModels().length; i++) {
			settingsButtonViews[i] = new ButtonView(menuModel.getSettingsButtonModels()[i]);
		}

	}
	
	

	/**
	 * Dessine le menu avec ses boutons.
	 */
	@Override
	public void draw(Graphics2D g) {
		switch(menuModel.getGameState().getState()) {
		case SETTINGS_MENU:
			drawSettingsButtons(g);
			break;
		case MENU:
			drawMenu(g);
			break;
		case EXIT_MENU:
			drawExitButtons(g);
			break;
		default:
			break;
		}
	}
	
	private void drawSettingsButtons(Graphics2D g) {
		g.setColor(new Color(0, 0, 0, 220));
		g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
		for(int i=0 ; i < menuModel.getSettingsButtonModels().length ; i++) {
			settingsButtonViews[i].draw(g);
		}
	}
	
	/**
	 * Dessine les boutons pour sauvegarder ou non la partie quand on a appuyé
	 * sur le bouton EXIT de menu.
	 */
	private void drawExitButtons(Graphics2D g) {
		g.setColor(new Color(0, 0, 0, 220));
		g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
		g.drawImage(
				menuModel.getExitMenuSentence(),
				SCREEN_WIDTH/2 - SAVE_WIDTH/2,
				SCREEN_HEIGHT/2 - 100,
				SAVE_WIDTH,
				SAVE_HEIGHT, 
				null
				);
		for(int i=0 ; i < menuModel.getExitButtonModels().length ; i++) {
			exitButtonViews[i].draw(g);
		}
	}
	
	/**
	 * Dessine juste le menu principale avec ses 4 boutons.
	 */
	private void drawMenu(Graphics2D g) {
		/*
		 * Léger voile semi-transparent en arrière plan
		 */
		g.setColor(new Color(0, 0, 0, 210));
		g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

		// Dessin de tout les boutons du menu
		for (int i = 0; i < menuModel.getButtonModels().length; i++) {
			buttonViews[i].draw(g);
		}
	}

	@Override
	public void modelPropertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub

	}
}

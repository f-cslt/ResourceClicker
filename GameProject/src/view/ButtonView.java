package view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.beans.PropertyChangeEvent;
import model.GameButton;

import static utils.GameConstants.UIConstants.MenuButtons.*;

/**
 * Cette classe a pour but d'afficher un bouton et un seul bouton. Elle est très générale et est
 * voué à être réutilisé. Si ce n'est pas le cas dans le futur on pourra essayer de la rendre interne
 * à MenuView.
 */
public class ButtonView extends AbstractView{

	private GameButton button;
	
	public ButtonView(GameButton button) {
		this.button = button;
	}

	@Override
	public void draw(Graphics2D g) {
		if(button.getButtonType() < 6) {
			drawRectButton(g);
		}else {
			drawSquareButton(g);
		}
		
		/*
		 * Dessin de la hitbox des boutons.
		 */
		
//		g.setColor(Color.RED);
//		g.draw(button.getHitbox());

	}
	
	private void drawRectButton(Graphics2D g) {
		g.drawImage(
				button.getBufferedImages()[button.getTick()], 
				button.getX() - BUTTON_WIDTH/2, 
				button.getY(),
				BUTTON_WIDTH,
				BUTTON_HEIGHT,
				null
			);
	}
	
	private void drawSquareButton(Graphics2D g) {
		g.drawImage(
				button.getBufferedImages()[button.getTick()], 
				button.getX() - SETTINGS_WIDTH/2, 
				button.getY(),
				SETTINGS_WIDTH,
				SETTINGS_HEIGHT,
				null
			);
	}
	
	@Override
	public void modelPropertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub
		
	}
}

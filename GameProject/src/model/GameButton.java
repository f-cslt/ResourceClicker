package model;

import static utils.FileHandler.*;
import static utils.GameConstants.UIConstants.MenuButtons.*;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * Classe qui crée un bouton. J'ai pensé à la rendre interne à MenuModel mais de cette manière
 * cette classe sera réutilisable autre part et non pas seulement pour le menu.
 */
public class GameButton {
	private int x, y;								// Position du bouton sur l'écran
	private int buttonType;							// Position du sprite du bouton dans l'atlas	
	private int animNumber;							// Nombre d'animation que le bouton possède
	private int tick; 								// Nous donne le numéro du sprite à afficher par rapport à l'animation
	private BufferedImage[] bufferedImages;			// Images du bouton (2 images par bouton pour le menu)
	private Rectangle hitbox;						// hitbox du bouton
	private boolean mouseOver, mousePressed;		// booleens qui verifient si la souris est/a appuyé sur le bouton
		
	public GameButton(int x, int y, int buttonType, int animNumber) {
		this.x = x;
		this.y = y;
		this.buttonType = buttonType;
		this.animNumber = animNumber;
		
		if(buttonType < 6) {
			loadRectBufferedImages();
			initRectHitbox();
		}
		else {
			loadSquareBufferedImages();
			initSquareHitbox();
		}
		
	}
	
	public void update() {
		tick = 0;	// Par défaut le bouton n'est pas enclenché donc l'image est celle de base
		
		// On crée une animation si la souris est passée au dessus du bouton
		if(mouseOver) {
			tick = 1;
		}
	}

	/**
	 * Fonction qui initialise la hitbox de chaque bouton.
	 */
	private void initRectHitbox() {
		this.hitbox = new Rectangle(x - BUTTON_WIDTH/2, y, BUTTON_WIDTH, BUTTON_HEIGHT);
		
	}
	
	private void initSquareHitbox() {
		this.hitbox = new Rectangle(x - SETTINGS_WIDTH/2, y, SETTINGS_WIDTH, SETTINGS_HEIGHT);
	}
	
	private void loadSquareBufferedImages() {
		this.bufferedImages = new BufferedImage[animNumber];
		BufferedImage img = getSpriteSheet(SETTINGS_BUTTON_ATLAS);
		
		for(int i=0 ; i < bufferedImages.length ; i++) {
			bufferedImages[i] = img.getSubimage(
					i * SETTINGS_WIDTH_DEFAULT,
					this.buttonType%2 * SETTINGS_HEIGHT_DEFAULT,	// Trick peu recommandable
					SETTINGS_WIDTH_DEFAULT,
					SETTINGS_HEIGHT_DEFAULT
					);
		}
	}

	/**
	 * Fonction qui charge nos images de boutons.
	 */
	private void loadRectBufferedImages() {
		this.bufferedImages = new BufferedImage[animNumber];	// Images de l'animation du bouton
		BufferedImage img = getSpriteSheet(MENU_BUTTONS_ATLAS);
		
		for(int i=0 ; i < bufferedImages.length ; i++) {
			bufferedImages[i] = img.getSubimage(
					i * BUTTON_WIDTH_DEFAULT,
					this.buttonType * BUTTON_HEIGHT_DEFAULT,
					BUTTON_WIDTH_DEFAULT,
					BUTTON_HEIGHT_DEFAULT);
		}
		
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getButtonType() {
		return buttonType;
	}

	public int getAnimNumber() {
		return animNumber;
	}

	public BufferedImage[] getBufferedImages() {
		return bufferedImages;
	}

	public Rectangle getHitbox() {
		return hitbox;
	}

	public int getTick() {
		return tick;
	}

	public boolean isMouseOver() {
		return mouseOver;
	}

	public void setMouseOver(boolean mouseOver) {
		this.mouseOver = mouseOver;
	}

	public boolean isMousePressed() {
		return mousePressed;
	}

	public void setMousePressed(boolean mousePressed) {
		this.mousePressed = mousePressed;
	}	
	
}

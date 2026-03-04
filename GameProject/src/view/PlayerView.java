package view;

import java.awt.Graphics2D;
import model.PlayerModel;
import static utils.GameConstants.PlayerConstants.*;

public class PlayerView extends AbstractView {
	private PlayerModel player;

	public PlayerView(PlayerModel player) {
		this.player = player;
	}

	@Override
	public void draw(Graphics2D g) {

		// On a une dimension de 192x320, avec 6 sprites de largeur et 10 de longueur.
		// 192/6 = 320/10 = 32 donc chaque sprite fait 32x32 pixels.
		// On fait x3 pour l'avoir en plus grand donc 96 pixels
		if (player.isFlipImage()) {
			// voir stackOverflow 'flip image with graphics2D' c'était la manière simple de
			// le faire
			g.drawImage(
					player.getAnimations()[player.getAction()][player.getAnimNumber()],
					(int) (player.getX() + PLAYER_SIZE),
					(int) player.getY(), 
					-PLAYER_SIZE, 
					PLAYER_SIZE,
					null
				);

		} else {
			g.drawImage(
					player.getAnimations()[player.getAction()][player.getAnimNumber()],
					(int) player.getX(),
					(int) player.getY(), 
					PLAYER_SIZE,
					PLAYER_SIZE,
					null
				);
		}
	}
}

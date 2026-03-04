package view;

import javax.swing.JFrame;

/* Cette classe s'occupe seulement de la fenêtre du jeu.
 * On y touchera sûrement que très peu mais je trouvais
 * mieux de la séparer comme cela.
 */

public class GameWindow extends JFrame {

	public GameWindow(GamePanelView gamePanelView) {
		this.setTitle("Trouver un nom");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.add(gamePanelView);
		this.pack();
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
}

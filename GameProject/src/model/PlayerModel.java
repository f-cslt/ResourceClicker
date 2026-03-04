package model;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;

import controller.PlayerController;
import model.PrimitiveResource.HarvestMode;
import model.PrimitiveResource.ResourceHarvestToken;
import utils.InventoryUtils;

import static utils.GameConstants.PlayerAnimationConstants.*;
import static utils.FileHandler.*;
import static utils.GameConstants.PlayerConstants.*;
import static utils.GameConstants.SCALE;
import static utils.GameConstants.TilesConstants.*;

public class PlayerModel extends AbstractModel {
	public static final String INVENTORY_RESOURCES_GROUP = "Resources";
	public static final String INVENTORY_CRAFT_GROUP = "Craft";

	private final InventoryModel inventory;
	private final InventoryModel craftInventory;
	private double x, y;
	private double money=20;
	private double speed;
	private int action = IDLE_DOWN;
	private boolean running = false;
	private boolean flipImage = false;
	private ResourceHarvestToken resourceToken;

	/*
	 * ici on a besoin de stocker dans le premier tableau la "ligne" càd à quoi
	 * correspond l'animation Par exemple : aller à gauche, en haut, courir. Et dans
	 * le deuxième tableau on va stocker les animations correspondantes à l'action.
	 */
	private BufferedImage[][] animations;
	private int animFrame, animNumber, animSpeed = 22;

	private boolean down, up, left, right;

	// TODO: Replace above code with below
	// This allows to get rid of running var, and KeyReleased can simply set runDir
	// to null
	// private enum RunDir { DOWN, UP, LEFT, RIGHT }
	// private RunDir runDir;

	public PlayerModel() {
		super();

		inventory = new InventoryModel(INVENTORY_RESOURCES_GROUP);
		craftInventory = new InventoryModel(INVENTORY_CRAFT_GROUP);
		
		InventoryUtils.initInventoryWithAllAvailableResources(inventory, 0);
        InventoryUtils.initInventoryWithAllAvailableRecipies(craftInventory, 0);

		init();
		loadAni();

	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double val) {
		firePropertyChange(PlayerController.ELEMENT_MONEY_PROPERTY, money, val);
		money = val;
	}

	private void init() {
		this.x = MapModel.startPosition.getX();
		this.y = MapModel.startPosition.getY();
		this.speed = 0.50 * SCALE;
	}
	
	public void update() {
		checkPendingResource();

		if (canMove())
			updatePosition();
		updateAnimationFrame();
	}
	
	/**
	 * Charge les données du joueur à partir du fichier du sauvegarde.
	 * Pour l'instant ne charge que les données de position.
	 */
	public void loadData() {
		BufferedReader bufferedReader = getBufferedReader("/sauvegarde.txt");
		String line;
		try {
			while((line = bufferedReader.readLine()) != null) {
				if(line.startsWith("## PLAYER DATA:")) {
					String playerDataLine = bufferedReader.readLine();
					if(playerDataLine != null) {
						String[] parsedData = playerDataLine.split(";");
						if(parsedData.length >= 3) {
							this.x = Double.parseDouble(parsedData[0]);
							this.y = Double.parseDouble(parsedData[1]);
							this.money = Double.parseDouble(parsedData[2]);
						}
						break;
					}
				}
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Enregistre la data du joueur dans le fichier de sauvegarde.
	 */
	public void saveData() {
		writeToSaveFile("## PLAYER DATA:");
		// On doit stocker la position de la case départ (voir sujet)
		writeToSaveFile(MapModel.startPosition.getX() + ";" + MapModel.startPosition.getY() + ";" + money);
	}
	
	private void loadAni() {

		BufferedImage img = getSpriteSheet(PLAYER_SPRITE);
		// On a 10 actions différentes et max 6 animations par action
		animations = new BufferedImage[10][6];

		for (int i = 0; i < animations.length; i++) {
			for (int j = 0; j < animations[i].length; j++) {
				animations[i][j] = img.getSubimage(j * PLAYER_DEFAULT_SIZE, i * PLAYER_DEFAULT_SIZE,
						PLAYER_DEFAULT_SIZE, PLAYER_DEFAULT_SIZE); // Le joueur est en 32x32 pixels
			}

		}
	}

	private void updateAnimationFrame() {
		animFrame++;
		// Si on a update 15 fois ou + il est temps de changer l'image pour créer une
		// animation
		if (animFrame >= animSpeed) {
			animFrame = 0; // reset du compteur
			animNumber++; // on change d'image
			if (animNumber >= GetSpriteCount(action)) { // Chaque action n'a pas le même nombre de sprite
				animNumber = 0; // si l'image est la dernière du tableau on retourne au début (cycle)
			}
		}

	}

	/*
	 * TODO: Implémenter cette fonction d'une autre façon. Peut-être faudrait-il
	 * enlever les booléens. On peut voir juste en dessous en commentaire un essai
	 * pour simplifier cette fonction.
	 * 
	 */

	private void updatePosition() {
		if (up) {
			running = true;
			if(y + 7*SCALE > 0) 
				y -= speed;
			flipImage = false;
			action = RUNNING_UP;
		} else if (down) {
			running = true;
			if(y < SCREEN_HEIGHT - PLAYER_SIZE + 7*SCALE)	// On va dire que 7 est l'offset du joueur
				y += speed;
			flipImage = false;
			action = RUNNING_DOWN;
		} else if (left) {
			running = true;
			if(x + 7*SCALE > 0)
				x -= speed;
			flipImage = true;
			action = RUNNING_LEFT_RIGHT;
		} else if (right) {
			running = true;
			if(x < SCREEN_WIDTH - PLAYER_SIZE + 7*SCALE)
				x += speed;
			flipImage = false;
			action = RUNNING_LEFT_RIGHT;
		} else {
			running = false;
			if (action == RUNNING_DOWN)
				action = IDLE_DOWN;
			if (action == RUNNING_UP)
				action = IDLE_UP;
			if (action == RUNNING_LEFT_RIGHT)
				action = IDLE_LEFT_RIGHT;
			if (action == RUNNING_LEFT_RIGHT)
				action = IDLE_LEFT_RIGHT;

		}

	}

	private void checkPendingResource() {
		if (resourceToken != null && resourceToken.isready()) {
			RootObject resource = resourceToken.harvest();
			inventory.addItem(resource);
			resourceToken = null;

			firePropertyChange(Events.EVENT_NEW_ITEM, INVENTORY_RESOURCES_GROUP, resource);
		}
	}

	private boolean canMove() {
		return resourceToken == null || resourceToken.getHarvestMode() != HarvestMode.BLOCKING;
	}
	
	public InventoryModel getInventory() {
		return inventory;
	}

	public InventoryModel getCraftInventory() {
		return craftInventory;
	}

	public boolean isFlipImage() {
		return flipImage;
	}

	public BufferedImage[][] getAnimations() {
		return animations;
	}

	public int getAction() {
		return action;
	}

	public int getAnimNumber() {
		return animNumber;
	}

	public boolean isDown() {
		return down;
	}

	public void setDown(boolean down) {
		this.down = down;
	}

	public boolean isUp() {
		return up;
	}

	public void setUp(boolean up) {
		this.up = up;
	}

	public boolean isLeft() {
		return left;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public boolean isRight() {
		return right;
	}

	public void setRight(boolean right) {
		this.right = right;
	}

	public void addResourceToken(ResourceHarvestToken token) {
		resourceToken = token;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getSpeed() {
		return speed;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

}

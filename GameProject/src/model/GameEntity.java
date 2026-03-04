package model;

import java.awt.Point;
import static utils.FileHandler.writeToSaveFile;
/**
 * Classe qui servira à instancier un ObjectModel à partir du fichier txt. Elle
 * est sujette à modification, les façons de faire étaient multiple.
 */

public class GameEntity extends AbstractModel implements Cloneable {
	private final RootObject rootObject;
	private boolean walkable;
	private int xpos;
	private int ypos;
	private GameEntityStatus status;

	public enum GameEntityStatus {
		REQUIRES_ACTION,
		BUSY,
		READY,
		NOTHING
	}

	public GameEntity(int x, int y, boolean walkable, RootObject rootObject) {
		this(x, y, walkable, rootObject, GameEntityStatus.NOTHING);
	}

	public GameEntity(int x, int y, boolean walkable, RootObject rootObject, GameEntityStatus status) {
		this.rootObject	= rootObject;
		this.walkable 	= walkable;
		this.xpos 		= x;
		this.ypos 		= y;
		this.status 	= status;
	}

	public void saveData() {
		writeToSaveFile(getSpriteId() + ";" + walkable + ";" + xpos + ";" + ypos);
		
	}

	/**
	 * Gets this game entity's status.
	 * 
	 * @return the status
	 */
	public GameEntityStatus getStatus() {
		return status;
	}

	/**
	 * Sets this game entity's status.
	 * 
	 * @param status the new status.
	 */
	public void setStatus(GameEntityStatus status) {
		this.status = status;
	}

	public boolean canBePlacedAt(int x, int y) {
		return true;
	}

	/**
	 * Returns true if the position was succefully updated.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean setPos(int x, int y) {
		if (!canBePlacedAt(x, y)) return false;

		this.xpos = x;
		this.ypos = y;
		return true;
	}

	/**
	 * Returns a deep copy of this instance with the specified spriteID in place
	 * 
	 * @param spriteID
	 * @return
	 */
	public GameEntity copyWithSpriteID(int spriteID) {
		return new GameEntity(xpos, ypos, walkable, rootObject);
	}

	public RootObject getRootObject() {
		return rootObject;
	}

	/**
	 * Returns sprite id assigned to this entity
	 * 
	 * @return
	 */
	public int getSpriteId() {
		return rootObject.spriteId;
	}

	/**
	 * Returns entity's x position on the map
	 * 
	 * @return
	 */
	public int getX() { 
		return xpos;
	}

	/**
	 * Returns entity's y position on the map
	 * 
	 * @return
	 */
	public int getY() { 
		return ypos; 
	}

	/**
	 * Returns entity's position on the map
	 * 
	 * @return
	 */
	public Point getPos() { 
		return new Point(xpos, ypos); 
	}

	public boolean isWalkable() {
		return walkable;
	}

	/**
	 * Computes the time left until this entity goes into READY status. 
	 * By default, {@link Long#MAX_VALUE} is returned, meaning this entity does not use READY status.
	 * 
	 * @return the time left
	 */
	public long getTimeLeftUntilReady() {
		return Long.MAX_VALUE;
	}

	/**
	 * Gets how long this entity takes to complete its task.
	 * 
	 * @return the time
	 */
	public long getTimeToComplete() {
		return Long.MAX_VALUE;
	}

	/**
	 * Trigger an action that may interact with the specified player.
	 * 
	 * @param player
	 */
	public void triggerAction(PlayerModel player) {}

	/**
	 * This function is to be called on every frame
	 */
	public void update() {}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof GameEntity) {
			GameEntity rhs = (GameEntity)obj;
			return getSpriteId() == rhs.getSpriteId();
		}

		return false;
	}

	@Override
	public GameEntity clone() {
		return new GameEntity(xpos, ypos, walkable, rootObject);
	}
}

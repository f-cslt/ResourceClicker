package view;

import static utils.GameConstants.TilesConstants.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.beans.PropertyChangeEvent;

import model.AtlasSprite;
import model.Events;
import model.GameEntity;
import model.MapModel;

import static utils.HelpFunctions.*;

public class MapView extends AbstractView {
	private final long DEFAULT_CLICK_EFFECT_TIME_IN_MILLI = 150;

	private MapModel map;
	private Point focusedPoint;
	private long clickEffectTimeout = 0;

	public MapView(MapModel map) {
		this.map = map;
	}

	/*
	 * j'aurai voulu ne pas parcourir les matrices deux fois mais pour les objets à
	 * taille > à 1 les tiles se draw par dessus. En soit ça peut rester comme ça
	 * car le jeu va quand même tourner sans difficulté.
	 */
	@Override
	public void draw(Graphics2D g2) {

		// drawObjects sera peut-être déplacé ultérieurement dans une autre vue mais en
		// soit ils font partie de la map.
		drawTiles(g2);
		drawObjects(g2);
	}

	// Handles hover and click graphic overlayes
	private void drawFocus(Graphics2D g2, Point p, AtlasSprite sprite) {
		if (focusedPoint != null && focusedPoint.equals(p)) {
			Color c = g2.getColor();
			int alpha = (clickEffectTimeout < System.currentTimeMillis()) ? 32 : 200;
			g2.setColor(new Color(255, 255, 255, alpha));
			
			g2.fillRect(
				p.x*TILE_SIZE, 
				p.y*TILE_SIZE, 
				TILE_SIZE * sprite.getTilesInWidth(), 
				TILE_SIZE * sprite.getTilesInHeight()
			);

			g2.setColor(c);
		}
	}

	private void drawTiles(Graphics2D g2) {
		for (int i = 0, y = 0; i < map.getMapData().length; i++, y += TILE_SIZE) {
			for (int j = 0, x = 0; j < map.getMapData()[i].length; j++, x += TILE_SIZE) {

				int baseIndex = map.getMapData()[i][j];
				AtlasSprite sprite = map.getGameObject(baseIndex);
				// On draw la couche du dessous (on pourra poser des objets par dessus)
				g2.drawImage(sprite.getImg(), x, y, TILE_SIZE, TILE_SIZE, null);
				drawFocus(g2, new Point(j, i), sprite);
			}
		}
	}

	private void drawObjects(Graphics2D g2) {
		map.getMapObjectData().foreach((e, p) -> {
			if (e == null || e.getSpriteId() == 0) return;
			
			AtlasSprite sprite = map.getGameObject(e.getSpriteId());

			drawObjectStatus(g2, e);

			// Pour les objets faisant une taille de plusieurs tiles on ne veut l'imprimer
			// qu'une fois
			if (sprite.getTilesInWidth() * sprite.getTilesInHeight() == 1 ||
			 	isTopLeftCornerOfMultiTileObject(map.getMapObjectData(), p.x, p.y, e.getSpriteId())) {
				g2.drawImage(
					sprite.getImg(), 
					e.getX()*TILE_SIZE, 
					e.getY()*TILE_SIZE, 
					TILE_SIZE * sprite.getTilesInWidth(), 
					TILE_SIZE * sprite.getTilesInHeight(), 
					null
				);

				/*
				 * TODO: trouver une solution pour ne pas appeller drawFocus dans drawObjects mais plutôt dans draw
				 */
				drawFocus(g2, e.getPos(), sprite);
			}
		});
	}

	private void drawObjectStatus(Graphics2D g2, GameEntity entity) {
		Color c = g2.getColor();

		switch(entity.getStatus()) {
			// TODO: else, set green if ready
			case REQUIRES_ACTION:
				g2.setColor(Color.RED.darker());
				drawStatusIcon(g2, entity);
				break;
			case BUSY:
				double progress = 1-(entity.getTimeLeftUntilReady()/((double)entity.getTimeToComplete()));
				g2.setColor(new Color(141, 141, 141, 200));
				g2.fillRect(entity.getX()*TILE_SIZE, entity.getY()*TILE_SIZE, (int)(TILE_SIZE*progress), (int)(TILE_SIZE*0.1));
				break;
			case READY:
				g2.setColor(Color.GREEN.darker());
				drawStatusIcon(g2, entity);
				break;
			default:
				break;
		}

		g2.setColor(c);
	}

	private void drawStatusIcon(Graphics2D g2, GameEntity entity) {
		g2.fillOval(entity.getX()*TILE_SIZE, entity.getY()*TILE_SIZE, 10, 10);
	}

	@Override 
	public void modelPropertyChange(PropertyChangeEvent evt) {
		switch (evt.getPropertyName()) {
		/*
		 * Les evenement sont stockés dans le model Event qui est voué à être modifié.
		*/
			case Events.EVENT_MAP_AREA_HOVERED:
				focusedPoint = (Point)evt.getNewValue();
				break;
			case Events.EVENT_MAP_AREA_CLICKED:
				clickEffectTimeout = System.currentTimeMillis() + DEFAULT_CLICK_EFFECT_TIME_IN_MILLI;
				break;
		}
	}
}

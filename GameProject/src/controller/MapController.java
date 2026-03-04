package controller;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;

import model.Events;
import model.GameEntity;
import model.GameState;
import model.MapModel;
import model.PlayerModel;
import model.GameState.States;
import utils.KeyAdapter;
import utils.MouseAdapter;
import view.MapView;
import view.PlayerInventoryView;

import static  utils.HelpFunctions.windowToMapCoord;
import static utils.GameConstants.PlayerConstants.*;

public class MapController extends AbstractController<MapModel, MapView> implements KeyAdapter, MouseAdapter {
	private final PlayerModel playerModel;
	
	public MapController(MapModel mapModel, MapView mapView, PlayerModel playerModel) {
		super(mapModel, mapView);

		this.playerModel = playerModel;
	}

	@Override
	public void update() {
		getFirstModel().update();
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
	
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		Point p = windowToMapCoord(e.getX(), e.getY());

		// On accède à n'importe quelle vue à l'aide de gameView
        getFirstView().modelPropertyChange(
            new PropertyChangeEvent(
                this, 
                /*
                 * Les EVENTS peuvent être stockées dans un model sous forme de classe et sous-classe pour chaque
                 * type d'events, ou bien sous forme d'enum toujours dans un model ou dans utils.
                 */
                Events.EVENT_MAP_AREA_HOVERED,
                null, 
                p
            )
        );		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Point p = windowToMapCoord(e.getX(), e.getY());
		GameEntity entity = getFirstModel().getObjectAt(p.x, p.y);
		Point realPlayerPos = new Point(
					(int)playerModel.getX() + PLAYER_SIZE/2,
					(int)playerModel.getY() + PLAYER_SIZE/2
					);
		Point playerPosInTile = windowToMapCoord((int)realPlayerPos.getX(), (int)realPlayerPos.getY());
		
		if (Math.abs(playerPosInTile.x - p.x) > 1 || Math.abs(playerPosInTile.y - p.y) > 1) {
			return;
		}

		getFirstView().modelPropertyChange(
	          new PropertyChangeEvent(
	              this,
	              // EVENT stocké dans un model
	              Events.EVENT_MAP_AREA_CLICKED, 
	              null, 
	              p
	          )
	      );
	
		if (entity.getSpriteId() != 0) {
			assert entity.getPos().equals(p);
			entity.triggerAction(playerModel);
		} else {
			GameState
				.getGameState()
				.pushState(
					States.CUSTOM, 
					new InventoryController(
						playerModel, 
						new PlayerInventoryView(p),
						playerModel.getInventory()
					)
				);
		}
	}
}

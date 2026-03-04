package utils;

import java.util.Map;

import static utils.FileHandler.*;
import static utils.GameConstants.ObjectConstants.*;

import model.AtlasSprite;
import model.FactoryModel;
import model.HarvesterModel;
import model.PrimitiveResource;
import model.Recipe;
import model.Resource;
import model.RootObject;
import model.FactoryModel.FactoryXXL;
import model.HarvesterModel.HarvesterXXL;
import model.InventoryModel.InventoryItem;

/* Classe très importante qui nous donnera toutes les constantes
 * et valeurs utiles du jeu.
 * 
 * Dans un premier temps elle contiendra la taille d'un carreau
 * (tile en anglais) ainsi que des valeurs liés pour pouvoir scale
 * la taille de notre jeu.
 * 
 * Note : la plupart des personnages 2D sont en 16x16, 24x24, ou 32x32 pixels.
 * Je vais mettre par défaut 16 ou 32 qui est plus courant en attendant
 * de trouver un asset.
 */

public class GameConstants {

	// Je déplace SCALE ici pour y avoir accès dans les classes internes.
	public static final int SCALE = 3; // int ou float c'est discutable
	
	public static class PlayerConstants {
		public static final int PLAYER_DEFAULT_SIZE = 32;
		public static final int PLAYER_SIZE = PLAYER_DEFAULT_SIZE * SCALE;
	}
	
	public static class TilesConstants {

		public static final int TILES_DEFAULT_SIZE = 16;
		public static final int TILE_SIZE = TILES_DEFAULT_SIZE * SCALE;
		public static final int TILES_IN_WIDTH = 22;
		public static final int TILES_IN_HEIGHT = 14;
		public static final int SCREEN_WIDTH = TILE_SIZE * TILES_IN_WIDTH;
		public static final int SCREEN_HEIGHT = TILE_SIZE * TILES_IN_HEIGHT;

	}
	
	public static class UIConstants {
		public static class MenuButtons {
			public static final int BUTTON_WIDTH_DEFAULT	 	= 196;
			public static final int BUTTON_HEIGHT_DEFAULT 		= 84;
			public static final int BUTTON_WIDTH = (BUTTON_WIDTH_DEFAULT/4) * SCALE;
			public static final int BUTTON_HEIGHT = (BUTTON_HEIGHT_DEFAULT/4) * SCALE;
			
			public static final int SAVE_WIDTH_DEFAULT = 419;
			public static final int SAVE_HEIGHT_DEFAULT = 141;
			public static final int SAVE_WIDTH = (SAVE_WIDTH_DEFAULT/3) * SCALE;
			public static final int SAVE_HEIGHT = (SAVE_HEIGHT_DEFAULT/3) * SCALE;
			
			public static final int SETTINGS_WIDTH_DEFAULT = 84;
			public static final int SETTINGS_HEIGHT_DEFAULT = 84;
			public static final int SETTINGS_WIDTH = (SETTINGS_WIDTH_DEFAULT/4) * SCALE;
			public static final int SETTINGS_HEIGHT = (SETTINGS_HEIGHT_DEFAULT/4) * SCALE;

			// Position dans l'atlas de chaque bouton. Même principe que PlayerAnimationConstants.
			public static final int LOAD_BUTTON			= 0;
			public static final int NEWGAME_BUTTON		= 1;
			public static final int SETTINGS_BUTTON		= 2;
			public static final int EXIT_BUTTON 		= 3;
			public static final int EXIT_YES_BUTTON		= 4;
			public static final int EXIT_NO_BUTTON		= 5;
			public static final int SOUND_ON_BUTTON		= 6;
			public static final int SOUND_OFF_BUTTON	= 7;

		}
	}

	public static class SpriteIdMappingObjects {
		public final AtlasSprite atlasSprite;
		public final RootObject rootObject;

		public SpriteIdMappingObjects(AtlasSprite atlasSprite, RootObject rootObject) {
			this.atlasSprite = atlasSprite;
			this.rootObject = rootObject;
		}
	}

	public static class ObjectConstants {
		
		/** On stock pour l'instant dans une liste les paramètre principaux de
		 *  chaque objet. Comme tu as pu le constater j'ai fais de plein de 
		 *  manière différente et je pense que celle-ci est une des plus claires.
		 */

		public static final int GRASS_ID 			= 0;
		public static final int PATH_ID 			= 1;
		public static final int WATER_ID 			= 2;
		public static final int STREET_LAMP_ID		= 3;
		public static final int MUSHROOM_ID			= 4;
		public static final int FLOWER1_ID			= 5;
		public static final int CARROT_ID			= 6;
		public static final int APPLE_ID			= 7;
		public static final int GOLD_NUGGET_ID		= 8;
		public static final int FACTORY_ID			= 9;
		public static final int HARVESTER_ID		= 10;
		public static final int APPLE_PIE_ID		= 11;
		public static final int MARKET_ID			= 12;
		public static final int HARVESTER_XXL_ID	= 13;
		public static final int FACTORY_XXL_ID		= 14;
		public static final int CARROT_PIE_ID		= 15;
		public static final int APPLE_COMPOTE_ID	= 16;
		public static final int GOLD_NUGGET_XL_ID	= 18;
		public static final int GOLD_BAR_ID			= 19;
		public static final int QUICHE_ID			= 20;
		
		public static final AtlasSprite GRASS_SPRITE 			= new AtlasSprite(GRASS_TILE_SPRITE, 0,  0, 1, 1);
		public static final AtlasSprite PATH_SPRITE 			= new AtlasSprite(PATH_TILE_SPRITE,  0,  0, 1, 1);
		public static final AtlasSprite WATER_SPRITE 			= new AtlasSprite(WATER_TILE_SPRITE, 0,  0, 1, 1);
		public static final AtlasSprite MUSHROOM_SPRITE			= new AtlasSprite(OBJECT_ATLAS, 	 2,  7, 1, 1);
		public static final AtlasSprite STREET_LAMP_SPRITE		= new AtlasSprite(OBJECT_ATLAS, 	 4,  4, 1, 3);
		public static final AtlasSprite FLOWER1_SPRITE			= new AtlasSprite(OBJECT_ATLAS, 	 0, 10, 1, 1);
		public static final AtlasSprite CARROT_SPRITE			= new AtlasSprite(OBJECT_ATLAS, 	 4,  3, 1, 1);
		public static final AtlasSprite APPLE_SPRITE			= new AtlasSprite(FOOD2_ATLAS, 	 	 2,  1, 1, 1);
		public static final AtlasSprite FACTORY_SPRITE			= new AtlasSprite(BUILDING_ATLAS, 	 1,  1, 1, 1, 4, 4, true);
		public static final AtlasSprite HARVESTER_SPRITE		= new AtlasSprite(BUILDING_ATLAS, 	 3,  0, 1, 1, 4, 4, true);
		public static final AtlasSprite APPLE_PIE_SPRITE		= new AtlasSprite(FOOD1_ATLAS, 	 	 6,  4, 1, 1);
		public static final AtlasSprite MARKET_SPRITE			= new AtlasSprite(BUILDING_ATLAS, 	 4,  0, 1, 1, 4, 4, true);
		public static final AtlasSprite CARROT_PIE_SPRITE		= new AtlasSprite(FOOD1_ATLAS, 	 	 4,  4, 1, 1);
		public static final AtlasSprite APPLE_COMPOTE_SPRITE	= new AtlasSprite(FOOD1_ATLAS, 	 	 3,  1, 1, 1);
		public static final AtlasSprite GOLD_NUGGET_SPRITE		= new AtlasSprite(OBJECT_ATLAS, 	 0,	 6, 1, 1);
		public static final AtlasSprite GOLD_NUGGET_XL_SPRITE	= new AtlasSprite(OBJECT_ATLAS, 	 1,	 6, 1, 1);
		public static final AtlasSprite GOLD_BAR_SPRITE			= new AtlasSprite(OBJECT_ATLAS, 	 2,	 6, 1, 1);
		public static final AtlasSprite QUICHE_SPRITE			= new AtlasSprite(FOOD1_ATLAS, 	 	 5,	 0, 1, 1);
		
		public static final Map<Integer, SpriteIdMappingObjects> OBJECTS_CONFIG = Map.ofEntries(
			Map.entry(GRASS_ID, 		new SpriteIdMappingObjects(GRASS_SPRITE, 		new RootObject("Grass", 			GRASS_ID, 			0, 	null))),
			Map.entry(PATH_ID, 			new SpriteIdMappingObjects(PATH_SPRITE, 		new RootObject("Path", 				PATH_ID, 			0, 	null))),
			Map.entry(WATER_ID, 		new SpriteIdMappingObjects(WATER_SPRITE, 		new RootObject("Water", 			WATER_ID, 			0, 	null))),
			Map.entry(STREET_LAMP_ID, 	new SpriteIdMappingObjects(STREET_LAMP_SPRITE, 	new RootObject("Street lamp", 		STREET_LAMP_ID, 	0, 	null))),
			Map.entry(MUSHROOM_ID, 		new SpriteIdMappingObjects(MUSHROOM_SPRITE, 	new RootObject("Mushroom", 			MUSHROOM_ID, 		1, 	PrimitiveResource.class))),
			Map.entry(FLOWER1_ID, 		new SpriteIdMappingObjects(FLOWER1_SPRITE, 		new RootObject("Flower1", 			FLOWER1_ID, 		1, 	PrimitiveResource.class))),
			Map.entry(CARROT_ID, 		new SpriteIdMappingObjects(CARROT_SPRITE, 		new RootObject("Carrot", 			CARROT_ID, 			2, 	PrimitiveResource.class))),
			Map.entry(APPLE_ID, 		new SpriteIdMappingObjects(APPLE_SPRITE, 		new RootObject("Apple", 			APPLE_ID, 			3, 	PrimitiveResource.class))),
			Map.entry(FACTORY_ID, 		new SpriteIdMappingObjects(FACTORY_SPRITE, 		new RootObject("Factory", 			FACTORY_ID, 		10, FactoryModel.class))),
			Map.entry(FACTORY_XXL_ID,	new SpriteIdMappingObjects(FACTORY_SPRITE, 		new RootObject("Factory XXL",		FACTORY_ID, 		30, FactoryXXL.class))),
			Map.entry(HARVESTER_ID, 	new SpriteIdMappingObjects(HARVESTER_SPRITE, 	new RootObject("Harvester", 		HARVESTER_ID, 		10, HarvesterModel.class))),
			Map.entry(HARVESTER_XXL_ID, new SpriteIdMappingObjects(HARVESTER_SPRITE, 	new RootObject("Harvester XXL",		HARVESTER_ID, 		30, HarvesterXXL.class))),
			Map.entry(APPLE_PIE_ID, 	new SpriteIdMappingObjects(APPLE_PIE_SPRITE, 	new RootObject("Apple pie", 		APPLE_PIE_ID, 		5, 	Resource.class))),
			Map.entry(MARKET_ID, 		new SpriteIdMappingObjects(MARKET_SPRITE, 		new RootObject("Market", 			MARKET_ID, 			0, 	null))),
			Map.entry(CARROT_PIE_ID, 	new SpriteIdMappingObjects(CARROT_PIE_SPRITE, 	new RootObject("Carrot pie", 		CARROT_PIE_ID, 		3, 	Resource.class))),
			Map.entry(APPLE_COMPOTE_ID,	new SpriteIdMappingObjects(APPLE_COMPOTE_SPRITE,new RootObject("Compote de pommes",	APPLE_COMPOTE_ID, 	3, 	Resource.class))),
			Map.entry(GOLD_NUGGET_ID,	new SpriteIdMappingObjects(GOLD_NUGGET_SPRITE,	new RootObject("Gold nugget",		GOLD_NUGGET_ID,		30, PrimitiveResource.class))),
			Map.entry(GOLD_NUGGET_XL_ID,new SpriteIdMappingObjects(GOLD_NUGGET_XL_SPRITE,new RootObject("Gold nugget XL",	GOLD_NUGGET_XL_ID,	60, Resource.class))),
			Map.entry(GOLD_BAR_ID,		new SpriteIdMappingObjects(GOLD_BAR_SPRITE,		new RootObject("Gold bar",			GOLD_BAR_ID,		120,Resource.class))),
			Map.entry(QUICHE_ID,		new SpriteIdMappingObjects(QUICHE_SPRITE,		new RootObject("Quiche",			QUICHE_ID,			5,  Resource.class)))
		);

		public static RootObject getRootObject(int spriteId) {
			return OBJECTS_CONFIG.get(spriteId).rootObject;
		}
	}

	public static class PlayerAnimationConstants {
		public static final int IDLE_DOWN 			= 0;
		public static final int IDLE_LEFT_RIGHT 	= 1; // pour l'avoir dans la direction gauche il faut faire son miroir
		public static final int IDLE_UP 			= 2;
		public static final int RUNNING_DOWN 		= 3;
		public static final int RUNNING_LEFT_RIGHT 	= 4; // pareil faire le miroir de l'image pour aller à gauche
		public static final int RUNNING_UP 			= 5;
		public static final int ATTACK_DOWN 		= 6;
		public static final int ATTACK_LEFT_RIGHT 	= 7;
		public static final int ATTACK_UP 			= 8;
		public static final int DYING 				= 9; // je crois que c'est l'animation pour mourir

		public static int GetSpriteCount(int action) {
			// Ancien switch case obligé car on est sous java 11

			// On a 6 animations pour idle et running dans chaque direction et 4 pour les
			// attaques et mourir
			switch (action) {
			case IDLE_DOWN:
			case IDLE_LEFT_RIGHT:
			case IDLE_UP:
			case RUNNING_DOWN:
			case RUNNING_LEFT_RIGHT:
			case RUNNING_UP:
				return 6;
			case ATTACK_DOWN:
			case ATTACK_LEFT_RIGHT:
			case ATTACK_UP:
			case DYING:
				return 4;
			}
			return 0;
		}
	}

	public static class RecipeContants {
		public static final Map<Integer, Recipe> recipies = Map.of(
			APPLE_PIE_ID, 		new Recipe(getRootObject(APPLE_PIE_ID), 		new InventoryItem(getRootObject(APPLE_COMPOTE_ID), 1), new InventoryItem(getRootObject(APPLE_ID), 1)),
			CARROT_PIE_ID, 		new Recipe(getRootObject(CARROT_PIE_ID), 		new InventoryItem(getRootObject(CARROT_ID), 2)),
			APPLE_COMPOTE_ID, 	new Recipe(getRootObject(APPLE_COMPOTE_ID), 	new InventoryItem(getRootObject(APPLE_ID), 2)),
			GOLD_NUGGET_XL_ID, 	new Recipe(getRootObject(GOLD_NUGGET_XL_ID), 	new InventoryItem(getRootObject(GOLD_NUGGET_ID), 3)),
			GOLD_BAR_ID, 		new Recipe(getRootObject(GOLD_BAR_ID), 			new InventoryItem(getRootObject(GOLD_NUGGET_XL_ID), 5)),
			QUICHE_ID, 			new Recipe(getRootObject(QUICHE_ID), 			new InventoryItem(getRootObject(MUSHROOM_ID), 2), new InventoryItem(getRootObject(CARROT_ID), 2))
		);
	}
}

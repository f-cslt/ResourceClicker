package utils;

import static utils.GameConstants.TilesConstants.TILE_SIZE;

import java.awt.Point;

import model.GameEntity;

/**
 * On utilisera cette classe pour y mettre les fonctions de check de collision,
 * de pathfinding etc. Pour l'instant je veux y mettre ma fonction de check pour
 * qu'un objet de plusieurs tiles ne soit pas dessiné plusieurs fois.
 */

public class HelpFunctions {

	/**
	 * Le but de cette fonction est de detecter si un objet est d'une hauteur ou
	 * largeur supérieur à 1. Sans cette méthode l'objet serait dessiné à chaque
	 * fois qu'on rencontrerait son id alors qu'il s'agit du même objet. Exemple :
	 * le lampadaire serait dessiné 3 fois alors qu'il s'agit juste d'un objet de
	 * hauteur 3 donc on met des 3 où l'on veut que notre lampadaire soit.
	 * 
	 * @param mapObjectData La matrice de notre map
	 * @param i             La position de la ligne
	 * @param j             La position de la colonne
	 * @param objectId      Le type d'objet
	 * @return Un booléen qui nous indique s'il s'agit bien de la tile en haut à
	 *         gauche de l'objet
	 */

	public static boolean isTopLeftCornerOfMultiTileObject(Array2D<GameEntity> mapObjectData, int i, int j, int objectId) {
		// Vérifier si la position actuelle correspond à l'objet
		if (mapObjectData.get(i, j).getSpriteId() != objectId) {
			return false; // Ce n'est pas l'objet recherché
		}


		// Vérifier qu'il n'y a pas d'objet identique au-dessus ou à gauche
		boolean noSameObjectAbove = (i == 0 || mapObjectData.get(i - 1, j).getSpriteId() != objectId);
		boolean noSameObjectLeft = (j == 0 || mapObjectData.get(i, j - 1).getSpriteId() != objectId);

		return noSameObjectAbove && noSameObjectLeft;
	}

	/**
	 * Simple helper function that returns the tile coordinates 
	 * corresponding to the specified screen coordinates.
	 * 
	 * @param screenx
	 * @param screeny
	 * @return
	 */
	public static Point windowToMapCoord(int screenx, int screeny) {
		int mapx = screenx / TILE_SIZE;
		int mapy = screeny / TILE_SIZE;

		return new Point(mapx, mapy);
	}
}

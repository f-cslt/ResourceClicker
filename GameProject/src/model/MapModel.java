package model;

import static utils.GameConstants.TilesConstants.*;
import static utils.FileHandler.*;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import utils.Array2D;
import utils.GameConstants.ObjectConstants;

import static utils.GameConstants.ObjectConstants.*;
import static utils.GameConstants.PlayerConstants.PLAYER_SIZE;

public class MapModel extends AbstractModel {
	private static MapModel activeMap;

	// Cette matrice stock les tiles de base donc de la première couche (eau, terre,
	// pelouse).
	private int[][] mapData;
	// Cette matrice constitue la deuxième couche. Elle contient les emplacements
	// d'objets et décorations.
	private Array2D<GameEntity> mapObjectData;
	/*
	 * Cette Hashmap nous permet de stocker nos tiles de base et nos objets avec
	 * l'association clé objet. La clé correspond au nombre dans le fichier texte et
	 * l'objet peut-être une tile de base ou un bien une fleur, un moulin...
	 */
	
	public static Point startPosition = new Point();

	public MapModel() {
		createMap();
		loadMapData();
		activeMap = this;
	}

	public static MapModel getActiveMap() {
		return activeMap;
	}

	@SuppressWarnings("unused")
	public void update() {
		// TODO: implémenter cette méthode car la map est censée évoluer (objets qui disparaissent etc)
		mapObjectData.foreach((e, p) -> e.update());
	}
	
	
	
	private int[][] generateMapData() {
		int[][] mapGenerated = new int[TILES_IN_HEIGHT][TILES_IN_WIDTH];
		for (int y = 0; y < TILES_IN_HEIGHT; y++) {
            for (int x = 0; x < TILES_IN_WIDTH; x++) {
                mapGenerated[y][x] = GRASS_ID;
            }
        }
		createClusters(mapGenerated, WATER_ID, 10, 10, 3);
		createPaths(mapGenerated, PATH_ID, 10, 15);
		return mapGenerated;
	}
	
	private void createClusters(int[][] map, int tileType, int clusterCount, int clusterSize, int spread) {
		Random random = new Random();
		for(int i=0 ; i < clusterCount ; i++) {
			int startX = random.nextInt(TILES_IN_WIDTH);
			int startY = random.nextInt(TILES_IN_HEIGHT);
			
			for(int j=0 ; j < clusterSize ; j++) {
				int offsetX = random.nextInt(spread) - spread / 2;
                int offsetY = random.nextInt(spread) - spread / 2;

                int x = Math.min(Math.max(startX + offsetX, 0), TILES_IN_WIDTH - 1);
                int y = Math.min(Math.max(startY + offsetY, 0), TILES_IN_HEIGHT - 1);

                
                map[y][x] = tileType;
			}
		}
	}
	
	private void createPaths(int[][] map, int tileType, int pathCount, int pathLength) {
		Random random = new Random();
        for (int i = 0; i < pathCount; i++) {
            int x = random.nextInt(TILES_IN_WIDTH);
            int y = random.nextInt(TILES_IN_HEIGHT);

            for (int j = 0; j < pathLength; j++) {
                map[y][x] = tileType;

                // Déplacement aléatoire pour former un chemin
                int direction = random.nextInt(4);
                switch (direction) {
                    case 0: y = Math.max(y - 1, 0); break; // Haut
                    case 1: y = Math.min(y + 1, TILES_IN_HEIGHT - 1); break; // Bas
                    case 2: x = Math.max(x - 1, 0); break; // Gauche
                    case 3: x = Math.min(x + 1, TILES_IN_WIDTH - 1); break; // Droite
                }
            }
        }
		
	}
		
	private void createMap() {
		try {
			clearDataFromMapFile();
			PrintWriter printWriter = new PrintWriter(new FileWriter("res" + MAP_FILE_PATH, true));
			Random rand = new Random();
			double rndVal;
			int rndKey;
			boolean marketPlaced = false;

			int[][] mapGenerated = generateMapData();
			
			for(int i=0 ; i < TILES_IN_HEIGHT ; i++) {
				for(int j=0 ; j< TILES_IN_WIDTH ; j++) {
					printWriter.print(mapGenerated[i][j] + " ");
				}
				printWriter.println();
			}	
			printWriter.println();
			
			int[] objectsSeenTwice = new int[5]; // 5 objets dans la map
			
			for(int i=0 ; i < objectsSeenTwice.length ; i++)
				objectsSeenTwice[i] = 0;
			
			
			for(int i=0 ; i < TILES_IN_HEIGHT ; i++) {
				for(int j=0 ; j< TILES_IN_WIDTH ; j++) {
					
					if((mapGenerated[i][j] == WATER_ID)) {	// Pas d'objet sur l'eau
						rndKey= 0;
					}else {
						rndVal = rand.nextDouble();
						
						if(rndVal <= 0.93)
							rndKey = GRASS_ID;
						else if(rndVal <= 0.935) {
							rndKey = STREET_LAMP_ID;
						}
						else if(!marketPlaced && ((i == 0 || j == 0 || i == TILES_IN_HEIGHT - 1 || j == TILES_IN_WIDTH - 1) && rndVal <= 0.97)) {	//placement au hasard sur les bords
							rndKey = MARKET_ID;				
							marketPlaced = true;
						}
						else {
							do {
								rndKey = 4 + rand.nextInt(5);
								if(objectsSeenTwice[rndKey - 4] >= 2) {
									rndKey = allObjectsSeenTwice(objectsSeenTwice) + 4;
								}
								objectsSeenTwice[rndKey - 4] ++;
							}while(rndKey != 0 && objectsSeenTwice[rndKey - 4] < 2);
						}
						if(!marketPlaced && i == TILES_IN_HEIGHT -1 && j == TILES_IN_WIDTH - 1) {	// S'assurer d'avoir un marché
							rndKey = MARKET_ID;
							marketPlaced = true;
						}
					}
					printWriter.print(rndKey + " ");

					
				}
				printWriter.println();
			}
			 printWriter.println("## START POSITION:");

			// Position aléatoire où le joueur sera visible dans la carte
	        int startX = rand.nextInt((TILE_SIZE * TILES_IN_WIDTH) - PLAYER_SIZE);
	        int startY = rand.nextInt((TILE_SIZE * TILES_IN_HEIGHT) - PLAYER_SIZE);
			
			printWriter.print(startX + ";" + startY);
			
			printWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	

	private int allObjectsSeenTwice(int[] objectsSeenTwice) {
		Random random = new Random();
		for(int i=0 ; i < objectsSeenTwice.length ; i++) {
			if(objectsSeenTwice[i] < 2)
				return i;
		}
		return random.nextInt(objectsSeenTwice.length);
	}

	private void loadMapData() {
		this.mapData = new int[TILES_IN_HEIGHT][TILES_IN_WIDTH];
		this.mapObjectData = new Array2D<>(TILES_IN_HEIGHT, TILES_IN_WIDTH);

		try {
			// On charge le fichier txt dans un buffer
			BufferedReader bufferedReader = getBufferedReader(MAP_FILE_PATH);

			// On itère sur notre fichier pour stocker les données dans notre matrice
			for (int i = 0; i < TILES_IN_HEIGHT; i++) {
				String[] line = bufferedReader.readLine().split(" "); // On parse seulement avec des espaces pour
																		// l'instant en tout cas
				for (int j = 0; j < TILES_IN_WIDTH; j++) {
					mapData[i][j] = Integer.parseInt(line[j]);
				}
			}

			// Ligne vide entre les deux matrices du fichier texte
			bufferedReader.readLine();

			for (int i = 0; i < TILES_IN_HEIGHT; i++) {
				String[] line = bufferedReader.readLine().split(" ");
				for (int j = 0; j < TILES_IN_WIDTH; j++) {
					RootObject rootObject = ObjectConstants.getRootObject(Integer.parseInt(line[j]));
					setObject(initEntity(j, i, rootObject));
				}
			}
			
			bufferedReader.readLine();
			
			String[] startPosLine = bufferedReader.readLine().split(";");
					
			startPosition.setLocation(Integer.parseInt(startPosLine[0]), Integer.parseInt(startPosLine[1]));
				

			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int[][] getMapData() {
		return this.mapData;
	}

	public Array2D<GameEntity> getMapObjectData() {
		return mapObjectData;
	}

	public AtlasSprite getGameObject(int id) {
		return OBJECTS_CONFIG.get(id).atlasSprite;
	}

	public GameEntity getObjectAt(int x, int y) {
		return mapObjectData.get(y, x);
	}

	public void setObject(GameEntity e) {
		mapObjectData.set(e.getY(), e.getX(), e);
	}

	public GameEntity[] getAdjacent(int x, int y) {
		x = Math.max(0, x-1);
		y = Math.max(0, y-1);
		List<GameEntity> res = new ArrayList<>();

		for (int i = y; i < y+3; i++) {
			for (int j = x; j < x+3; j++) {
				if (i != y+1 || j != x+1) {
					res.add(getObjectAt(j, i));
				}
			}
		}

		return res.toArray(GameEntity[]::new);
	}

	public void place(GameEntity entity, int x, int y) {
		if (mapObjectData.get(y, x).getSpriteId() != 0) {
			return;
		}

		if (entity.setPos(x, y)) {
			setObject(entity);
		}
	}
	
	public Point getStartPosition() {
		return startPosition;
	}

	private GameEntity initEntity(int x, int y, RootObject rootObject) {
		GameEntity entity = null;

		if(rootObject.spriteId == MARKET_ID) {
			entity = new Market(x, y);
		}
		else if (rootObject.targetedClass == null) {
			entity = new GameEntity(x, y, true, rootObject);
		} else {
			try {
				entity = rootObject.constructEntity(x, y);
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return entity;
	}
	/**
	 * Charge les informations du fichier de sauvgarde dans mapData et mapObjectData.
	 */
	public void loadData() {
		BufferedReader bufferedReader = getBufferedReader("/sauvegarde.txt");
		String line;
		try {
			while ((line = bufferedReader.readLine()) != null) {
			    switch (line) {
			    	
			    	// Partie du fichier concernant mapData 
			        case "## MAP DATA:":
			        	/*
			        	 * Même code que dans loadMapData. On peut le changer, etc mais ça fonctionne et c'est simple.
			        	 */
			            for (int i = 0; i < TILES_IN_HEIGHT; i++) {
			                String[] mapDataLine = bufferedReader.readLine().split(" ");
			                for (int j = 0; j < TILES_IN_WIDTH; j++) {
			                    mapData[i][j] = Integer.parseInt(mapDataLine[j]);
			                }
			            }
			            break;

			        // Partie du fichier concernant mapObjectData
			        case "## MAP OBJECT DATA:":
			        	/*
			        	 * On veut lire les lignes du fichier de sauvegarde tant qu'on est dans la partie "## MAP OBJECT DATA:".
			        	 * Donc on s'arrête de lire soit s'il n'y a pas de prochaine ligne, ou si on entre dans une nouvelle 
			        	 * partie de la sauvegarde "## INVENTORY DATA:" par exemple.
			        	 */
			            while ((line = bufferedReader.readLine()) != null && !line.startsWith("##")) {
			                String[] objectLine = line.split(";");	// Les données sont parsées avec des ';'
							RootObject rootObject = ObjectConstants.getRootObject(Integer.parseInt(objectLine[0]));
							int x = Integer.parseInt(objectLine[2]);
							int y = Integer.parseInt(objectLine[3]);
							
			                setObject(initEntity(x, y, rootObject));
			            }
			            break;

			        default:
			            break;
			    }
			}
		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Fonction qui enregistre les informations de mapData et mapObjectData dans
	 * le fichier de sauvegarde
	 */
	public void saveData() {
		// Data de la map sous la même forme que la première matrice de map.txt
		writeToSaveFile("## MAP DATA:");
		for(int i=0 ; i < mapData.length ; i++) {
			String saveString = "";
			for(int j=0 ; j < mapData[i].length ; j++) {
				saveString += mapData[i][j] + " ";
			}
			writeToSaveFile(saveString.substring(0, saveString.length() - 1));	// On enlève le dernier espace inutile
		}
		
		// Data des objets sous la forme spriteID;walkable;xPos;yPos
		writeToSaveFile("## MAP OBJECT DATA:");
		
		mapObjectData.foreach((e, p) -> e.saveData());
	}
	
	public static void clearDataFromMapFile() {
		try {
			PrintWriter printWriter = new PrintWriter(new FileWriter("res" + MAP_FILE_PATH, false));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

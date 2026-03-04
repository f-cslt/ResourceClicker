package model;

import static utils.FileHandler.getSubImage;

import java.awt.image.BufferedImage;

public class AtlasSprite {
    private final int tiles_in_width;
    private final int tiles_in_height;
    private final BufferedImage img;

    public AtlasSprite(String atlasPath, int x, int y, int tiles_in_width, int tiles_in_height) {
        this(atlasPath, x, y, tiles_in_width, tiles_in_height, tiles_in_width, tiles_in_height, false);
    }

    /**
     * 
     * @param atlasPath
     * @param x
     * @param y
     * @param tiles_in_width the width this sprite should be rendered at.
     * @param tiles_in_height the height this sprite should be rendered at.
     * @param realTilesInWidth the width of this sprite in the atlas.
     * @param realTilesInHeight the height of this sprite in the atlas.
     * @param isBigSprite if true, x/y will be multiplied by the real tiles width/height. This option is targeted at rendering 64x64 sprites.
     */
    public AtlasSprite(String atlasPath, int x, int y, int tiles_in_width, int tiles_in_height, int realTilesInWidth, int realTilesInHeight, boolean isBigSprite) {
        this.tiles_in_width     = tiles_in_width;
        this.tiles_in_height    = tiles_in_height;

        int _x = isBigSprite ? x*realTilesInWidth : x;
        int _y = isBigSprite ? y*realTilesInHeight : y;
        
        this.img = getSubImage(atlasPath, _x, _y, realTilesInWidth, realTilesInHeight);
    }

    public int getTilesInWidth()    { return tiles_in_width;    }
    public int getTilesInHeight()   { return tiles_in_height;   }

    public BufferedImage getImg() {
		return img;
	}
}

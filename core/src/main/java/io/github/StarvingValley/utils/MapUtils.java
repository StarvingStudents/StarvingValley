package io.github.StarvingValley.utils;

import com.badlogic.gdx.Gdx;

public class MapUtils {
    public static int calculateVerticalTileCount(int horizontalTilesCount) {
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();

        int tileSize = screenWidth / horizontalTilesCount;

        return screenHeight / tileSize;
    }
}

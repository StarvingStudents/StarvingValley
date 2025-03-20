package io.github.StarvingValley.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class ScreenUtils {
    public static Vector2 convertTouchCoordinatesToRenderCoordinates(int screenX, int screenY) {
        float touchX = screenX;

        // input-coordinates-origo is top-left but the rendering-coordinates-origo is
        // bottom-left
        float touchY = Gdx.graphics.getHeight() - screenY;

        return new Vector2(touchX, touchY);
    }
}

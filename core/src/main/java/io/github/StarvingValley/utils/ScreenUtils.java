package io.github.StarvingValley.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class ScreenUtils {
    public static Vector2 getScreenPositionFromTouchPosition(int screenX, int screenY) {
        float touchX = screenX;

        // input-coordinates-origo is top-left but the rendering-coordinates-origo is
        // bottom-left
        float touchY = Gdx.graphics.getHeight() - screenY;

        return new Vector2(touchX, touchY);
    }

    public static Vector2 getMouseScreenPosition() {
        return new Vector2(
                Gdx.input.getX(),
                Gdx.graphics.getHeight() - Gdx.input.getY());
    }

    public static Vector2 getWorldPosition(OrthographicCamera camera, int screenX, int screenY) {
        Vector3 unprojected = camera.unproject(new Vector3(screenX, screenY, 0));
        return new Vector2(unprojected.x, unprojected.y);
    }
}

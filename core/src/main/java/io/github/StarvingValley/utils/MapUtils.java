package io.github.StarvingValley.utils;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;

import io.github.StarvingValley.config.Config;
import io.github.StarvingValley.models.entities.EnvironmentCollidableFactory;

public class MapUtils {
    public static void loadCollidables(TiledMap map, float unitScale, Engine engine) {
        MapLayer layer = map.getLayers().get(Config.MAP_COLLISION_LAYER_NAME);
        if (layer == null)
            return;

        // Currently only supports rectangles as we might not need polygons
        for (MapObject object : layer.getObjects().getByType(RectangleMapObject.class)) {
            Rectangle hitbox = ((RectangleMapObject) object).getRectangle();

            Rectangle scaledHitbox = new Rectangle(
                    hitbox.x * unitScale,
                    hitbox.y * unitScale,
                    hitbox.width * unitScale,
                    hitbox.height * unitScale);

            engine.addEntity(EnvironmentCollidableFactory.CreateEnvironmentColliderComponent(scaledHitbox));
        }
    }

    public static int calculateVerticalTileCount(int horizontalTilesCount) {
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();

        int tileSize = screenWidth / horizontalTilesCount;

        return screenHeight / tileSize;
    }
}

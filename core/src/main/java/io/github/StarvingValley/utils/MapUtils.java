package io.github.StarvingValley.utils;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;

import io.github.StarvingValley.config.Config;
import io.github.StarvingValley.models.dto.WorldObjectConfig;
import io.github.StarvingValley.models.entities.WorldObjectFactory;
import io.github.StarvingValley.models.types.WorldLayer;

public class MapUtils {
    public static void loadCollidables(TiledMap map, float unitScale, Engine engine) {
        List<Rectangle> scaledHitboxes = getScaledHitboxes(map, Config.MAP_COLLISION_LAYER_NAME, unitScale, engine);
        for (Rectangle scaledHitbox : scaledHitboxes) {
            WorldObjectConfig config = new WorldObjectConfig();
            config.blocksMovement = true;
            config.blocksPlacement = false;
            config.worldLayer = WorldLayer.TERRAIN;

            engine.addEntity(WorldObjectFactory.createWorldObject(scaledHitbox, config));
        }
    }

    public static void loadPlacementBlockers(TiledMap map, float unitScale, WorldLayer layerTypeToApply,
            Engine engine) {
        List<Rectangle> scaledHitboxes = getScaledHitboxes(map, Config.MAP_NON_PLACEMENT_LAYER_NAME, unitScale, engine);
        for (Rectangle scaledHitbox : scaledHitboxes) {
            WorldObjectConfig config = new WorldObjectConfig();
            config.blocksMovement = false;
            config.blocksPlacement = true;
            config.worldLayer = layerTypeToApply;

            Entity entity = WorldObjectFactory.createWorldObject(scaledHitbox, config);

            engine.addEntity(entity);
            TileUtils.updateOverlappingTiles(entity);
        }
    }

    // Currently only supports rectangles as we might not need polygons
    public static List<Rectangle> getScaledHitboxes(TiledMap map, String mapLayerName, float unitScale, Engine engine) {
        List<Rectangle> result = new ArrayList<>();

        MapLayer layer = map.getLayers().get(mapLayerName);
        if (layer == null)
            return result;

        for (MapObject object : layer.getObjects().getByType(RectangleMapObject.class)) {
            Rectangle scaledHitbox = getScaledHitbox(object, unitScale);

            result.add(scaledHitbox);
        }

        return result;
    }

    private static Rectangle getScaledHitbox(MapObject mapObject, float unitScale) {
        Rectangle rect = ((RectangleMapObject) mapObject).getRectangle();

        return new Rectangle(
                rect.x * unitScale,
                rect.y * unitScale,
                rect.width * unitScale,
                rect.height * unitScale);
    }

    public static int calculateVerticalTileCount(int horizontalTilesCount) {
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();

        int tileSize = screenWidth / horizontalTilesCount;

        return screenHeight / tileSize;
    }
}

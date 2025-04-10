package io.github.StarvingValley.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;

import io.github.StarvingValley.models.components.AnimationComponent;
import io.github.StarvingValley.models.components.SpriteComponent;
import io.github.StarvingValley.config.Config;
import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.Interfaces.EntityDataCallback;
import io.github.StarvingValley.models.components.SpriteComponent;
import io.github.StarvingValley.models.components.UnsyncedComponent;
import io.github.StarvingValley.models.dto.SyncEntity;
import io.github.StarvingValley.models.entities.MapFactory;
import io.github.StarvingValley.models.entities.PlayerFactory;
import io.github.StarvingValley.models.types.GameContext;
import io.github.StarvingValley.models.types.PrefabType;
import io.github.StarvingValley.models.types.WorldLayer;

public class MapUtils {
    public static void loadEnvCollidables(TiledMap map, float unitScale, Engine engine) {
        List<Rectangle> scaledHitboxes = getScaledHitboxes(map, Config.MAP_COLLISION_LAYER_NAME, unitScale, engine);
        for (Rectangle scaledHitbox : scaledHitboxes) {
            engine.addEntity(MapFactory.createEnvCollidable(scaledHitbox));
        }
    }

    public static void loadPlacementBlockers(TiledMap map, float unitScale, WorldLayer layerTypeToApply,
            Engine engine) {
        List<Rectangle> scaledHitboxes = getScaledHitboxes(map, Config.MAP_NON_PLACEMENT_LAYER_NAME, unitScale, engine);
        for (Rectangle scaledHitbox : scaledHitboxes) {
            Entity entity = MapFactory.createEnvPlacementBlocker(scaledHitbox);
            engine.addEntity(entity);
        }
    }

    // TODO: While we're waiting on firebase to fetch entities we should show a
    // loading screen so we
    // don't first load the map, then add the player which causes the camera to jump
    public static void loadSyncedEntities(GameContext context, Entity camera) {
        context.firebaseRepository.getAllEntities(
                new EntityDataCallback() {
                    @Override
                    public void onSuccess(Map<String, SyncEntity> data) {

                        boolean anyIsPlayer = false;
                        for (Map.Entry<String, SyncEntity> entry : data.entrySet()) {
                            SyncEntity syncEntity = entry.getValue();

                            Entity entity = EntitySerializer.deserialize(syncEntity, camera, context.assetManager);

                            // Replace static sprite with animation for players
                            if (syncEntity.isPlayer) {
                                anyIsPlayer = true;
                                context.player = entity;


                                AnimationComponent anim = AnimationFactory.createAnimationsForType(PrefabType.PLAYER,context.assetManager);
                                entity.add(anim);
                            }

                            skipSpriteSyncOnLoad(entity);
                            context.engine.addEntity(entity);
                        }

                        if (!anyIsPlayer) {
                            Entity player = PlayerFactory.createPlayer(35, 15, 1, 1, 5f, context.assetManager, camera);
                            player.add(new UnsyncedComponent());
                            skipSpriteSyncOnLoad(player);
                            context.engine.addEntity(player);
                        }
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        System.err.println("Failed to load entities: " + errorMessage);
                    }
                });
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

    // Skip sync of sprite when it loads in SpriteSystem
    private static void skipSpriteSyncOnLoad(Entity entity) {
        SpriteComponent spriteComponent = Mappers.sprite.get(entity);
        if (spriteComponent != null && spriteComponent.getTexturePath() != null) {
            spriteComponent.previousTexturePath = spriteComponent.getTexturePath();
        }
    }
}

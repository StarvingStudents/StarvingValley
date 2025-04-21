package io.github.StarvingValley.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;

import io.github.StarvingValley.config.Config;
import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.Interfaces.EntityDataCallback;
import io.github.StarvingValley.models.components.AnimationComponent;
import io.github.StarvingValley.models.components.AttackComponent;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.components.SpriteComponent;
import io.github.StarvingValley.models.components.SyncComponent;
import io.github.StarvingValley.models.components.UnsyncedComponent;
import io.github.StarvingValley.models.dto.SyncEntity;
import io.github.StarvingValley.models.entities.MapFactory;
import io.github.StarvingValley.models.entities.PlayerFactory;
import io.github.StarvingValley.models.entities.WorldMapUserFactory;
import io.github.StarvingValley.models.types.GameContext;
import io.github.StarvingValley.models.types.PrefabType;
import io.github.StarvingValley.models.types.ScreenType;
import io.github.StarvingValley.models.types.WorldLayer;

public class MapUtils {
  public static void loadEnvCollidables(TiledMap map, float unitScale, Engine engine) {
    List<Rectangle> scaledHitboxes = getScaledHitboxes(map, Config.MAP_COLLISION_LAYER_NAME, unitScale, engine);
    for (Rectangle scaledHitbox : scaledHitboxes) {
      engine.addEntity(MapFactory.createEnvCollidable(scaledHitbox));
    }
  }

  public static void loadPlacementBlockers(
      TiledMap map, float unitScale, WorldLayer layerTypeToApply, Engine engine) {
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

              if (syncEntity.isPlayer) {
                anyIsPlayer = true;
                context.player = entity;

                AnimationComponent anim = AnimationFactory.createAnimationsForType(
                    PrefabType.PLAYER, context.assetManager);
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

              context.player = player;
            }
          }

          @Override
          public void onFailure(String errorMessage) {
            System.err.println("Failed to load your entities: " + errorMessage);
          }
        });
  }

  private static final float FARM_TO_VILLAGE_BOUNDARY = 39.5f;
  private static final float VILLAGE_TO_FARM_BOUNDARY = 0f;

  public static void loadSyncedFarmEntities(GameContext context, Entity camera) {
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
                Mappers.currScreen.get(entity).currentScreen = ScreenType.FARM;
                AnimationComponent anim = AnimationFactory.createAnimationsForType(PrefabType.PLAYER,
                    context.assetManager);
                entity.add(anim);
              }

              skipSpriteSyncOnLoad(entity);
              context.engine.addEntity(entity);
            }

            if (!anyIsPlayer) {
              Entity player = PlayerFactory.createPlayer(35, 15, 1, 1, 5f, context.assetManager, camera);
              player.add(new UnsyncedComponent());
              Mappers.currScreen.get(player).currentScreen = ScreenType.FARM;
              skipSpriteSyncOnLoad(player);
              context.engine.addEntity(player);
              context.player = player;
            }
          }

          @Override
          public void onFailure(String errorMessage) {
            System.err.println("Failed to load entities: " + errorMessage);
          }
        });
  }

  public static void loadSyncedVillageEntities(GameContext context, Entity camera) {
    context.firebaseRepository.getAllEntities(
        new EntityDataCallback() {
          @Override
          public void onSuccess(Map<String, SyncEntity> data) {
            Entity player = getPlayer(data, camera, context);
            Mappers.currScreen.get(player).currentScreen = ScreenType.VILLAGE;
            skipSpriteSyncOnLoad(player);
            context.engine.addEntity(player);
            context.player = player;
          }

          @Override
          public void onFailure(String errorMessage) {
            System.err.println("Failed to load entities: " + errorMessage);
          }
        });
  }

  public static void loadPlayerForAttack(GameContext context, Entity camera) {
    context.firebaseRepository.getAllEntities(
        new EntityDataCallback() {
          @Override
          public void onSuccess(Map<String, SyncEntity> data) {
            Entity player = getPlayer(data, camera, context);
            player.add(new AttackComponent(Config.ATTACK_DURATION));

            PositionComponent position = Mappers.position.get(player);
            position.position.x = Config.DEFAULT_SPAWN_LOCATION.x;
            position.position.y = Config.DEFAULT_SPAWN_LOCATION.y;

            skipSpriteSyncOnLoad(player);
            context.engine.addEntity(player);
            context.player = player;
          }

          @Override
          public void onFailure(String errorMessage) {
            System.err.println("Failed to load entities: " + errorMessage);
          }
        });
  }

  public static void loadSyncedEntitiesForUser(GameContext context, Entity camera, String userId) {
    context.firebaseRepository.getEntitiesForUser(
        userId,
        new EntityDataCallback() {
          @Override
          public void onSuccess(Map<String, SyncEntity> data) {
            for (Map.Entry<String, SyncEntity> entry : data.entrySet()) {
              SyncEntity syncEntity = entry.getValue();

              // Skip the player's avatar & systems
              if (syncEntity.isPlayer) {
                continue;
              }

              Entity entity = EntitySerializer.deserialize(syncEntity, camera, context.assetManager);

              entity.remove(SyncComponent.class);

              skipSpriteSyncOnLoad(entity);
              context.engine.addEntity(entity);
            }
          }

          @Override
          public void onFailure(String errorMessage) {
            System.err.println("Failed to load entities for user " + userId + ": " + errorMessage);
          }
        });
  }

  private static Entity getPlayer(Map<String, SyncEntity> data, Entity camera, GameContext context) {
    Entity player = null;

    for (Map.Entry<String, SyncEntity> entry : data.entrySet()) {
      SyncEntity syncEntity = entry.getValue();
      if (!syncEntity.isPlayer)
        continue;

      player = EntitySerializer.deserialize(syncEntity, camera, context.assetManager);
      break;
    }

    if (player == null) {
      player = PlayerFactory.createPlayer(35, 15, 1, 1, 5f, context.assetManager, camera);
    }

    AnimationComponent anim = AnimationFactory.createAnimationsForType(PrefabType.PLAYER, context.assetManager);
    player.add(anim);

    return player;
  }

  // Currently only supports rectangles as we might not need polygons
  public static List<Rectangle> getScaledHitboxes(
      TiledMap map, String mapLayerName, float unitScale, Engine engine) {
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
        rect.x * unitScale, rect.y * unitScale, rect.width * unitScale, rect.height * unitScale);
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

  public static void loadWorldMapFarmEntities(List<String> data, Engine engine) {
    Collections.shuffle(data);
    List<int[]> gridPositions = new ArrayList<>();

    int gridWidth = 4;
    int gridHeight = 2;
    for (int y = 0; y < gridHeight; y++) {
      for (int x = 0; x < gridWidth; x++) {
        gridPositions.add(new int[] { x, y });
      }
    }
    Collections.shuffle(gridPositions);

    int farmsToCreate = Math.min(Config.ATTACKABLE_FARMS, Math.min(data.size(), gridPositions.size()));
    for (int i = 0; i < farmsToCreate; i++) {
      int[] pos = gridPositions.get(i);

      int gridXSpacing = 3;
      int gridYSpacing = 2;

      float gridXOffset = -6f; // adjust as needed
      float gridYOffset = -1.5f; // adjust as needed

      float xCoord = pos[0] * gridXSpacing + gridXOffset; // adjust as needed
      float yCoord = pos[1] * gridYSpacing + gridYOffset; // adjust as needed
      Entity entity = WorldMapUserFactory.create(data.get(i), xCoord, yCoord);
      engine.addEntity(entity);
      System.out.println("Entity added");
    }
  }
}

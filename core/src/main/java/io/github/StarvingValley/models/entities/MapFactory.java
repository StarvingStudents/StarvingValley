package io.github.StarvingValley.models.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;

import io.github.StarvingValley.models.components.CameraComponent;
import io.github.StarvingValley.models.components.EnvironmentCollidableComponent;
import io.github.StarvingValley.models.components.MapRenderComponent;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.components.SizeComponent;
import io.github.StarvingValley.models.components.SyncComponent;
import io.github.StarvingValley.models.components.TileOccupierComponent;
import io.github.StarvingValley.models.components.TileOverlapComponent;
import io.github.StarvingValley.models.components.TiledMapComponent;
import io.github.StarvingValley.models.components.WorldLayerComponent;
import io.github.StarvingValley.models.types.WorldLayer;

public class MapFactory {
    public static Entity createMap(String tmxFilePath, float unitScale, CameraComponent camera) {
        Entity entity = new Entity();

        TiledMapComponent tiledMap = new TiledMapComponent();
        tiledMap.tiledMap = new TmxMapLoader().load(tmxFilePath);

        MapRenderComponent mapRenderer = new MapRenderComponent();
        mapRenderer.mapRenderer = new OrthogonalTiledMapRenderer(tiledMap.tiledMap, unitScale);

        entity.add(tiledMap);
        entity.add(mapRenderer);
        entity.add(camera);

        return entity;
    }

    public static Entity createEnvCollidable(Rectangle scaledHitbox) {
        Entity entity = new Entity();

        entity.add(new PositionComponent(scaledHitbox.x, scaledHitbox.y));
        entity.add(new SizeComponent(scaledHitbox.width, scaledHitbox.height));
        entity.add(new TileOverlapComponent());
        entity.add(new SyncComponent());
        entity.add(new EnvironmentCollidableComponent());
        entity.add(new WorldLayerComponent(WorldLayer.TERRAIN));

        return entity;
    }

    public static Entity createEnvPlacementBlocker(Rectangle scaledHitbox) {
        Entity entity = new Entity();

        entity.add(new PositionComponent(scaledHitbox.x, scaledHitbox.y));
        entity.add(new SizeComponent(scaledHitbox.width, scaledHitbox.height));
        entity.add(new TileOverlapComponent());
        entity.add(new SyncComponent());
        entity.add(new TileOccupierComponent());
        entity.add(new WorldLayerComponent(WorldLayer.TERRAIN));

        return entity;
    }

    public static Entity createEnvPlacementBlocker(float x, float y, int width, int height) {
        Rectangle rect = new Rectangle(x, y, width, height);
        
        return createEnvPlacementBlocker(rect);
    }
}

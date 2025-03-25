package io.github.StarvingValley.models.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import io.github.StarvingValley.models.components.CameraComponent;
import io.github.StarvingValley.models.components.MapRenderComponent;
import io.github.StarvingValley.models.components.TiledMapComponent;

public class MapFactory {
    public static Entity CreateMap(String tmxFilePath, float unitScale, CameraComponent camera) {
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
}

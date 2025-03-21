package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import io.github.StarvingValley.models.components.CameraComponent;
import io.github.StarvingValley.models.components.MapRenderComponent;

public class MapRenderSystem extends IteratingSystem {

    public MapRenderSystem() {
        super(Family.all(MapRenderComponent.class, CameraComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        CameraComponent camera = entity.getComponent(CameraComponent.class);
        MapRenderComponent mapRenderer = entity.getComponent(MapRenderComponent.class);

        mapRenderer.mapRenderer.setView(camera.camera);

        mapRenderer.mapRenderer.render();
    }
}

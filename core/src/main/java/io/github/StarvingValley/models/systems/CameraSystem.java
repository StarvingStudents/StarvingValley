package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.CameraComponent;
import io.github.StarvingValley.models.components.CameraFollowComponent;
import io.github.StarvingValley.models.components.PositionComponent;

public class CameraSystem extends IteratingSystem {
    public CameraSystem() {
        super(Family.all(CameraFollowComponent.class, PositionComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        CameraFollowComponent followComponent = Mappers.cameraFollow.get(entity);
        PositionComponent position = Mappers.position.get(entity);

        if (followComponent.targetCamera == null) {
            return;
        }

        CameraComponent cameraComponent = Mappers.camera.get(followComponent.targetCamera);
        if (cameraComponent == null) {
            return;
        }

        cameraComponent.camera.position.set(position.position.x, position.position.y, 0f);
        cameraComponent.camera.update();
    }
}

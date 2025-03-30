package io.github.StarvingValley.models.entities;

import com.badlogic.ashley.core.Entity;

import io.github.StarvingValley.models.components.CameraComponent;

public class CameraFactory {
    public static Entity createCamera(float viewportWidth, float viewportHeight) {
        Entity entity = new Entity();

        entity.add(new CameraComponent(viewportWidth, viewportHeight));

        return entity;
    }
}

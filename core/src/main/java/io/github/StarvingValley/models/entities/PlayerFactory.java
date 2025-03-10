package io.github.StarvingValley.models.entities;

import com.badlogic.ashley.core.Entity;

import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.components.SpriteComponent;
import io.github.StarvingValley.models.components.TargetComponent;
import io.github.StarvingValley.models.components.VelocityComponent;

public class PlayerFactory {
    public static Entity createPlayer(float x, float y) {
        Entity entity = new Entity();
        PositionComponent position = new PositionComponent();
        position.position.set(x, y);

        entity.add(position);
        entity.add(new VelocityComponent());
        entity.add(new TargetComponent());
        entity.add(new SpriteComponent("suspicious frog.jpg"));
        return entity;
    }
}

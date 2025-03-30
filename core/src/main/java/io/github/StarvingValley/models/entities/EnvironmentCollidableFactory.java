package io.github.StarvingValley.models.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Rectangle;

import io.github.StarvingValley.models.components.EnvironmentCollidableComponent;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.components.SizeComponent;

public class EnvironmentCollidableFactory {
    public static Entity CreateEnvironmentColliderComponent(Rectangle rect) {
        Entity entity = new Entity();

        entity.add(new EnvironmentCollidableComponent());
        entity.add(new PositionComponent(rect.x, rect.y));
        entity.add(new SizeComponent(rect.width, rect.height));

        return entity;
    }
}

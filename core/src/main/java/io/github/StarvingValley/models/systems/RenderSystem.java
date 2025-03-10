package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.components.SpriteComponent;

public class RenderSystem extends IteratingSystem {
    private final SpriteBatch batch;

    public RenderSystem(SpriteBatch batch) {
        super(Family.all(PositionComponent.class, SpriteComponent.class).get());
        this.batch = batch;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent position = entity.getComponent(PositionComponent.class);
        SpriteComponent sprite = entity.getComponent(SpriteComponent.class);

        sprite.sprite.setPosition(position.position.x, position.position.y);
        sprite.sprite.draw(batch);
    }
}

package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.DurabilityComponent;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.events.EntityRemovedEvent;
import io.github.StarvingValley.models.events.EventBus;
import io.github.StarvingValley.utils.TextureUtils;

//TODO: Use shaperenderer instead and switch to entitysystem
public class DurabilityRenderSystem extends IteratingSystem {
    private final Engine engine;
    private final SpriteBatch batch;
    private EventBus eventBus;

    public DurabilityRenderSystem(Engine engine, SpriteBatch batch, EventBus eventBus) {
        super(Family.all(DurabilityComponent.class, PositionComponent.class).get());
        this.engine = engine;
        this.batch = batch;
        this.eventBus = eventBus;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        DurabilityComponent durabilityComponent = Mappers.durability.get(entity);
        PositionComponent position = Mappers.position.get(entity);

        if (durabilityComponent == null || position == null)
            return;

        if (durabilityComponent.getHealth() <= 0) {
            eventBus.publish(new EntityRemovedEvent(entity));
            engine.removeEntity(entity);
            return;
        }

        batch.begin();

        float barX = position.position.x;
        float barY = position.position.y + 1;

        Texture pxl = TextureUtils.createWhitePixel();
        batch.setColor(Color.RED);
        batch.draw(pxl, barX, barY, 1, 0.2f);
        batch.setColor(Color.GREEN);
        batch.draw(pxl, barX, barY, 1 * durabilityComponent.getHealthPercentage(), 0.2f);
        batch.setColor(Color.WHITE);

        batch.end();
    }
}

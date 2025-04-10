package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.ActiveWorldEntityComponent;
import io.github.StarvingValley.models.components.DurabilityComponent;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.events.EntityRemovedEvent;
import io.github.StarvingValley.models.types.GameContext;
import io.github.StarvingValley.utils.TextureUtils;

//TODO: Use shaperenderer instead and switch to entitysystem
public class DurabilityRenderSystem extends IteratingSystem {
    private final Engine engine;
    private GameContext context;

    public DurabilityRenderSystem(GameContext context) {
        super(Family.all(DurabilityComponent.class, PositionComponent.class, ActiveWorldEntityComponent.class).get());
        this.context = context;
        this.engine = getEngine();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        DurabilityComponent durabilityComponent = Mappers.durability.get(entity);
        PositionComponent position = Mappers.position.get(entity);

        if (durabilityComponent == null || position == null)
            return;

        if (durabilityComponent.getHealth() <= 0) {
            context.eventBus.publish(new EntityRemovedEvent(entity));
            engine.removeEntity(entity);
            return;
        }

        context.spriteBatch.begin();

        float barX = position.position.x;
        float barY = position.position.y + 1;

        Texture pxl = TextureUtils.createWhitePixel();
        context.spriteBatch.setColor(Color.RED);
        context.spriteBatch.draw(pxl, barX, barY, 1, 0.2f);
        context.spriteBatch.setColor(Color.GREEN);
        context.spriteBatch.draw(pxl, barX, barY, 1 * durabilityComponent.getHealthPercentage(), 0.2f);
        context.spriteBatch.setColor(Color.WHITE);

        context.spriteBatch.end();
    }
}

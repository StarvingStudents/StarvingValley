package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.ActiveWorldEntityComponent;
import io.github.StarvingValley.models.components.HungerComponent;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.types.GameContext;
import io.github.StarvingValley.utils.TextureUtils;

//TODO: Use shaperenderer instead and switch to entitysystem
public class HungerRenderSystem extends IteratingSystem {
    private GameContext context;

    public HungerRenderSystem(GameContext context) {
        super(Family.all(HungerComponent.class, PositionComponent.class, ActiveWorldEntityComponent.class).get());
        this.context = context;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        HungerComponent HungerComponent = Mappers.hunger.get(entity);
        PositionComponent position = Mappers.position.get(entity);
        if (HungerComponent == null || position == null) {
            return;
        }
        
        float barX = position.position.x;
        float barY = position.position.y + 1f;

        context.spriteBatch.begin();
        
        Texture pxl = TextureUtils.createWhitePixel();
        context.spriteBatch.setColor(Color.BROWN);
        context.spriteBatch.draw(pxl, barX, barY, 1, 0.2f);
        context.spriteBatch.setColor(Color.GOLD);
        context.spriteBatch.draw(pxl, barX, barY, 1 * HungerComponent.hungerPoints / HungerComponent.maxHungerPoints,
                0.2f);
        context.spriteBatch.setColor(Color.WHITE);

        context.spriteBatch.end();
    }    
}

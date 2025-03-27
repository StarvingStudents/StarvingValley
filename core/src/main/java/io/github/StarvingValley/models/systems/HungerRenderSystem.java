package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.HungerComponent;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.utils.TextureUtils;

public class HungerRenderSystem extends IteratingSystem {
    private final Engine engine; 
    private final SpriteBatch batch;

    public HungerRenderSystem(Engine engine, SpriteBatch batch) {
        super(Family.all(HungerComponent.class, PositionComponent.class).get());
        this.engine = engine;
        this.batch = batch;
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

        Texture pxl = TextureUtils.createWhitePixel();
        batch.setColor(Color.BROWN);
        batch.draw(pxl, barX, barY, 1 , 0.2f);
        batch.setColor(Color.GOLD);
        batch.draw(pxl, barX, barY, 1 * HungerComponent.hungerPoints / HungerComponent.maxHungerPoints, 0.2f);
        batch.setColor(Color.WHITE);
    }    
}

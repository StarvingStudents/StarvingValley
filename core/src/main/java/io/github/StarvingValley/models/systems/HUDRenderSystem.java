package io.github.StarvingValley.models.systems;

import java.util.ArrayList;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.HudComponent;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.components.SizeComponent;
import io.github.StarvingValley.models.components.SpriteComponent;

public class HUDRenderSystem extends EntitySystem {
    private SpriteBatch batch;
    private BitmapFont font;

    public HUDRenderSystem() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.getData().setScale(4f);
    }

    @Override
    public void update(float deltaTime) {
        ImmutableArray<Entity> entities = getEngine().getEntitiesFor(
                Family.all(PositionComponent.class, SizeComponent.class, HudComponent.class).get());

        ArrayList<Entity> sortedEntities = new ArrayList<>(entities.size());
        for (int i = 0; i < entities.size(); i++) {
            sortedEntities.add(entities.get(i));
        }

        batch.begin();

        for (Entity entity : sortedEntities) {
            PositionComponent pos = Mappers.position.get(entity);
            SizeComponent size = Mappers.size.get(entity);

            if (Mappers.hidden.has(entity))
                continue;

            if (Mappers.sprite.has(entity)) {
                SpriteComponent sprite = Mappers.sprite.get(entity);
                if (sprite.sprite.getTexture() != null) {
                    sprite.sprite.setPosition(pos.position.x, pos.position.y);
                    sprite.sprite.setSize(size.width, size.height);
                    sprite.sprite.draw(batch);
                }
            }
        }

        batch.end();
    }
}

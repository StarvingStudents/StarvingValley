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
import io.github.StarvingValley.models.components.TextComponent;

public class HudRenderSystem extends EntitySystem {
    private SpriteBatch batch;
    private BitmapFont font;

    public HudRenderSystem() {
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

        // First render items being dragged, then items, then inventory slots.
        // TODO: Maybe this would be cleaner with a z-index
        sortedEntities.sort((a, b) -> {
            boolean aDragging = Mappers.dragging.has(a);
            boolean bDragging = Mappers.dragging.has(b);

            boolean aSlot = Mappers.inventorySlot.has(a);
            boolean bSlot = Mappers.inventorySlot.has(b);

            if (aDragging && !bDragging)
                return 1;
            if (!aDragging && bDragging)
                return -1;

            if (aSlot && !bSlot)
                return -1;
            if (!aSlot && bSlot)
                return 1;

            return 0;
        });

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

            if (Mappers.text.has(entity)) {
                TextComponent text = Mappers.text.get(entity);
                font.draw(batch,
                        text.text,
                        pos.position.x + text.offsetX,
                        pos.position.y + text.offsetY);
            }
        }

        batch.end();
    }
}

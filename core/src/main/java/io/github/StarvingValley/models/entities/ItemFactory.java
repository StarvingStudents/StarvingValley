package io.github.StarvingValley.models.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Engine;
import io.github.StarvingValley.models.components.ItemComponent;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.components.SpriteComponent;
import java.util.UUID;

public class ItemFactory {
    private final Engine engine;

    public ItemFactory(Engine engine) {
        this.engine = engine;
    }

    public Entity createItem(float x, float y, String itemName, String spriteTexture) {
        Entity item = engine.createEntity();

        // Create and configure the ItemComponent
        ItemComponent itemComponent = engine.createComponent(ItemComponent.class);
        itemComponent.id = UUID.randomUUID().toString(); // Generate unique id and convert to string
        itemComponent.itemName = itemName;
        item.add(itemComponent);

        PositionComponent position = engine.createComponent(PositionComponent.class);
        position.position.x = x;
        position.position.y = y;
        item.add(position);

        SpriteComponent sprite = engine.createComponent(SpriteComponent.class);
        item.add(sprite);

        engine.addEntity(item);
        return item;
    }
}

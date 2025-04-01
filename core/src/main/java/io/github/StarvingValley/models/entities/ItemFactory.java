package io.github.StarvingValley.models.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Engine;
import io.github.StarvingValley.models.components.ItemComponent;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.components.SpriteComponent;

public class ItemFactory {
    private final Engine engine;

    public ItemFactory(Engine engine) {
        this.engine = engine;
    }

    public Entity createItem(float x, float y, String itemName, String spriteTexture) {
        Entity item = engine.createEntity();

        PositionComponent position = engine.createComponent(PositionComponent.class);
        position.position.x = x; // Access the x field of the Vector2
        position.position.y = y; // Access the y field of the Vector2
        item.add(position);

        SpriteComponent sprite = engine.createComponent(SpriteComponent.class);
        // ... Initialize sprite using spriteTexture ...
        item.add(sprite);

        ItemComponent itemComponent = engine.createComponent(ItemComponent.class);
        itemComponent.itemName = itemName;
        item.add(itemComponent);

        engine.addEntity(item);
        return item;
    }
}

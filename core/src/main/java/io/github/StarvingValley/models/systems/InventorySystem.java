package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import io.github.StarvingValley.models.components.InventoryComponent;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.utils.InventoryUtility;

public class InventorySystem extends IteratingSystem {

    private ComponentMapper<InventoryComponent> inventoryMapper;
    private ComponentMapper<PositionComponent> positionMapper;

    public InventorySystem() {
        super(Family.all(InventoryComponent.class).get());
        inventoryMapper = ComponentMapper.getFor(InventoryComponent.class);
        positionMapper = ComponentMapper.getFor(PositionComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        InventoryComponent inventory = inventoryMapper.get(entity);
        // Add your inventory logic here (e.g., handling item pickup, usage, consuming of items, etc.)
        //Adding items: When the player interacts with an item in the world.
        //Removing items: When the player uses or drops an item.
        //Checking inventory: For example, before the player can use an item, you'd check if it's in their inventory.
    }

    public void addItem(Entity entity, String item) {
        if (InventoryUtility.addItem(entity, item)) {
            System.out.println("Added item '" + item + "' to inventory of entity: " + entity);
        } else {
            System.out.println("Failed to add item. Entity may lack InventoryComponent: " + entity);
        }
    }

    public void removeItem(Entity entity, String item) {
        if (InventoryUtility.removeItem(entity, item)) {
            System.out.println("Removed item '" + item + "' from inventory of entity: " + entity);
        } else {
            System.out.println("Failed to remove item. Either item missing or no InventoryComponent: " + entity);
        }
    }
}

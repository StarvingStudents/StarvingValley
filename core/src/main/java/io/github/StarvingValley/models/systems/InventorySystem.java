package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import io.github.StarvingValley.models.components.InventoryComponent;
import io.github.StarvingValley.models.components.PositionComponent;

public class InventorySystem extends IteratingSystem {

    private ComponentMapper<InventoryComponent> inventoryMapper;
    private ComponentMapper<PositionComponent> positionMapper; // Example

    public InventorySystem() {
        super(Family.all(InventoryComponent.class).get()); // Process entities with InventoryComponent
        inventoryMapper = ComponentMapper.getFor(InventoryComponent.class);
        positionMapper = ComponentMapper.getFor(PositionComponent.class); // Example
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        InventoryComponent inventory = inventoryMapper.get(entity);
        // Add your inventory logic here (e.g., handling item pickup, usage, etc.)
        // For now, we'll leave it empty and expand in later steps.
        //Adding items: When the player interacts with an item in the world.
        //Removing items: When the player uses or drops an item.
        //Checking inventory: For example, before the player can use an item, you'd check if it's in their inventory.
    }

    // Method to add an item to an entity's inventory
    public void addItem(Entity entity, String item) {
        InventoryComponent inventory = inventoryMapper.get(entity);
        if (inventory != null) {
            inventory.addItem(item);
            System.out.println("Added item '" + item + "' to inventory of entity: " + entity); // For debugging
        } else {
            System.out.println("Entity does not have an inventory: " + entity); // For debugging
        }
    }

    // Method to remove an item from an entity's inventory
    public void removeItem(Entity entity, String item) {
        InventoryComponent inventory = inventoryMapper.get(entity);
        if (inventory != null) {
            inventory.removeItem(item);
            System.out.println("Removed item '" + item + "' from inventory of entity: " + entity); // For debugging
        } else {
            System.out.println("Entity does not have an inventory: " + entity); // For debugging
        }
    }
}

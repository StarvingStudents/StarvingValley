package io.github.StarvingValley.models.systems;

import java.util.List;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import io.github.StarvingValley.models.components.FoodItemComponent;
import io.github.StarvingValley.models.components.HungerComponent;
import io.github.StarvingValley.models.components.InventoryItemComponent;
import io.github.StarvingValley.models.components.InventorySelectedItemComponent;
import io.github.StarvingValley.models.events.EatingButtonPressedEvent;
import io.github.StarvingValley.models.events.EventBus;

public class EatingSystem extends IteratingSystem {
    private EventBus eventBus;

    public EatingSystem(EventBus eventBus) {
        super(Family.all(HungerComponent.class).get());
        this.eventBus = eventBus;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        List<EatingButtonPressedEvent> events = eventBus.getEvents(EatingButtonPressedEvent.class);
        if (events.size() > 0) {

            // Get entity with HungerComponent:
            Entity playerEntity = getEngine().getEntitiesFor(Family.all(HungerComponent.class).get()).first();
            HungerComponent hunger = playerEntity.getComponent(HungerComponent.class);

            // TODO: Combine with inventory system
            // Held item should have InventorySelectedItemComponent
            // Edible items should have FoodItemComponent

            // Check that there is at least one selected item:
            if (getEngine()
                    .getEntitiesFor(
                            Family.all(InventorySelectedItemComponent.class, InventoryItemComponent.class).get())
                    .size() == 0) {
                return;
            }

            // Get the item with InventorySelectedItemComponent and InventoryItemComponent
            Entity selectedItemEntity = getEngine()
                    .getEntitiesFor(
                            Family.all(InventorySelectedItemComponent.class,
                                    InventoryItemComponent.class).get())
                    .first();

            // Check if selectedItemEntity has FoodItemComponent:
            if (selectedItemEntity.getComponent(FoodItemComponent.class) != null) {
                // Get the food points from the selected item:
                FoodItemComponent foodItem = selectedItemEntity.getComponent(FoodItemComponent.class);
                float foodPoints = foodItem.foodPoints;

                // Update hunger points:
                hunger.hungerPoints = Math.min(hunger.maxHungerPoints, hunger.hungerPoints +
                        foodPoints);

                // TODO : Remove the consumed food item from the inventory:
            }
        }
    }
}

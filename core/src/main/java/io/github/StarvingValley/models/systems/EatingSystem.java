package io.github.StarvingValley.models.systems;

import java.util.List;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;

import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.FoodItemComponent;
import io.github.StarvingValley.models.components.HungerComponent;
import io.github.StarvingValley.models.components.InventoryItemComponent;
import io.github.StarvingValley.models.components.InventorySelectedItemComponent;
import io.github.StarvingValley.models.components.SelectedHotbarEntryComponent;
import io.github.StarvingValley.models.entities.EntityFactoryRegistry;
import io.github.StarvingValley.models.events.EatingButtonPressedEvent;
import io.github.StarvingValley.models.events.EventBus;
import io.github.StarvingValley.models.events.RemoveItemFromInventoryEvent;
import io.github.StarvingValley.models.types.GameContext;
import io.github.StarvingValley.models.entities.BuildPreviewFactory;

public class EatingSystem extends IteratingSystem {
    private EventBus eventBus;
    private GameContext context;

    public EatingSystem(EventBus eventBus, GameContext context) {
        super(Family.all(HungerComponent.class).get());
        this.eventBus = eventBus;
        this.context = context;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        List<EatingButtonPressedEvent> events = eventBus.getEvents(EatingButtonPressedEvent.class);
        if (events.size() > 0) {
            System.out.println("Processing eating event");

            Entity playerEntity = context.player;
            // HungerComponent hunger = playerEntity.getComponent(HungerComponent.class);

            // TODO: Combine with inventory system
            // Held item should have SelectedHotbarEntryComponent
            // Edible items should have FoodItemComponent (foodItem)

            ImmutableArray<Entity> selectedItemEntities = getEngine()
                    .getEntitiesFor(
                            Family.all(InventoryItemComponent.class, SelectedHotbarEntryComponent.class).get());

            if (selectedItemEntities.size() == 0) {
                System.out.println("No selected item entity found");
                return;
            }

            Entity selectedItemEntity = selectedItemEntities.first();

            if (!Mappers.foodItem.has(selectedItemEntity)) {
                System.out.println("Selected item is not food");
                return;
            }

            FoodItemComponent foodItem = Mappers.foodItem.get(selectedItemEntity);

            HungerComponent hunger = playerEntity.getComponent(HungerComponent.class);

            hunger.hungerPoints = Math.min(hunger.maxHungerPoints, hunger.hungerPoints + foodItem.foodPoints);

            InventoryItemComponent inventoryItem = Mappers.inventoryItem.get(selectedItemEntity);
            context.eventBus.publish(new RemoveItemFromInventoryEvent(playerEntity, inventoryItem.type, 1));
            System.out.println("Ate and removed item from inventory: " + inventoryItem.type);

            // Check that that entity has foodItem component
            // Entity prototype = EntityFactoryRegistry
            // .create(selectedItemEntity.getComponent(InventoryItemComponent.class).type);
            // if (!Mappers.foodItem.has(prototype)) {
            // return;
            // }

            // // Get the food points from the selected item:
            // FoodItemComponent foodItem =
            // selectedItemEntity.getComponent(FoodItemComponent.class);
            // float foodPoints = foodItem.foodPoints;

            // // Update hunger points:
            // hunger.hungerPoints = Math.min(hunger.maxHungerPoints, hunger.hungerPoints +
            // foodPoints);

            // TODO : Remove the consumed food item from the inventory:
        }
    }
}

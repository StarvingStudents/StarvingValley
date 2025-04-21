package io.github.StarvingValley.models.systems;

import java.util.List;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;

import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.FoodItemComponent;
import io.github.StarvingValley.models.components.HungerComponent;
import io.github.StarvingValley.models.components.InventoryItemComponent;
import io.github.StarvingValley.models.components.SelectedHotbarEntryComponent;
import io.github.StarvingValley.models.entities.EntityFactoryRegistry;
import io.github.StarvingValley.models.events.EatingButtonPressedEvent;
import io.github.StarvingValley.models.events.EventBus;
import io.github.StarvingValley.models.events.RemoveItemFromInventoryEvent;
import io.github.StarvingValley.models.types.GameContext;
import io.github.StarvingValley.models.types.PrefabType;
import io.github.StarvingValley.utils.InventoryUtils;

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
            Engine engine = getEngine();

            ImmutableArray<Entity> selectedAnyItems = engine
                    .getEntitiesFor(Family.all(SelectedHotbarEntryComponent.class, InventoryItemComponent.class).get());

            if (selectedAnyItems.size() > 0
                    && (selectedAnyItems.get(0).getComponent(InventoryItemComponent.class).type == PrefabType.WHEAT
                            || selectedAnyItems.get(0)
                                    .getComponent(InventoryItemComponent.class).type == PrefabType.BEETROOT)) {

                Entity playerEntity = context.player;
                HungerComponent hunger = playerEntity.getComponent(HungerComponent.class);
                Entity selectedFoodItem = selectedAnyItems.get(0);

                InventoryItemComponent item = Mappers.inventoryItem.get(selectedFoodItem);

                Entity prototype = EntityFactoryRegistry
                        .create(selectedFoodItem.getComponent(InventoryItemComponent.class).type);
                if (!Mappers.foodItem.has(prototype)) {
                    return;
                }

                float foodPoints = 0;

                // Get the food points from the selected item:
                if (selectedFoodItem.getComponent(InventoryItemComponent.class).type == PrefabType.WHEAT) {
                    foodPoints = prototype.getComponent(FoodItemComponent.class).foodPoints;
                } else if (selectedFoodItem.getComponent(InventoryItemComponent.class).type == PrefabType.BEETROOT) {
                    foodPoints = prototype.getComponent(FoodItemComponent.class).foodPoints;
                }
                System.out.println(foodPoints);

                // Update hunger points:
                hunger.hungerPoints = Math.min(hunger.maxHungerPoints, hunger.hungerPoints +
                        foodPoints);

                context.eventBus.publish(new RemoveItemFromInventoryEvent(context.player,
                        selectedFoodItem.getComponent(InventoryItemComponent.class).type, 1));

                if (item.quantity <= 1) {
                    engine.removeEntity(selectedFoodItem);
                    InventoryUtils.unselectSelectedHotbarItems(engine);
                }
            } else if (selectedAnyItems.size() == 0) {
                InventoryUtils.unselectSelectedHotbarItems(engine);
            }
        }
    }
}

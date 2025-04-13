package io.github.StarvingValley.models.systems;

import java.util.List;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import io.github.StarvingValley.models.components.ButtonComponent;
import io.github.StarvingValley.models.components.ClickableComponent;
import io.github.StarvingValley.models.components.ClickedComponent;
import io.github.StarvingValley.models.components.EatingComponent;
import io.github.StarvingValley.models.components.HungerComponent;
import io.github.StarvingValley.models.events.EatingButtonPressedEvent;
import io.github.StarvingValley.models.events.EntityUpdatedEvent;
import io.github.StarvingValley.models.events.EventBus;
import io.github.StarvingValley.utils.DiffUtils;

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
            System.out.println(events.get((0)));

            // HungerComponent hunger = entity.getComponent(HungerComponent.class);
            // float oldHunger = hunger.hungerPoints;

            // hunger.hungerPoints = Math.min(hunger.maxHungerPoints, hunger.hungerPoints +
            // events.get(0).foodPoints);

            // if (DiffUtils.hasChanged(hunger.hungerPoints, oldHunger))
            // eventBus.publish(new EntityUpdatedEvent(entity));
        }
    }
}

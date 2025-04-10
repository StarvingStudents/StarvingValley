package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import io.github.StarvingValley.models.components.EatingComponent;
import io.github.StarvingValley.models.components.HungerComponent;
import io.github.StarvingValley.models.events.EntityUpdatedEvent;
import io.github.StarvingValley.models.events.EventBus;
import io.github.StarvingValley.utils.DiffUtils;

public class EatingSystem extends IteratingSystem {
    private EventBus eventBus;

    public EatingSystem(EventBus eventBus) {
        super(Family.all(HungerComponent.class, EatingComponent.class).get());
        this.eventBus = eventBus;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        HungerComponent hunger = entity.getComponent(HungerComponent.class);
        EatingComponent eating = entity.getComponent(EatingComponent.class);

        float oldHunger = hunger.hungerPoints;

        hunger.hungerPoints = Math.min(hunger.maxHungerPoints, hunger.hungerPoints + eating.foodPoints); 

        if (DiffUtils.hasChanged(hunger.hungerPoints, oldHunger))
            eventBus.publish(new EntityUpdatedEvent(entity));
    }
}

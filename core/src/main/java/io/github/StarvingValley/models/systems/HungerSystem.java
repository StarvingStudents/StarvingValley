package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import io.github.StarvingValley.models.components.ActiveWorldEntityComponent;
import io.github.StarvingValley.models.components.HungerComponent;
import io.github.StarvingValley.models.events.EntityUpdatedEvent;
import io.github.StarvingValley.models.events.EventBus;
import io.github.StarvingValley.models.events.RespawnEvent;
import io.github.StarvingValley.utils.DiffUtils;

public class HungerSystem extends IteratingSystem {
  private EventBus eventBus;

  public HungerSystem(EventBus eventBus) {
    super(Family.all(HungerComponent.class, ActiveWorldEntityComponent.class).get());
    this.eventBus = eventBus;
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    HungerComponent hunger = entity.getComponent(HungerComponent.class);

    float oldHunger = hunger.hungerPoints;

    hunger.hungerPoints -= hunger.decayRate * deltaTime;
    if (hunger.hungerPoints <= 0) {
      eventBus.publish(new RespawnEvent(entity));
    }

    // Don't need to sync on every decimal change
    if (DiffUtils.hasChanged((int) hunger.hungerPoints, (int) oldHunger))
      eventBus.publish(new EntityUpdatedEvent(entity));
  }
}

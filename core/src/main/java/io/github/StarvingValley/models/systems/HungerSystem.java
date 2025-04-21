package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import io.github.StarvingValley.models.components.ActiveWorldEntityComponent;
import io.github.StarvingValley.models.components.HungerComponent;
import io.github.StarvingValley.models.events.EntityUpdatedEvent;
import io.github.StarvingValley.models.events.RespawnEvent;
import io.github.StarvingValley.models.types.GameContext;

public class HungerSystem extends IteratingSystem {
  private GameContext context;

  public HungerSystem(GameContext context) {
    super(Family.all(HungerComponent.class, ActiveWorldEntityComponent.class).get());
    this.context = context;
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    HungerComponent hunger = entity.getComponent(HungerComponent.class);

    float oldHunger = hunger.hungerPoints;

    hunger.hungerPoints -= hunger.decayRate * deltaTime;
    if (hunger.hungerPoints <= 0) {
      context.eventBus.publish(new RespawnEvent(entity));
    }

    // Don't need to sync on every decimal change
    if ((int) hunger.hungerPoints != (int) oldHunger)
      context.eventBus.publish(new EntityUpdatedEvent(entity));
  }
}

package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.HungerComponent;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.events.EventBus;
import io.github.StarvingValley.models.events.RespawnEvent;
import java.util.List;

public class RespawnSystem extends EntitySystem {
  private final EventBus eventBus;

  public RespawnSystem(EventBus eventBus) {
    this.eventBus = eventBus;
  }

  @Override
  public void update(float deltaTime) {
    List<RespawnEvent> respawnEvents = eventBus.getEvents(RespawnEvent.class);

    for (RespawnEvent respawnEvent : respawnEvents) {
      handleRespawn(respawnEvent);
    }
  }

  private void handleRespawn(RespawnEvent event) {
    Entity entity = event.getEntity();

    resetPlayerState(entity);
  }

  private void resetPlayerState(Entity player) {
    PositionComponent position = Mappers.position.get(player);
    position.position.set(35, 15, 100);

    HungerComponent hunger = Mappers.hunger.get(player);
    hunger.hungerPoints = hunger.maxHungerPoints;
  }
}

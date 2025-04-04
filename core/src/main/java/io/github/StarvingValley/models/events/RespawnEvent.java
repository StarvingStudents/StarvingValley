package io.github.StarvingValley.models.events;

import com.badlogic.ashley.core.Entity;

public class RespawnEvent extends EntityEvent {
  public RespawnEvent(Entity entity) {
    super(entity);
  }
}

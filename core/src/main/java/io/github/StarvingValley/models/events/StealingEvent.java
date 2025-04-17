package io.github.StarvingValley.models.events;

import com.badlogic.ashley.core.Entity;

public class StealingEvent extends EntityEvent {
  public StealingEvent(Entity entity) {
    super(entity);
  }
}

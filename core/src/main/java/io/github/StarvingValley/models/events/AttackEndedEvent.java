package io.github.StarvingValley.models.events;

import com.badlogic.ashley.core.Entity;

public class AttackEndedEvent extends EntityEvent {
  public AttackEndedEvent(Entity entity) {
    super(entity);
  }
}

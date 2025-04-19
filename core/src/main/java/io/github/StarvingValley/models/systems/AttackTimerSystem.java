package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import io.github.StarvingValley.controllers.StarvingValley;
import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.AttackComponent;
import io.github.StarvingValley.models.events.AttackEndedEvent;
import io.github.StarvingValley.models.events.EventBus;
import io.github.StarvingValley.models.types.ScreenType;

public class AttackTimerSystem extends IteratingSystem {

  private final EventBus eventBus;
  private float logTimer = 0f;
  private StarvingValley game;

  public AttackTimerSystem(EventBus eventBus, StarvingValley game) {
    super(Family.all(AttackComponent.class).get());
    this.eventBus = eventBus;
    this.game = game;
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    AttackComponent attack = Mappers.attack.get(entity);
    attack.timeRemaining -= deltaTime;

    logTimer += deltaTime;
    if (logTimer >= 1f) {
      System.out.println("Attack time remaining: " + Math.max(0, attack.timeRemaining) + "s");
      logTimer = 0f;
    }

    if (attack.timeRemaining <= 0) {
      attack.timeRemaining = 0;
      entity.remove(AttackComponent.class);
      eventBus.publish(new AttackEndedEvent(entity));

      game.requestViewSwitch(ScreenType.FARM);
    }
  }
}

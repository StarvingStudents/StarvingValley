package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.AttackComponent;
import io.github.StarvingValley.models.events.AttackEndedEvent;
import io.github.StarvingValley.models.events.ScreenTransitionEvent;
import io.github.StarvingValley.models.types.GameContext;
import io.github.StarvingValley.models.types.ScreenType;

public class AttackTimerSystem extends IteratingSystem {
  private float logTimer = 0f;
  private GameContext context;

  public AttackTimerSystem(GameContext context) {
    super(Family.all(AttackComponent.class).get());
    this.context = context;
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
      // TODO: This should reset the players position to default_spawn_location, but
      // they way screen transitions are done this would get overriden, even if i
      // force fbSync.updateInterval because player doesn't immediately get
      // UnsyncedComponent in the engine even if i add it here directly. Only reason
      // it works when transitioning between farm and village is because the player is
      // moving so it already has unsyncedcomponent.
      // Can't fix it right now so the player will just
      // spawn on the same spot they were. Might spawn inside walls and get stuck tho

      context.eventBus.publish(new AttackEndedEvent(entity));
      context.eventBus.publish(new ScreenTransitionEvent(ScreenType.FARM));
    }
  }
}

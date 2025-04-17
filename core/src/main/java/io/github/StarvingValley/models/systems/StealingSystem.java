package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import io.github.StarvingValley.config.Config;
import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.ClickedComponent;
import io.github.StarvingValley.models.components.CropTypeComponent;
import io.github.StarvingValley.models.components.EconomyComponent;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.components.TimeToGrowComponent;
import io.github.StarvingValley.models.events.StealingEvent;
import io.github.StarvingValley.models.types.GameContext;

public class StealingSystem extends IteratingSystem {

  private final GameContext context;

  public StealingSystem(GameContext context) {
    super(
        Family.all(ClickedComponent.class, PositionComponent.class, CropTypeComponent.class).get());
    this.context = context;
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    if (context.player == null) return;

    EconomyComponent economy = Mappers.economy.get(context.player);

    TimeToGrowComponent crop = Mappers.timeToGrow.get(entity);
    if (!crop.isMature()) return;

    PositionComponent playerPos = Mappers.position.get(context.player);
    PositionComponent entityPos = Mappers.position.get(entity);

    float distance = playerPos.position.dst(entityPos.position);
    if (distance > Config.DEFAULT_ATTACK_RANGE) return;

    economy.balance += 10; // TODO: should vary based on type
    System.out.println(economy.balance);

    context.eventBus.publish(new StealingEvent(entity));
    getEngine().removeEntity(entity);
  }
}

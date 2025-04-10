package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.ActiveWorldEntityComponent;
import io.github.StarvingValley.models.components.BuildPreviewComponent;
import io.github.StarvingValley.models.components.BuildableComponent;
import io.github.StarvingValley.models.components.ClickedComponent;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.entities.EntityFactoryRegistry;
import io.github.StarvingValley.models.events.EntityAddedEvent;
import io.github.StarvingValley.models.events.EntityPlacedEvent;
import io.github.StarvingValley.models.types.GameContext;

public class BuildPlacementSystem extends IteratingSystem {
  private GameContext context;

  public BuildPlacementSystem(GameContext context) {
    super(
        Family.all(
            BuildPreviewComponent.class,
            ClickedComponent.class,
            BuildableComponent.class,
            PositionComponent.class)
            .get());
    this.context = context;
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    BuildPreviewComponent buildPreview = Mappers.buildPreview.get(entity);

    if (buildPreview.isBlocked) {
      return;
    }

    Engine engine = getEngine();

    BuildableComponent buildable = Mappers.buildable.get(entity);
    PositionComponent position = Mappers.position.get(entity);

    Entity entityToPlace = EntityFactoryRegistry.create(buildable.builds);
    entityToPlace.add(new ActiveWorldEntityComponent());
    entityToPlace.add(new PositionComponent(position.position.x, position.position.y));

    engine.addEntity(entityToPlace);
    engine.removeEntity(entity);

    // TODO: Inventory should listen to this, and remove the entity/lower
    // counter from the inventory. If there's no more items of this type to place,
    // exit build mode by removing the buildpreview entity from the engine.
    // Inventory system could probably send an event to this system to tell it to
    // stop placing that item. Then that inventory/publisher should be before
    // BuildPlacementSystem in the engine
    context.eventBus.publish(new EntityPlacedEvent(entityToPlace));
    context.eventBus.publish(new EntityAddedEvent(entityToPlace));
  }
}

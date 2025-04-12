package io.github.StarvingValley.models.systems;

import java.util.List;
import java.util.function.Supplier;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;

import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.ClickableComponent;
import io.github.StarvingValley.models.components.ClickedComponent;
import io.github.StarvingValley.models.components.DragEndComponent;
import io.github.StarvingValley.models.components.DraggableComponent;
import io.github.StarvingValley.models.components.DraggingComponent;
import io.github.StarvingValley.models.components.HudComponent;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.components.SizeComponent;
import io.github.StarvingValley.models.events.DragEndEvent;
import io.github.StarvingValley.models.events.DragStartEvent;
import io.github.StarvingValley.models.events.TapEvent;
import io.github.StarvingValley.models.types.GameContext;
import io.github.StarvingValley.utils.TileUtils;

public class InputSystem extends EntitySystem {
  private final GameContext context;

  public InputSystem(GameContext context) {
    this.context = context;
  }

  @Override
  public void update(float deltaTime) {
    processTapEvents();
    processDragStartEvents();
    processDragEndEvents();
  }

  private void processTapEvents() {
    List<TapEvent> tapEvents = context.eventBus.getEvents(TapEvent.class);

    ImmutableArray<Entity> clickableHudEntities = getEngine().getEntitiesFor(
        Family.all(PositionComponent.class, SizeComponent.class, ClickableComponent.class, HudComponent.class).get());

    ImmutableArray<Entity> clickableWorldEntities = getEngine().getEntitiesFor(
        Family.all(PositionComponent.class, SizeComponent.class, ClickableComponent.class)
            .exclude(HudComponent.class).get());

    for (TapEvent event : tapEvents) {
      boolean handledHud = attachComponentIfOverlapping(clickableHudEntities, event.screenPos, ClickedComponent::new);
      if (!handledHud) {
        attachComponentIfOverlapping(clickableWorldEntities, event.tile, ClickedComponent::new);
      }
    }
  }

  private void processDragStartEvents() {
    List<DragStartEvent> dragEvents = context.eventBus.getEvents(DragStartEvent.class);

    ImmutableArray<Entity> draggableHudEntities = getEngine().getEntitiesFor(
        Family.all(PositionComponent.class, SizeComponent.class, DraggableComponent.class, HudComponent.class).get());

    ImmutableArray<Entity> draggableWorldEntities = getEngine().getEntitiesFor(
        Family.all(PositionComponent.class, SizeComponent.class, DraggableComponent.class)
            .exclude(HudComponent.class).get());

    for (DragStartEvent event : dragEvents) {
      Supplier<Component> component = () -> new DraggingComponent(
          (int) event.screenPos.x, (int) event.screenPos.y,
          event.tile.x, event.tile.y);

      boolean handledHud = attachComponentIfOverlapping(draggableHudEntities, event.screenPos, component);
      if (handledHud)
        continue;

      attachComponentIfOverlapping(draggableWorldEntities, event.tile, component);
    }
  }

  private void processDragEndEvents() {
    List<DragEndEvent> dragEndEvents = context.eventBus.getEvents(DragEndEvent.class);
    if (dragEndEvents.isEmpty())
      return;

    DragEndEvent event = dragEndEvents.get(0);

    ImmutableArray<Entity> draggedEntities = getEngine().getEntitiesFor(
        Family.all(DraggingComponent.class).exclude(DragEndComponent.class).get());

    for (Entity entity : draggedEntities) {
      entity.add(new DragEndComponent(
          (int) event.screenPos.x, (int) event.screenPos.y,
          event.tile.x, event.tile.y));
    }
  }

  private boolean attachComponentIfOverlapping(
      ImmutableArray<Entity> entities,
      GridPoint2 tile,
      Supplier<Component> componentSupplier) {
    boolean handled = false;
    for (Entity entity : entities) {
      PositionComponent pos = Mappers.position.get(entity);
      SizeComponent size = Mappers.size.get(entity);

      if (TileUtils.isOverlappingTile(pos.position.x, pos.position.y, size.width, size.height, tile)) {
        entity.add(componentSupplier.get());
        handled = true;
      }
    }
    return handled;
  }

  private boolean attachComponentIfOverlapping(
      ImmutableArray<Entity> entities,
      Vector2 screenPos,
      Supplier<Component> componentSupplier) {
    boolean handled = false;
    for (Entity entity : entities) {
      PositionComponent pos = Mappers.position.get(entity);
      SizeComponent size = Mappers.size.get(entity);

      boolean isInside = screenPos.x >= pos.position.x &&
          screenPos.x <= pos.position.x + size.width &&
          screenPos.y >= pos.position.y &&
          screenPos.y <= pos.position.y + size.height;

      if (isInside) {
        entity.add(componentSupplier.get());
        handled = true;
      }
    }
    return handled;
  }
}

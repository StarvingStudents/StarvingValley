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
import io.github.StarvingValley.models.components.ButtonComponent;
import io.github.StarvingValley.models.components.ClickableComponent;
import io.github.StarvingValley.models.components.ClickedComponent;
import io.github.StarvingValley.models.components.PickupButtonComponent;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.components.SizeComponent;
import io.github.StarvingValley.models.events.TapEvent;
import io.github.StarvingValley.models.types.GameContext;
import io.github.StarvingValley.utils.TileUtils;
import io.github.StarvingValley.models.components.SpriteComponent;

public class HUDButtonPressSystem extends EntitySystem {
  private GameContext context;

  public HUDButtonPressSystem(GameContext context) {
    this.context = context;
  }

  @Override
  public void update(float delta) {
    processTapEvents();
  }

  private void processTapEvents() {
    List<TapEvent> tapEvents = context.eventBus.getEvents(TapEvent.class);

    ImmutableArray<Entity> clickableHudEntities = getEngine().getEntitiesFor(
        Family
            .all(PositionComponent.class, SizeComponent.class, ClickableComponent.class, ButtonComponent.class,
                SpriteComponent.class)
            .get());

    ImmutableArray<Entity> clickableWorldEntities = getEngine().getEntitiesFor(
        Family.all(PositionComponent.class, SizeComponent.class, ClickableComponent.class)
            .exclude(ButtonComponent.class).get());

    for (TapEvent event : tapEvents) {
      boolean handledHud = attachComponentIfOverlapping(clickableHudEntities, event.screenPos, ClickedComponent::new);
      if (!handledHud) {
        attachComponentIfOverlapping(clickableWorldEntities, event.screenPos, ClickedComponent::new);
      }
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

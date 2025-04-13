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
    // List<TapEvent> tapEvents = context.eventBus.getEvents(TapEvent.class);

    // ImmutableArray<Entity> clickableEntities = getEngine()
    // .getEntitiesFor(
    // Family.all(ButtonComponent.class, ClickableComponent.class)
    // .get());

    // ImmutableArray<Entity> clickableWorldEntities = getEngine().getEntitiesFor(
    // Family.all(PositionComponent.class, SizeComponent.class,
    // ClickableComponent.class)
    // .exclude(ButtonComponent.class).get());

    // for (TapEvent clickEvent : events) {
    // for (Entity entity : clickableEntities) {
    // PositionComponent position = Mappers.position.get(entity);
    // SizeComponent size = Mappers.size.get(entity);

    // if (isOverlapping(position, size, clickEvent.tile)) {
    // // tile coordinate system as the button.
    // entity.add(new ClickedComponent());
    // }
    // }
    // }
    // }

    // for(TapEvent event:tapEvents)
    // {
    // boolean handledHud = attachComponentIfOverlapping(clickableEntities,
    // event.screenPos, ClickedComponent::new);
    // if (!handledHud) {
    // attachComponentIfOverlapping(clickableWorldEntities, event.tile,
    // ClickedComponent::new);
    // }
    // }
    // }

    processTapEvents();
  }

  private void processTapEvents() {
    List<TapEvent> tapEvents = context.eventBus.getEvents(TapEvent.class);

    ImmutableArray<Entity> clickableHudEntities = getEngine().getEntitiesFor(
        Family
            .all(PositionComponent.class, SizeComponent.class, ClickableComponent.class, ButtonComponent.class,
                SpriteComponent.class)
            .get());

    // Print out the clickable entities for debugging
    for (int i = 0; i < clickableHudEntities.size(); i++) {
      Entity entity = clickableHudEntities.get(i);
      System.out.println("Clickable HUD Entity: " + entity.getComponent(SpriteComponent.class).getTexturePath());
      // Print out position of entity
      PositionComponent position = Mappers.position.get(entity);
      SizeComponent size = Mappers.size.get(entity);
      System.out.println("Position: " + position.position.x + ", " + position.position.y);
      // System.out.println("Converted position: " + position.position.x * + ", " +
      // position.position.y);
      System.out.println("Size: " + size.width + ", " + size.height);
    }

    ImmutableArray<Entity> clickableWorldEntities = getEngine().getEntitiesFor(
        Family.all(PositionComponent.class, SizeComponent.class, ClickableComponent.class)
            .exclude(ButtonComponent.class).get());

    for (TapEvent event : tapEvents) {
      System.out.println("Tap events processed");

      boolean handledHud = attachComponentIfOverlapping(clickableHudEntities, event.screenPos, ClickedComponent::new);
      System.out.println("screenPos: " + event.screenPos);
      System.out.println("Handled HUD: " + handledHud);
      if (!handledHud) {
        attachComponentIfOverlapping(clickableWorldEntities, event.tile, ClickedComponent::new);
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

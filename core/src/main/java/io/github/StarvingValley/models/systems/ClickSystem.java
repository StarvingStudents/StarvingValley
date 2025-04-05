package io.github.StarvingValley.models.systems;

import java.util.List;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.ClickableComponent;
import io.github.StarvingValley.models.components.ClickedComponent;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.components.SizeComponent;
import io.github.StarvingValley.models.events.EventBus;
import io.github.StarvingValley.models.events.TapEvent;
import io.github.StarvingValley.utils.TileUtils;

public class ClickSystem extends EntitySystem {
  private final EventBus eventBus;

  public ClickSystem(EventBus eventBus) {
    this.eventBus = eventBus;
  }

  @Override
  public void update(float delta) {
    List<TapEvent> events = eventBus.getEvents(TapEvent.class);

    ImmutableArray<Entity> clickableEntities =
        getEngine()
            .getEntitiesFor(
                Family.all(PositionComponent.class, SizeComponent.class, ClickableComponent.class)
                    .get());

    for (TapEvent clickEvent : events) {
      for (Entity entity : clickableEntities) {
        PositionComponent position = Mappers.position.get(entity);
        SizeComponent size = Mappers.size.get(entity);

        if (TileUtils.isOverlappingTile(position.position.x, position.position.y, size.width, size.height,
            clickEvent.tile)) {
          entity.add(new ClickedComponent());
        }
      }
    }
  }
}

package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.ActiveWorldEntityComponent;
import io.github.StarvingValley.models.components.BuildPreviewComponent;
import io.github.StarvingValley.models.components.ClickedComponent;
import io.github.StarvingValley.models.components.PulseAlphaComponent;
import io.github.StarvingValley.models.components.SpriteComponent;
import io.github.StarvingValley.models.components.SyncComponent;
import io.github.StarvingValley.models.events.EntityPlacedEvent;
import io.github.StarvingValley.models.events.EntityUpdatedEvent;
import io.github.StarvingValley.models.events.EventBus;

public class BuildPlacementSystem extends IteratingSystem {
    private final EventBus eventBus;

    public BuildPlacementSystem(EventBus eventBus) {
      super(Family.all(BuildPreviewComponent.class, ClickedComponent.class).get());
      this.eventBus = eventBus;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
      BuildPreviewComponent buildPreview = Mappers.buildPreview.get(entity);

      if (buildPreview.isBlocked) {
        return;
      }

      entity.add(new ActiveWorldEntityComponent());
      entity.add(new SyncComponent());
      entity.remove(BuildPreviewComponent.class);
      entity.remove(PulseAlphaComponent.class);

      SpriteComponent sprite = Mappers.sprite.get(entity);
      if (sprite != null) {
        sprite.sprite.setAlpha(1);
      }

      // TODO: Inventory should listen to this, and remove the entity/lower
      // counter from the inventory. If there's no more items of this type to place,
      // exit build mode by removing the buildpreview entity from the engine.
      // Inventory system could probably send an event to this system to tell it to
      // stop placing that item. Then that inventory/publisher should be before
      // BuildPlacementSystem in the engine
      eventBus.publish(new EntityPlacedEvent(entity));
      eventBus.publish(new EntityUpdatedEvent(entity));
    }
}

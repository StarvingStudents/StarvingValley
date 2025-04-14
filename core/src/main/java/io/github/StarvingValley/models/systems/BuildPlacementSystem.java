package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;

import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.ActiveWorldEntityComponent;
import io.github.StarvingValley.models.components.BuildPreviewComponent;
import io.github.StarvingValley.models.components.BuildableComponent;
import io.github.StarvingValley.models.components.ClickedComponent;
import io.github.StarvingValley.models.components.InventoryItemComponent;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.components.SelectedHotbarItemComponent;
import io.github.StarvingValley.models.entities.EntityFactoryRegistry;
import io.github.StarvingValley.models.events.EntityAddedEvent;
import io.github.StarvingValley.models.events.EntityPlacedEvent;
import io.github.StarvingValley.models.events.ItemUsedEvent;
import io.github.StarvingValley.models.types.GameContext;
import io.github.StarvingValley.models.types.ItemStack;
import io.github.StarvingValley.utils.BuildUtils;

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

    context.eventBus.publish(new EntityPlacedEvent(entityToPlace));
    context.eventBus.publish(new EntityAddedEvent(entityToPlace));
    context.eventBus.publish(new ItemUsedEvent(new ItemStack(buildPreview.madeFromPrefabType, 1)));

    ImmutableArray<Entity> selectedHotbarItems = engine
        .getEntitiesFor(Family.all(SelectedHotbarItemComponent.class, InventoryItemComponent.class).get());
    if (selectedHotbarItems.size() > 0) {
      Entity selectedHotbarItem = selectedHotbarItems.get(0);

      InventoryItemComponent item = Mappers.inventoryItem.get(selectedHotbarItem);
      if (item.quantity <= 1) {
        engine.removeEntity(entity);
        BuildUtils.disableBuildPreview(engine);
      }
    } else if (selectedHotbarItems.size() == 0) {
      BuildUtils.disableBuildPreview(engine);
    }
  }
}

package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.ActiveWorldEntityComponent;
import io.github.StarvingValley.models.components.ClickedComponent;
import io.github.StarvingValley.models.components.GrowthStageComponent;
import io.github.StarvingValley.models.components.HarvestingComponent;
import io.github.StarvingValley.models.components.PlayerComponent;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.components.TimeToGrowComponent;
import io.github.StarvingValley.models.events.EntityRemovedEvent;
import io.github.StarvingValley.models.events.EventBus;

public class HarvestingSystem extends EntitySystem {
  private EventBus eventBus;

  public HarvestingSystem(EventBus eventBus) {
    this.eventBus = eventBus;
  }

  @Override
  public void update(float deltaTime) {
    Engine engine = getEngine();

    ImmutableArray<Entity> players = engine.getEntitiesFor(Family.all(PlayerComponent.class).get());
    if (players.size() == 0) {
      return;
    }

    Entity player = players.first();
    PositionComponent playerPos = Mappers.position.get(player);

    ImmutableArray<Entity> clickedCrops = engine
        .getEntitiesFor(Family.all(ClickedComponent.class, GrowthStageComponent.class, TimeToGrowComponent.class,
            PositionComponent.class, HarvestingComponent.class, ActiveWorldEntityComponent.class).get());

    for (Entity crop : clickedCrops) {
      GrowthStageComponent growthStageComponent = Mappers.growthStage.get(crop);
      TimeToGrowComponent timeToGrowComponent = Mappers.timeToGrow.get(crop);
      PositionComponent cropPos = Mappers.position.get(crop);
      HarvestingComponent harvestingComponent = Mappers.harvesting.get(crop);

      if (growthStageComponent.growthStage == 2
          && timeToGrowComponent.isMature()
          && harvestingComponent.canHarvest
          && isPlayerNearCrop(playerPos, cropPos)) {

        harvestCrop(crop, harvestingComponent);
      }
    }
  }

  private boolean isPlayerNearCrop(PositionComponent playerPos, PositionComponent cropPos) {
    float distance = playerPos.position.dst(cropPos.position);
    return distance < 100.03f; // need to test what works best, kinda wonky
  }

  private void harvestCrop(Entity crop, HarvestingComponent harvestingComponent) {
    Engine engine = getEngine();

    eventBus.publish(new EntityRemovedEvent(crop));
    // TODO: When we add inventory this should also publish a CropHarvestedEvent
    // that inventory or similar listens to

    engine.removeEntity(crop);

    System.out.println("Crop harvested");
  }
}

package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.ActiveWorldEntityComponent;
import io.github.StarvingValley.models.components.GrowthStageComponent;
import io.github.StarvingValley.models.components.HarvestingComponent;
import io.github.StarvingValley.models.components.PickupComponent;
import io.github.StarvingValley.models.components.PlayerComponent;
import io.github.StarvingValley.models.components.TimeToGrowComponent;
import io.github.StarvingValley.models.types.GameContext;

public class HarvestingSystem extends EntitySystem {
  private GameContext context;

  public HarvestingSystem(GameContext context) {
    this.context = context;
  }

  @Override
  public void update(float deltaTime) {
    Engine engine = getEngine();

    ImmutableArray<Entity> crops =
        engine.getEntitiesFor(
            Family.all(
                    GrowthStageComponent.class,
                    TimeToGrowComponent.class,
                    HarvestingComponent.class,
                    ActiveWorldEntityComponent.class)
                .get());

    for (Entity crop : crops) {
      GrowthStageComponent growthStageComponent = Mappers.growthStage.get(crop);
      TimeToGrowComponent timeToGrowComponent = Mappers.timeToGrow.get(crop);
      HarvestingComponent harvestingComponent = Mappers.harvesting.get(crop);

      // If crop is mature and doesn't have PickupComponent yet, add it
      if (growthStageComponent.growthStage == 3
          && timeToGrowComponent.isMature()
          && harvestingComponent.canHarvest
          && !Mappers.pickup.has(crop)) {
        crop.add(new PickupComponent(1.5f));
      }
    }
  }
}

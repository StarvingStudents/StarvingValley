package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.ActiveWorldEntityComponent;
import io.github.StarvingValley.models.components.ClickedComponent;
import io.github.StarvingValley.models.components.DropComponent;
import io.github.StarvingValley.models.components.GrowthStageComponent;
import io.github.StarvingValley.models.components.HarvestingComponent;
import io.github.StarvingValley.models.components.PlayerComponent;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.components.TimeToGrowComponent;
import io.github.StarvingValley.models.events.AddItemToInventoryEvent;
import io.github.StarvingValley.models.events.EntityRemovedEvent;
import io.github.StarvingValley.models.types.GameContext;
import io.github.StarvingValley.models.types.ItemStack;

public class HarvestingSystem extends EntitySystem {
  private GameContext context;

  public HarvestingSystem(GameContext context) {
    this.context = context;
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

    ImmutableArray<Entity> clickedCrops =
        engine.getEntitiesFor(
            Family.all(
                    ClickedComponent.class,
                    GrowthStageComponent.class,
                    TimeToGrowComponent.class,
                    PositionComponent.class,
                    HarvestingComponent.class,
                    ActiveWorldEntityComponent.class)
                .get());

    for (Entity crop : clickedCrops) {
      GrowthStageComponent growthStageComponent = Mappers.growthStage.get(crop);
      TimeToGrowComponent timeToGrowComponent = Mappers.timeToGrow.get(crop);
      PositionComponent cropPos = Mappers.position.get(crop);
      HarvestingComponent harvestingComponent = Mappers.harvesting.get(crop);

      if (growthStageComponent.growthStage == 3
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

    context.eventBus.publish(new EntityRemovedEvent(crop));

    // TODO: Should this be handled by pickupsystem? This could just check if crop
    // can be harvested and publish an event and pickup removes the entity and gives
    // player the drops
    DropComponent drops = Mappers.drop.get(crop);
    if (drops != null) {
      for (ItemStack drop : drops.drops) {
        context.eventBus.publish(new AddItemToInventoryEvent(drop));
        System.out.println("Dropped " + drop.quantity + " " + drop.type);
      }
    }

    engine.removeEntity(crop);

    System.out.println("Crop harvested");
  }
}

package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.ActiveWorldEntityComponent;
import io.github.StarvingValley.models.components.CropTypeComponent;
import io.github.StarvingValley.models.components.GrowthStageComponent;
import io.github.StarvingValley.models.components.PickupComponent;
import io.github.StarvingValley.models.components.SizeComponent;
import io.github.StarvingValley.models.components.SpriteComponent;
import io.github.StarvingValley.models.components.TimeToGrowComponent;
import io.github.StarvingValley.models.events.EntityUpdatedEvent;
import io.github.StarvingValley.models.types.GameContext;
import io.github.StarvingValley.config.Config;

public class CropGrowthSystem extends IteratingSystem {
  private GameContext context;

  public CropGrowthSystem(GameContext context) {
    super(
        Family.all(
            TimeToGrowComponent.class,
            GrowthStageComponent.class,
            SpriteComponent.class,
            CropTypeComponent.class,
            SizeComponent.class,
            ActiveWorldEntityComponent.class)
            .get());
    this.context = context;
  }

  @Override
  protected void processEntity(Entity cropEntity, float deltaTime) {
    TimeToGrowComponent growthTime = Mappers.timeToGrow.get(cropEntity);
    GrowthStageComponent growthStage = Mappers.growthStage.get(cropEntity);
    SpriteComponent spriteComponent = Mappers.sprite.get(cropEntity);
    CropTypeComponent cropType = Mappers.cropType.get(cropEntity);

    int prevGrowthStage = growthStage.growthStage;
    growthStage.growthStage = growthTime.getGrowthStage();
    if (prevGrowthStage != growthStage.growthStage) {
      context.eventBus.publish(new EntityUpdatedEvent(cropEntity));
    }

    switch (growthStage.growthStage) {
      case 1:
        if (cropType.cropType == CropTypeComponent.CropType.WHEAT) {
          spriteComponent.setTexturePath("Sprout Lands - Sprites - Basic pack\\Sprout Lands - Sprites - Basic pack\\wheat_1.png");
        } else if (cropType.cropType == CropTypeComponent.CropType.BEETROOT) {
          spriteComponent.setTexturePath("Sprout Lands - Sprites - Basic pack\\Sprout Lands - Sprites - Basic pack\\beetroot_1.png");
          // sizeComponent.height = 2f;
        }
        break;
      case 2:
        if (cropType.cropType == CropTypeComponent.CropType.WHEAT) {
          spriteComponent.setTexturePath("Sprout Lands - Sprites - Basic pack\\Sprout Lands - Sprites - Basic pack\\wheat_2.png");
          // sizeComponent.height = 2f;
        } else if (cropType.cropType == CropTypeComponent.CropType.BEETROOT) {
          spriteComponent.setTexturePath("Sprout Lands - Sprites - Basic pack\\Sprout Lands - Sprites - Basic pack\\beetroot_2.png");
        }
        break;
      case 3:
        if (cropType.cropType == CropTypeComponent.CropType.WHEAT) {
          spriteComponent.setTexturePath("Sprout Lands - Sprites - Basic pack\\Sprout Lands - Sprites - Basic pack\\wheat_3.png");
          cropEntity.add(new PickupComponent(Config.DEFAULT_PICKUP_RANGE));
          // sizeComponent.height = 2f;
        } else if (cropType.cropType == CropTypeComponent.CropType.BEETROOT) {
          spriteComponent.setTexturePath("Sprout Lands - Sprites - Basic pack\\Sprout Lands - Sprites - Basic pack\\beetroot_3.png");
          cropEntity.add(new PickupComponent(Config.DEFAULT_PICKUP_RANGE));
        }
        break;
      default:
        break;
    }
  }
}

package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.CropTypeComponent;
import io.github.StarvingValley.models.components.GrowthStageComponent;
import io.github.StarvingValley.models.components.SizeComponent;
import io.github.StarvingValley.models.components.SpriteComponent;
import io.github.StarvingValley.models.components.TimeToGrowComponent;
import io.github.StarvingValley.utils.SyncUtils;

public class CropGrowthSystem extends IteratingSystem {
  public CropGrowthSystem() {
    super(
        Family.all(
            TimeToGrowComponent.class,
            GrowthStageComponent.class,
            SpriteComponent.class,
            CropTypeComponent.class,
            SizeComponent.class)
            .get());
  }

  @Override
  protected void processEntity(Entity cropEntity, float deltaTime) {
    TimeToGrowComponent growthTime = Mappers.timeToGrow.get(cropEntity);
    GrowthStageComponent growthStage = Mappers.growthStage.get(cropEntity);
    SpriteComponent spriteComponent = Mappers.sprite.get(cropEntity);
    CropTypeComponent cropType = Mappers.cropType.get(cropEntity);
    SizeComponent sizeComponent = Mappers.size.get(cropEntity);

    growthTime.accumulateGrowth(deltaTime);

    if (growthTime.growthProgress >= growthTime.timeToGrow) {
      growthStage.growthStage = 2; // mature
    } else if (growthTime.growthProgress >= growthTime.timeToGrow * 0.50) {
      growthStage.growthStage = 1; // growing
    } else {
      growthStage.growthStage = 0; // sprout
    }

    switch (growthStage.growthStage) {
      case 1:
        if (cropType.cropType == CropTypeComponent.CropType.TOMATO) {
          spriteComponent.setTexturePath("tomato2.png");
        } else if (cropType.cropType == CropTypeComponent.CropType.POTATO) {
          spriteComponent.setTexturePath("potato2.png");
          sizeComponent.height = 2f;
        }
        break;
      case 2:
        if (cropType.cropType == CropTypeComponent.CropType.TOMATO) {
          spriteComponent.setTexturePath("tomato3.png");
          sizeComponent.height = 2f;
        } else if (cropType.cropType == CropTypeComponent.CropType.POTATO) {
          spriteComponent.setTexturePath("potato3.png");
        }
        break;
      default:
        break;
    }

    SyncUtils.markUnsynced(cropEntity);
  }
}

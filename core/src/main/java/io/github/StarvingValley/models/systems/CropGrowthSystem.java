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
import io.github.StarvingValley.models.events.EntityUpdatedEvent;
import io.github.StarvingValley.models.events.EventBus;

public class CropGrowthSystem extends IteratingSystem {
  private EventBus eventBus;

  public CropGrowthSystem(EventBus eventBus) {
    super(
        Family.all(
            TimeToGrowComponent.class,
            GrowthStageComponent.class,
            SpriteComponent.class,
            CropTypeComponent.class,
            SizeComponent.class)
            .get());
    this.eventBus = eventBus;
  }

  @Override
  protected void processEntity(Entity cropEntity, float deltaTime) {
    TimeToGrowComponent growthTime = Mappers.timeToGrow.get(cropEntity);
    GrowthStageComponent growthStage = Mappers.growthStage.get(cropEntity);
    SpriteComponent spriteComponent = Mappers.sprite.get(cropEntity);
    CropTypeComponent cropType = Mappers.cropType.get(cropEntity);
    SizeComponent sizeComponent = Mappers.size.get(cropEntity);

    growthTime.accumulateGrowth(deltaTime);

    // TODO: Maybe instead of accumulation growth, we can store planting-datetime
    // and just check time-diff? Then we don't need to sync so often. NB! We then would
    // need to add conditional sync on if growthStage has changed

    // Don't sync fully-grown crops
    if (growthStage.growthStage < 2) {
      eventBus.publish(new EntityUpdatedEvent(cropEntity));
    }

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
  }
}

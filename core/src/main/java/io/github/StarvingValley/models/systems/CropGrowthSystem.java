package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import io.github.StarvingValley.models.components.CropTypeComponent;
import io.github.StarvingValley.models.components.GrowthStageComponent;
import io.github.StarvingValley.models.components.SizeComponent;
import io.github.StarvingValley.models.components.SpriteComponent;
import io.github.StarvingValley.models.components.TimeToGrowComponent;

public class CropGrowthSystem extends IteratingSystem {
  private Texture growingTomato;
  private Texture matureTomato;
  private Texture growingPotato;
  private Texture maturePotato;

  public CropGrowthSystem() {
    super(
        Family.all(
                TimeToGrowComponent.class,
                GrowthStageComponent.class,
                SpriteComponent.class,
                CropTypeComponent.class,
                SizeComponent.class)
            .get());

    growingTomato = new Texture("tomato2.png");
    matureTomato = new Texture("tomato3.png");
    growingPotato = new Texture("potato2.png");
    maturePotato = new Texture("potato3.png");
  }

  @Override
  protected void processEntity(Entity cropEntity, float deltaTime) {
    TimeToGrowComponent growthTime = cropEntity.getComponent(TimeToGrowComponent.class);
    GrowthStageComponent growthStage = cropEntity.getComponent(GrowthStageComponent.class);
    SpriteComponent spriteComponent = cropEntity.getComponent(SpriteComponent.class);
    CropTypeComponent cropType = cropEntity.getComponent(CropTypeComponent.class);
    SizeComponent sizeComponent = cropEntity.getComponent(SizeComponent.class);

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
          spriteComponent.setSprite(new Sprite(growingTomato));
        } else if (cropType.cropType == CropTypeComponent.CropType.POTATO) {
          spriteComponent.setSprite(new Sprite(growingPotato));
          sizeComponent.height = 2f;
        }
        break;
      case 2:
        if (cropType.cropType == CropTypeComponent.CropType.TOMATO) {
          spriteComponent.setSprite(new Sprite(matureTomato));
          sizeComponent.height = 2f;
        } else if (cropType.cropType == CropTypeComponent.CropType.POTATO) {
          spriteComponent.setSprite(new Sprite(maturePotato));
        }
        break;
      default:
        break;
    }
  }
}

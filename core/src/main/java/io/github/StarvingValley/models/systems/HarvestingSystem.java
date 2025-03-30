package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.CameraComponent;
import io.github.StarvingValley.models.components.GrowthStageComponent;
import io.github.StarvingValley.models.components.HarvestingComponent;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.components.TimeToGrowComponent;

public class HarvestingSystem extends EntitySystem {
  private Entity player;

  public HarvestingSystem(Entity player) {
    this.player = player;
  }

  @Override
  public void update(float deltaTime) {
    PositionComponent playerPos = Mappers.position.get(player);

    Entity cameraEntity =
        getEngine().getEntitiesFor(Family.all(CameraComponent.class).get()).first();
    if (cameraEntity == null) {
      return;
    }

    CameraComponent cameraComponent = Mappers.camera.get(cameraEntity);
    if (cameraComponent == null) {
      return;
    }

    OrthographicCamera camera = cameraComponent.camera;

    if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
      float mouseX = Gdx.input.getX();
      float mouseY = Gdx.input.getY();

      Vector3 worldCoordinates = camera.unproject(new Vector3(mouseX, mouseY, 0));

      for (Entity crop :
          getEngine()
              .getEntitiesFor(
                  Family.all(
                          GrowthStageComponent.class,
                          TimeToGrowComponent.class,
                          PositionComponent.class,
                          HarvestingComponent.class)
                      .get())) {

        GrowthStageComponent growthStageComponent = Mappers.growthStage.get(crop);
        TimeToGrowComponent timeToGrowComponent = Mappers.timeToGrow.get(crop);
        PositionComponent cropPos = Mappers.position.get(crop);
        HarvestingComponent harvestingComponent = Mappers.harvesting.get(crop);

        if (growthStageComponent.growthStage == 2
            && timeToGrowComponent.isMature()
            && harvestingComponent.canHarvest
            && !harvestingComponent.isHarvested) {
          if (isPlayerNearCrop(playerPos, cropPos)
              && isClickNearCrop(worldCoordinates.x, worldCoordinates.y, cropPos)) {
            harvestCrop(crop, harvestingComponent);
          }
        }
      }
    }
  }

  private boolean isPlayerNearCrop(PositionComponent playerPos, PositionComponent cropPos) {
    float distance = playerPos.position.dst(cropPos.position);
    return distance < 2f;
  }

  private boolean isClickNearCrop(float mouseX, float mouseY, PositionComponent cropPos) {
    float clickThreshold = 0.7f;
    float distance = new Vector2(mouseX, mouseY).dst(cropPos.position.x, cropPos.position.y);
    return distance < clickThreshold;
  }

  private void harvestCrop(Entity crop, HarvestingComponent harvestingComponent) {
    harvestingComponent.isHarvested = true;

    getEngine().removeEntity(crop);
    // should be added to inventory when it exists

    System.out.println("Crop harvested");
  }
}

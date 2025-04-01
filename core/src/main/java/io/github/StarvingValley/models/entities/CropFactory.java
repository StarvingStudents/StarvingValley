package io.github.StarvingValley.models.entities;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import io.github.StarvingValley.models.components.CropTypeComponent;
import io.github.StarvingValley.models.components.GrowthStageComponent;
import io.github.StarvingValley.models.components.HarvestingComponent;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.components.SizeComponent;
import io.github.StarvingValley.models.components.SpriteComponent;
import io.github.StarvingValley.models.components.TimeToGrowComponent;
import io.github.StarvingValley.models.components.WorldLayerComponent;
import io.github.StarvingValley.models.types.WorldLayer;

public class CropFactory {

  public static Entity createCrop(float x, float y, CropTypeComponent.CropType cropType) {
    Entity entity = new Entity();
    PositionComponent position = new PositionComponent(x, y);
    position.position.set(x, y, 0);
    entity.add(position);

    WorldLayerComponent layer = new WorldLayerComponent(WorldLayer.CROP);
    entity.add(layer);

    switch (cropType) {
      case TOMATO:
        addComponentsToEntity(entity, createTomatoComponents());
        break;
      case POTATO:
        addComponentsToEntity(entity, createPotatoComponents());
        break;
      default:
        throw new IllegalArgumentException("Unknown crop type: " + cropType);
    }

    return entity;
  }

  private static void addComponentsToEntity(Entity entity, Component[] components) {
    for (Component component : components) {
      entity.add(component);
    }
  }

  private static SpriteComponent createSprite(String cropType) {
    return new SpriteComponent(cropType + "1.png");
  }

  private static GrowthStageComponent createGrowthStage() {
    return new GrowthStageComponent();
  }

  private static TimeToGrowComponent createTimeToGrow(int time) {
    return new TimeToGrowComponent(time);
  }

  private static HarvestingComponent createHarvestingComponent() {
    return new HarvestingComponent(true, 2.0f);
  }

  private static CropTypeComponent createCropType(CropTypeComponent.CropType cropType) {
    return new CropTypeComponent(cropType);
  }

  private static Component[] createTomatoComponents() {
    return new Component[] {
      createSprite("tomato"),
      createGrowthStage(),
      new SizeComponent(1, 1),
      createTimeToGrow(20), // very short growth time for now, just for testing :3
      createHarvestingComponent(),
      createCropType(CropTypeComponent.CropType.TOMATO)
    };
  }

  private static Component[] createPotatoComponents() {
    return new Component[] {
      createSprite("potato"),
      createGrowthStage(),
      new SizeComponent(1, 1),
      createTimeToGrow(40),
      createHarvestingComponent(),
      createCropType(CropTypeComponent.CropType.POTATO)
    };
  }
}

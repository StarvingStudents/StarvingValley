package io.github.StarvingValley.models.entities;

import java.util.Arrays;
import java.util.List;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

import io.github.StarvingValley.models.components.ActiveWorldEntityComponent;
import io.github.StarvingValley.models.components.BuildableComponent;
import io.github.StarvingValley.models.components.ClickableComponent;
import io.github.StarvingValley.models.components.CropTypeComponent;
import io.github.StarvingValley.models.components.DropComponent;
import io.github.StarvingValley.models.components.GrowthStageComponent;
import io.github.StarvingValley.models.components.HarvestingComponent;
import io.github.StarvingValley.models.components.PrefabTypeComponent;
import io.github.StarvingValley.models.components.SizeComponent;
import io.github.StarvingValley.models.components.SpriteComponent;
import io.github.StarvingValley.models.components.SyncComponent;
import io.github.StarvingValley.models.components.TileOccupierComponent;
import io.github.StarvingValley.models.components.TimeToGrowComponent;
import io.github.StarvingValley.models.components.WorldLayerComponent;
import io.github.StarvingValley.models.types.ItemStack;
import io.github.StarvingValley.models.types.PrefabType;
import io.github.StarvingValley.models.types.WorldLayer;

public class CropFactory {
  public static Entity createCrop(PrefabType cropType) {
    Entity entity = new Entity();

    WorldLayerComponent layer = new WorldLayerComponent(WorldLayer.CROP);
    entity.add(layer);
    entity.add(new SyncComponent());
    entity.add(new TileOccupierComponent());
    entity.add(new ActiveWorldEntityComponent());
    entity.add(new ClickableComponent());

    switch (cropType) {
      case WHEAT_CROP:
        addComponentsToEntity(entity, createWheatComponents());
        break;
      case BEETROOT_CROP:
        addComponentsToEntity(entity, createBeetrootComponents());
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
    return new SpriteComponent("Sprout Lands - Sprites - Basic pack\\Sprout Lands - Sprites - Basic pack\\" + cropType + "_0.png");
  }

  private static GrowthStageComponent createGrowthStage() {
    return new GrowthStageComponent();
  }

  private static TimeToGrowComponent createTimeToGrow(int time) {
    return new TimeToGrowComponent(time);
  }

  private static HarvestingComponent createHarvestingComponent() {
    return new HarvestingComponent(true);
  }

  private static CropTypeComponent createCropType(CropTypeComponent.CropType cropType) {
    return new CropTypeComponent(cropType);
  }

  private static PrefabTypeComponent createItemType(PrefabType prefabType) {
    return new PrefabTypeComponent(prefabType);
  }

  private static DropComponent createDrop(List<ItemStack> drops) {
    return new DropComponent(drops);
  }

  private static BuildableComponent createBuildable(PrefabType builds) {
    return new BuildableComponent(builds);
  }

  private static Component[] createWheatComponents() {
    return new Component[] {
        createSprite("wheat"),
        createGrowthStage(),
        new SizeComponent(1, 1),
        createTimeToGrow(20), // very short growth time for now, just for testing :3
        createHarvestingComponent(),
        createCropType(CropTypeComponent.CropType.WHEAT),
        createItemType(PrefabType.WHEAT_CROP),
        createDrop(Arrays.asList(new ItemStack(PrefabType.WHEAT_SEEDS, 2))),
        createBuildable(PrefabType.WHEAT_CROP)
    };
  }

  private static Component[] createBeetrootComponents() {
    return new Component[] {
        createSprite("beetroot"),
        createGrowthStage(),
        new SizeComponent(1, 1),
        createTimeToGrow(40),
        createHarvestingComponent(),
        createCropType(CropTypeComponent.CropType.BEETROOT),
        createItemType(PrefabType.BEETROOT_CROP),
        createDrop(Arrays.asList(new ItemStack(PrefabType.BEETROOT_SEEDS, 2))),
        createBuildable(PrefabType.BEETROOT_CROP)
    };
  }
}

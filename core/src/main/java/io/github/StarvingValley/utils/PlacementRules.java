package io.github.StarvingValley.utils;

import java.util.EnumSet;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.GridPoint2;

import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.components.SizeComponent;
import io.github.StarvingValley.models.components.TileOccupierComponent;
import io.github.StarvingValley.models.components.WorldLayerComponent;
import io.github.StarvingValley.models.types.WorldLayer;

public class PlacementRules {
  public static boolean canPlace(
      GridPoint2 targetTile, WorldLayer targetWorldLayer, ImmutableArray<Entity> blockingEntities) {

    EnumSet<WorldLayer> blockingLayers = getBlockingLayers(targetTile, blockingEntities);

    if (targetWorldLayer == WorldLayer.CROP) {
      return blockingLayers.equals(EnumSet.of(WorldLayer.SOIL));
    }

    return blockingLayers.isEmpty();
  }

  public static EnumSet<WorldLayer> getBlockingLayers(
      GridPoint2 targetTile, ImmutableArray<Entity> blockingEntities) {
    EnumSet<WorldLayer> blockingLayers = EnumSet.noneOf(WorldLayer.class);

    for (Entity blockingEntity : blockingEntities) {
      TileOccupierComponent placement = Mappers.tileOccupier.get(blockingEntity);
      WorldLayerComponent worldLayer = Mappers.worldLayer.get(blockingEntity);
      PositionComponent position = Mappers.position.get(blockingEntity);
      SizeComponent size = Mappers.size.get(blockingEntity);

      if (placement == null || worldLayer == null || position == null) {
        continue;
      }

      if (TileUtils.isOverlappingTile(position.position.x, position.position.y, size.width, size.height, targetTile)) {
        blockingLayers.add(worldLayer.layer);
      }
    }

    return blockingLayers;
  }
}

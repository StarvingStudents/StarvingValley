package io.github.StarvingValley.utils;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.GridPoint2;
import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.TileOccupierComponent;
import io.github.StarvingValley.models.components.TileOverlapComponent;
import io.github.StarvingValley.models.components.WorldLayerComponent;
import io.github.StarvingValley.models.types.WorldLayer;
import java.util.EnumSet;

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
      TileOverlapComponent tileOverlap = Mappers.tileOccupancy.get(blockingEntity);
      TileOccupierComponent placement = Mappers.tileOccupier.get(blockingEntity);
      WorldLayerComponent worldLayer = Mappers.worldLayer.get(blockingEntity);

      if (tileOverlap == null || placement == null || worldLayer == null) {
        continue;
      }

      if (tileOverlap.overlappingTiles.contains(targetTile)) {
        blockingLayers.add(worldLayer.layer);
      }
    }

    return blockingLayers;
  }
}

package io.github.StarvingValley.utils;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector3;

import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.components.SizeComponent;
import io.github.StarvingValley.models.components.TileOverlapComponent;

public class TileUtils {
  //TODO: Change overlapping tiles to a spatialgrid
  public static Set<GridPoint2> getOverlappingTiles(
      float x, float y, float width, float height, int tileSize) {
    Set<GridPoint2> result = new HashSet<>();

    int startX = (int) Math.floor(x / tileSize);
    int endX = (int) Math.floor((x + width - 0.001f) / tileSize);
    int startY = (int) Math.floor(y / tileSize);
    int endY = (int) Math.floor((y + height - 0.001f) / tileSize);

    for (int tileX = startX; tileX <= endX; tileX++) {
      for (int tileY = startY; tileY <= endY; tileY++) {
        result.add(new GridPoint2(tileX, tileY));
      }
    }

    return result;
  }

  public static void updateOverlappingTiles(Entity entity) {
    PositionComponent pos = Mappers.position.get(entity);
    SizeComponent size = Mappers.size.get(entity);
    TileOverlapComponent occ = Mappers.tileOccupancy.get(entity);

    if (pos == null || size == null || occ == null)
      return;

    occ.overlappingTiles.clear();
    occ.overlappingTiles.addAll(
        getOverlappingTiles(pos.position.x, pos.position.y, size.width, size.height, 1));
  }

  public static GridPoint2 getHoveredTile(Camera camera) {
    Vector3 world = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
    int tileX = (int) (world.x);
    int tileY = (int) (world.y);

    return new GridPoint2(tileX, tileY);
  }
}

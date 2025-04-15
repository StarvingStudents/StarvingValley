package io.github.StarvingValley.utils;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import io.github.StarvingValley.config.Config;

public class TileUtils {
  public static int getTileWidth() {
    return Gdx.graphics.getWidth() / Config.CAMERA_TILES_WIDE;
  }

  // TODO: Change overlapping tiles to a spatialgrid
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

  public static GridPoint2 getHoveredTile(Camera camera) {
    Vector3 world = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
    int tileX = (int) (world.x);
    int tileY = (int) (world.y);

    return new GridPoint2(tileX, tileY);
  }

  public static GridPoint2 worldToTile(Vector2 worldPos) {
    return new GridPoint2((int) Math.floor(worldPos.x), (int) Math.floor(worldPos.y));
  }

  public static GridPoint2 coordinatesToTile(int screenX, int screenY, OrthographicCamera camera) {
    Vector2 worldClick = ScreenUtils.getWorldPosition(camera, screenX, screenY);
    return worldToTile(worldClick);
  }

  public static boolean isOverlappingTile(
      float x, float y, float width, float height, GridPoint2 tile) {

    int startX = (int) Math.floor(x);
    int endX = (int) Math.floor((x + width - 0.001f));
    int startY = (int) Math.floor(y);
    int endY = (int) Math.floor((y + height - 0.001f));

    return tile.x >= startX && tile.x <= endX
        && tile.y >= startY && tile.y <= endY;
  }
}

package io.github.StarvingValley.views;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import io.github.StarvingValley.config.Config;
import io.github.StarvingValley.utils.EventDebugger;
import io.github.StarvingValley.utils.ScreenUtils;

public class EventDebugOverlay {
  private final BitmapFont font = new BitmapFont();
  private final EventDebugger debugger;

  private final SpriteBatch batch = new SpriteBatch();

  public EventDebugOverlay(EventDebugger debugger) {
    this.debugger = debugger;
    font.getData().setScale(5f);
  }

  public void render() {

    int tilesWide = Config.CAMERA_TILES_WIDE;
    int screenWidth = Gdx.graphics.getWidth();
    int tileWidth = screenWidth / tilesWide;

    Vector2 renderPos = ScreenUtils.getScreenPositionFromTouchPosition(screenWidth - (tileWidth * 2), 0);

    batch.begin();

    List<String> events = debugger.getRecentEvents();
    float y = renderPos.y;
    for (String eventName : events) {
      font.draw(batch, eventName, 10, y);
      y -= 60;
    }

    batch.end();
  }
}

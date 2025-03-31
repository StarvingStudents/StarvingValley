package io.github.StarvingValley.input;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

import io.github.StarvingValley.config.Config;

// Custom tap listener since both GestureDetector and InputAdapter triggers tap even on long presses
public class TapInputAdapter extends InputAdapter {
    private float touchDownTime = 0f;
    private Vector2 downPos = new Vector2();
    private boolean isTouchDown = false;
    private float nanosecondsInSecond = 1_000_000_000f;

    private final Runnable onTap;

    public TapInputAdapter(Runnable onTap) {
        this.onTap = onTap;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        isTouchDown = true;
        touchDownTime = TimeUtils.nanoTime() / nanosecondsInSecond;
        downPos.set(screenX, screenY);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (!isTouchDown)
            return false;
        isTouchDown = false;

        float touchUpTime = TimeUtils.nanoTime() / nanosecondsInSecond;
        float duration = touchUpTime - touchDownTime;

        Vector2 upPos = new Vector2(screenX, screenY);
        float distance = downPos.dst(upPos);

        if (duration <= Config.MAX_TAP_DURATION && distance <= Config.MAX_TAP_DISTANCE) {
            onTap.run();
            return false;
        }

        return false;
    }
}

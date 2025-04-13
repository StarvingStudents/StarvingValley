package io.github.StarvingValley.views;

import com.badlogic.gdx.InputMultiplexer;

import java.util.function.BooleanSupplier;

public class FilteringInputMultiplexer extends InputMultiplexer {
    private final BooleanSupplier shouldBlockInput;

    public FilteringInputMultiplexer(BooleanSupplier shouldBlockInput) {
        super();
        this.shouldBlockInput = shouldBlockInput;
    }

    @Override
    public boolean keyDown(int keycode) {
        return shouldBlockInput.getAsBoolean() || super.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        return shouldBlockInput.getAsBoolean() || super.keyUp(keycode);
    }

    @Override
    public boolean keyTyped(char character) {
        return shouldBlockInput.getAsBoolean() || super.keyTyped(character);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return shouldBlockInput.getAsBoolean() || super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return shouldBlockInput.getAsBoolean() || super.touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return shouldBlockInput.getAsBoolean() || super.touchDragged(screenX, screenY, pointer);
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return shouldBlockInput.getAsBoolean() || super.mouseMoved(screenX, screenY);
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return shouldBlockInput.getAsBoolean() || super.scrolled(amountX, amountY);
    }
}


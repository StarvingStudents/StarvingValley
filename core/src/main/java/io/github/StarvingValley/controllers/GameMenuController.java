package io.github.StarvingValley.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;

import io.github.StarvingValley.models.events.GameMenuCloseEvent;
import io.github.StarvingValley.models.events.GameMenuOpenEvent;
import io.github.StarvingValley.models.types.GameContext;
import io.github.StarvingValley.views.GameMenuView;

public class GameMenuController {
    private final GameContext context;
    private final GameMenuView view;
    private final SpriteBatch uiBatch;

    private boolean isVisible = false;

    public GameMenuController(GameContext context) {
        this.context = context;
        this.view = new GameMenuView(this, context);
        this.uiBatch = new SpriteBatch();
    }

    public void update() {
        if (isVisible) {
            uiBatch.begin();
            view.update();
            uiBatch.end();
        } else {
            view.update();
        }
        handleEvents();

    }

    public void render() {
        if (isVisible) {
            uiBatch.setProjectionMatrix(
                new Matrix4().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight())
            );
            uiBatch.begin();
            view.render(uiBatch);
            uiBatch.end();
        }
    }

    public void setVisible(boolean visible) {
        this.isVisible = visible;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void toggleVisibility() {
        setVisible(!isVisible);
    }

    public void onPlayPressed() {
        context.eventBus.publish(new GameMenuCloseEvent());
    }

    public void onExitPressed() {
        Gdx.app.exit();
    }

    public void onSettingsPressed() {
        // Settings logic TBD
    }
    public void onToggleMenuRequested() {
        toggleVisibility();
        if (isVisible) {
            context.eventBus.publish(new GameMenuOpenEvent());
        } else {
            context.eventBus.publish(new GameMenuCloseEvent());
        }
    }

    private void handleEvents() {
        if (!context.eventBus.getEvents(GameMenuOpenEvent.class).isEmpty()) {
            setVisible(true);
        }

        if (!context.eventBus.getEvents(GameMenuCloseEvent.class).isEmpty()) {
            setVisible(false);
        }
    }


}

package io.github.StarvingValley.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import io.github.StarvingValley.controllers.GameMenuController;
import io.github.StarvingValley.models.types.GameContext;

public class GameMenuView {
    private final GameMenuController controller;
    private final Texture backgroundTexture;
    private final Sprite background;

    private Rectangle playButtonBounds;
    private Rectangle settingsButtonBounds;
    private Rectangle exitButtonBounds;

    public GameMenuView(GameMenuController controller, GameContext context) {
        this.controller = controller;
        this.backgroundTexture = context.assetManager.get("GameMenu.png", Texture.class);
        this.background = new Sprite(backgroundTexture);

        float width = Gdx.graphics.getWidth() * 0.6f;
        float scale = width / backgroundTexture.getWidth();
        float height = backgroundTexture.getHeight() * scale;

        float x = (Gdx.graphics.getWidth() - width) / 2f;
        float y = (Gdx.graphics.getHeight() - height) / 2f;
        background.setBounds(x, y, width, height);

        playButtonBounds = new Rectangle(x + 55 * scale, y + 35 * scale, 50 * scale, 16 * scale
        );

        settingsButtonBounds = new Rectangle(x + 15 * scale, y + 10 * scale, 50 * scale, 16 * scale
        );

        exitButtonBounds = new Rectangle(x + 90 * scale, y + 10 * scale, 50 * scale, 16 * scale
        );
    }


    public void update() {
        if (Gdx.input.justTouched()) {
            Vector2 touch = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());

            if (playButtonBounds.contains(touch)) {
                controller.onPlayPressed();
            } else if (settingsButtonBounds.contains(touch)) {
                controller.onSettingsPressed();
            } else if (exitButtonBounds.contains(touch)) {
                controller.onExitPressed();
            }
        }
    }

    public void render(SpriteBatch batch) {
        background.draw(batch);
    }
}

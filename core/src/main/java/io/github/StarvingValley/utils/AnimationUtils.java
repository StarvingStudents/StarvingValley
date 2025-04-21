package io.github.StarvingValley.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimationUtils {

    public static Animation<TextureRegion> loadFromSheet(
            Assets assets,
                String texturePath,
        int frameCols,
        int frameRows,
        float frameDuration,
        PlayMode playMode) {

        Texture sheet = assets.getTexture(texturePath);
        if (sheet == null)
            throw new RuntimeException("Texture not loaded: " + texturePath);

        TextureRegion[][] tmp = TextureRegion.split(sheet,
            sheet.getWidth() / frameCols,
            sheet.getHeight() / frameRows);

        TextureRegion[] frames = new TextureRegion[frameCols * frameRows];
        int index = 0;
        for (int i = 0; i < frameRows; i++) {
            for (int j = 0; j < frameCols; j++) {
                frames[index++] = tmp[i][j];
            }
        }

        Animation<TextureRegion> animation = new Animation<>(frameDuration, frames);
        animation.setPlayMode(playMode);
        return animation;
    }

    public static void loadTexturesForAnimation(AssetManager assetManager) {
        assetManager.load("idle_down.png", Texture.class);
        assetManager.load("idle_up.png", Texture.class);
        assetManager.load("idle_left.png", Texture.class);
        assetManager.load("idle_right.png", Texture.class);
        assetManager.load("walking_down.png", Texture.class);
        assetManager.load("walking_up.png", Texture.class);
        assetManager.load("walking_left.png", Texture.class);
        assetManager.load("walking_right.png", Texture.class);
        assetManager.load("action_soil_down.png", Texture.class);
        assetManager.load("action_soil_up.png", Texture.class);
        assetManager.load("action_soil_left.png", Texture.class);
        assetManager.load("action_soil_right.png", Texture.class);
        assetManager.load("action_axe_down.png", Texture.class);
        assetManager.load("action_axe_up.png", Texture.class);
        assetManager.load("action_axe_left.png", Texture.class);
        assetManager.load("action_axe_right.png", Texture.class);

        assetManager.finishLoading();
    }
}

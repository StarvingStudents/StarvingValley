package io.github.StarvingValley.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;

public class AnimationUtils {

    public static Animation<TextureRegion> loadFromSheet(
        AssetManager manager,
        String texturePath,
        int frameCols,
        int frameRows,
        float frameDuration,
        PlayMode playMode) {

        if (!manager.isLoaded(texturePath)) {
            throw new RuntimeException("Texture not loaded: " + texturePath);
        }

        Texture sheet = manager.get(texturePath, Texture.class);
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
}


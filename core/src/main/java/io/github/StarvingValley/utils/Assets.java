package io.github.StarvingValley.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class Assets {
    public AssetManager assetManager;

    public Assets(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public Texture getTexture(String texturePath) {
        if (assetManager.isLoaded(texturePath)) {
            return assetManager.get(texturePath, Texture.class);
        } else {
            assetManager.load(texturePath, Texture.class);
            return null;
        }
    }
}

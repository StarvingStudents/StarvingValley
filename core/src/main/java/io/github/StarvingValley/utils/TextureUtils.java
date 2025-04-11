package io.github.StarvingValley.utils;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.entities.EntityFactoryRegistry;
import io.github.StarvingValley.models.types.PrefabType;

public class TextureUtils {
    public static Texture createWhitePixel() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    public static Sprite getSpriteForPrefabType(PrefabType type, Assets assets) {
        Entity prefab = EntityFactoryRegistry.create(type);
        String path = Mappers.sprite.get(prefab).getTexturePath();
        Texture texture = assets.getTexture(path);
        return texture != null ? new Sprite(texture) : null;
    }
}

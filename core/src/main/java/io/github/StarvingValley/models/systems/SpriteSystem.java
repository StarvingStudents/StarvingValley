package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.SpriteComponent;
import io.github.StarvingValley.utils.SyncUtils;

public class SpriteSystem extends IteratingSystem {
    private AssetManager assetManager;

    public SpriteSystem(AssetManager assetManager) {
        super(Family
                .all(SpriteComponent.class)
                .get());

        this.assetManager = assetManager;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        SpriteComponent spriteComponent = Mappers.sprite.get(entity);

        if (!spriteComponent.textureChanged)
            return;

        String texturePath = spriteComponent.getTexturePath();

        if (assetManager.isLoaded(texturePath)) {
            spriteComponent.sprite = new Sprite(assetManager.get(texturePath, Texture.class));
            spriteComponent.textureChanged = false;


        } else {
            assetManager.load(texturePath, Texture.class);
        }
    }
}

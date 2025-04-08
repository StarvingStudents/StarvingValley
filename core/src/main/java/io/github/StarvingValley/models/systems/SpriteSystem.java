package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.SpriteComponent;
import io.github.StarvingValley.models.types.GameContext;

public class SpriteSystem extends IteratingSystem {
    private GameContext context;

    public SpriteSystem(GameContext context) {
        super(Family
                .all(SpriteComponent.class)
                .get());
        this.context = context;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        SpriteComponent spriteComponent = Mappers.sprite.get(entity);

        if (!spriteComponent.textureChanged)
            return;

        String texturePath = spriteComponent.getTexturePath();

        if (context.assetManager.isLoaded(texturePath)) {
            spriteComponent.sprite = new Sprite(context.assetManager.get(texturePath, Texture.class));
            spriteComponent.textureChanged = false;
        } else {
            context.assetManager.load(texturePath, Texture.class);
        }
    }
}

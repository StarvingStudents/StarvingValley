package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import io.github.StarvingValley.config.Config;
import io.github.StarvingValley.models.components.ButtonComponent;
import io.github.StarvingValley.models.components.ClickableComponent;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.components.SizeComponent;
import io.github.StarvingValley.models.components.SpriteComponent;
import io.github.StarvingValley.utils.ScreenUtils;

public class HUDRenderSystem extends EntitySystem {

    @Override
    public void update(float deltaTime) {

        ImmutableArray<Entity> renderEntities = getEngine()
                .getEntitiesFor(Family.all(ClickableComponent.class, SizeComponent.class,
                        PositionComponent.class, SpriteComponent.class, ButtonComponent.class).get());

        int tilesWide = Config.CAMERA_TILES_WIDE;
        int screenWidth = Gdx.graphics.getWidth();
        int tileWidth = screenWidth / tilesWide;

        SpriteBatch newSpritebatch = new SpriteBatch();

        newSpritebatch.begin();

        for (Entity entity : renderEntities) {
            // System.out.println("Rendering entity HUDRenderSystem");

            Sprite sprite = new Sprite(new Texture(entity.getComponent(SpriteComponent.class).getTexturePath()));
            sprite.setSize(tileWidth * entity.getComponent(SizeComponent.class).width, tileWidth * entity
                    .getComponent(SizeComponent.class).width);

            // Vector2 renderPos = ScreenUtils.getScreenPositionFromTouchPosition(
            // screenWidth + (tileWidth * (int)
            // entity.getComponent(PositionComponent.class).position.x),
            // tileWidth * (int) entity.getComponent(PositionComponent.class).position.y);

            Vector2 renderPos = new Vector2(entity.getComponent(PositionComponent.class).position.x,
                    entity.getComponent(PositionComponent.class).position.y);

            sprite.setPosition(renderPos.x, renderPos.y);
            if (sprite.getTexture() != null) {
                sprite.draw(newSpritebatch);
            } else {
                System.out.println("Warning: Sprite texture is null, skipping drawing.");
            }
            sprite.draw(newSpritebatch);
        }

        newSpritebatch.end();
    }
}
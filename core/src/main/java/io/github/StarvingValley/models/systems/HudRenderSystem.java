package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.github.StarvingValley.config.Config;
import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.HudComponent;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.components.SizeComponent;
import io.github.StarvingValley.models.components.SpriteComponent;
import io.github.StarvingValley.utils.MapUtils;

public class HudRenderSystem extends EntitySystem {
    private SpriteBatch batch;

    public HudRenderSystem() {
        batch = new SpriteBatch();
    }

    @Override
    public void update(float deltaTime) {
        ImmutableArray<Entity> renderEntities = getEngine()
                .getEntitiesFor(Family
                        .all(PositionComponent.class, SpriteComponent.class, SizeComponent.class, HudComponent.class)
                        .get());

        int tilesWide = Config.CAMERA_TILES_WIDE;
        float tilesHigh = MapUtils.calculateVerticalTileCount(tilesWide);
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();

        float tileWidth = (float) screenWidth / tilesWide;
        float tileHeight = (float) screenHeight / tilesHigh;

        batch.begin();

        for (Entity entity : renderEntities) {
            if (Mappers.hidden.has(entity))
                continue;

            PositionComponent pos = Mappers.position.get(entity);
            SpriteComponent sprite = Mappers.sprite.get(entity);
            SizeComponent size = Mappers.size.get(entity);

            if (sprite.sprite.getTexture() == null)
                continue;

            float pixelX = pos.position.x * tileWidth;
            float pixelY = pos.position.y * tileHeight;

            float pixelWidth = size.width * tileWidth;
            float pixelHeight = size.height * tileHeight;

            sprite.sprite.setPosition(pixelX, pixelY);
            sprite.sprite.setSize(pixelWidth, pixelHeight);

            sprite.sprite.draw(batch);
        }

        batch.end();
    }

}

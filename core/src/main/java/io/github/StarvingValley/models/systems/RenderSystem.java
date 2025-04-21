package io.github.StarvingValley.models.systems;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;

import io.github.StarvingValley.config.Config;
import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.HiddenComponent;
import io.github.StarvingValley.models.components.HudComponent;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.components.SizeComponent;
import io.github.StarvingValley.models.components.SpriteComponent;
import io.github.StarvingValley.models.types.GameContext;

public class RenderSystem extends EntitySystem {
  private final Comparator<Entity> renderOrderComparator = Comparator
      .comparingInt((Entity e) -> {
        if (Mappers.worldLayer.has(e)) {
          return Mappers.worldLayer.get(e).layer.getRenderPriority();
        } else {
          return Integer.MAX_VALUE;
        }
      })
      .thenComparing(e -> {
        PositionComponent pos = Mappers.position.get(e);
        return pos != null ? -pos.position.y : 0f;
      });

  private GameContext context;

  public RenderSystem(GameContext context) {
    this.context = context;
  }

  @Override
  public void update(float deltaTime) {
    ImmutableArray<Entity> renderEntities =
        getEngine()
            .getEntitiesFor(
                Family.all(PositionComponent.class, SpriteComponent.class, SizeComponent.class)
                    .exclude(HudComponent.class, HiddenComponent.class)
                    .get());

    List<Entity> sorted = new ArrayList<>(renderEntities.size());
    for (int i = 0; i < renderEntities.size(); i++) {
      sorted.add(renderEntities.get(i));
    }

    sorted.sort(renderOrderComparator);

    context.spriteBatch.begin();

    for (Entity entity : sorted) {
      PositionComponent pos = Mappers.position.get(entity);
      SpriteComponent sprite = Mappers.sprite.get(entity);
      SizeComponent size = Mappers.size.get(entity);

      if (sprite.sprite.getTexture() == null) {
        continue;
      }
      if (size.useRegionSize) {
        float widthInTiles = sprite.sprite.getRegionWidth() / Config.PIXELS_PER_TILE;
        float heightInTiles = sprite.sprite.getRegionHeight() / Config.PIXELS_PER_TILE;
        sprite.sprite.setSize(widthInTiles, heightInTiles);
      } else {
        sprite.sprite.setSize(size.width, size.height);
      }

      float renderX = pos.position.x - sprite.sprite.getWidth() / 2f + size.width / 2f;
      float renderY = pos.position.y - sprite.sprite.getHeight() / 2f + size.height / 2f;
      sprite.sprite.setPosition(renderX, renderY);


      sprite.sprite.draw(context.spriteBatch);
    }

    context.spriteBatch.end();
  }
}

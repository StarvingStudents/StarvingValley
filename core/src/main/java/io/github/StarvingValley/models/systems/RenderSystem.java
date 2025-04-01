package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.components.SizeComponent;
import io.github.StarvingValley.models.components.SpriteComponent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RenderSystem extends EntitySystem {
  private final SpriteBatch batch;

  private final Comparator<Entity> renderOrderComparator =
      // TODO: take both y and z into consideration
      Comparator.comparing(e -> -Mappers.position.get(e).position.y); // changed from z to y

  public RenderSystem(SpriteBatch batch) {
    this.batch = batch;
  }

  @Override
  public void update(float deltaTime) {
    ImmutableArray<Entity> renderEntities =
        getEngine()
            .getEntitiesFor(
                Family.all(PositionComponent.class, SpriteComponent.class, SizeComponent.class)
                    .get());

    List<Entity> sorted = new ArrayList<>(renderEntities.size());
    for (int i = 0; i < renderEntities.size(); i++) {
      sorted.add(renderEntities.get(i));
    }

    sorted.sort(renderOrderComparator);

    batch.begin();

    for (Entity entity : sorted) {
      if (Mappers.hidden.has(entity)) continue;

      PositionComponent pos = Mappers.position.get(entity);
      SpriteComponent sprite = Mappers.sprite.get(entity);
      SizeComponent size = Mappers.size.get(entity);

      if (sprite.sprite.getTexture() == null) {
        continue;
      }

      sprite.sprite.setPosition(pos.position.x, pos.position.y);
      sprite.sprite.setSize(size.width, size.height);
      sprite.sprite.draw(batch);
    }

    batch.end();
  }
}

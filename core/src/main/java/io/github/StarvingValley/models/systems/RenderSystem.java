package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.components.SizeComponent;
import io.github.StarvingValley.models.components.SpriteComponent;

public class RenderSystem extends IteratingSystem {
  private final SpriteBatch batch;

  public RenderSystem(SpriteBatch batch) {
    super(Family.all(PositionComponent.class, SpriteComponent.class, SizeComponent.class).get());
    this.batch = batch;
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    PositionComponent position = Mappers.position.get(entity);
    SpriteComponent sprite = Mappers.sprite.get(entity);
    SizeComponent size = Mappers.size.get(entity);

    sprite.sprite.setPosition(position.position.x, position.position.y);
    sprite.sprite.setSize(size.width, size.height);

    batch.begin();

    sprite.sprite.draw(batch);

    batch.end();
  }
}

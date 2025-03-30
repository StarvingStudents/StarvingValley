package io.github.StarvingValley.models.entities;

import com.badlogic.ashley.core.Entity;
import io.github.StarvingValley.models.components.CropTypeComponent;
import io.github.StarvingValley.models.components.GrowthStageComponent;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.components.SizeComponent;
import io.github.StarvingValley.models.components.SpriteComponent;
import io.github.StarvingValley.models.components.TimeToGrowComponent;

public class TomatoFactory {

  public static Entity createTomato(float x, float y) {
    Entity entity = new Entity();
    PositionComponent position = new PositionComponent(x, y);
    position.position.set(x, y);

    entity.add(position);
    SpriteComponent tomato = new SpriteComponent("tomato1.png");
    entity.add(new GrowthStageComponent());
    entity.add(new SizeComponent(1, 1));
    entity.add(new TimeToGrowComponent(6)); // 6 seconds
    entity.add(new CropTypeComponent(CropTypeComponent.CropType.TOMATO));
    entity.add(tomato);
    return entity;
  }
}

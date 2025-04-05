package io.github.StarvingValley.utils;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.ActiveWorldEntity;
import io.github.StarvingValley.models.components.BuildPreviewComponent;
import io.github.StarvingValley.models.components.ClickableComponent;
import io.github.StarvingValley.models.components.PulseAlphaComponent;
import io.github.StarvingValley.models.components.SyncComponent;

public class BuildUtils {
  public static void toggleBuildPreview(Entity entity, Engine engine) {
    assertBuildPreviewCompatible(entity);

    ImmutableArray<Entity> previews = engine.getEntitiesFor(Family.all(BuildPreviewComponent.class).get());

    if (previews.size() > 0) {
      for (Entity preview : previews) {
        engine.removeEntity(preview);
      }
      return;
    }

    entity.add(new BuildPreviewComponent());
    entity.add(new ClickableComponent());
    entity.add(new PulseAlphaComponent());
    entity.remove(ActiveWorldEntity.class);
    entity.remove(SyncComponent.class);
  }

  public static boolean isBuildable(Entity entity) {
    return Mappers.buildable.has(entity);
  }

  private static void assertBuildPreviewCompatible(Entity entity) {
    if (!Mappers.position.has(entity)
        || !Mappers.sprite.has(entity)
        || !Mappers.worldLayer.has(entity)) {
      throw new IllegalArgumentException(
          "Build preview must have Position, Sprite, and WorldLayer components");
    }
  }
}

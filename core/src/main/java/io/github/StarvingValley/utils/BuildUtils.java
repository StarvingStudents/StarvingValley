package io.github.StarvingValley.utils;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.BuildPreviewComponent;
import io.github.StarvingValley.models.components.BuildableComponent;
import io.github.StarvingValley.models.entities.BuildPreviewFactory;
import io.github.StarvingValley.models.entities.EntityFactoryRegistry;
import io.github.StarvingValley.models.types.PrefabType;

public class BuildUtils {
  public static void enableBuildPreview(PrefabType prefabType, Engine engine) {
    disableBuildPreview(engine);

    Entity preview = BuildPreviewFactory.create(prefabType);

    engine.addEntity(preview);
  }

  public static void disableBuildPreview(Engine engine) {
    ImmutableArray<Entity> previews = engine.getEntitiesFor(Family.all(BuildPreviewComponent.class).get());

    if (previews.size() > 0) {
      for (Entity preview : previews) {
        engine.removeEntity(preview);
      }
    }
  }

  public static PrefabType getBuildsTypeFromType(PrefabType prefabType) {
    Entity entity = EntityFactoryRegistry.create(prefabType);

    BuildableComponent buildable = Mappers.buildable.get(entity);
    if (buildable == null)
      return null;

    return buildable.builds;
  }
}

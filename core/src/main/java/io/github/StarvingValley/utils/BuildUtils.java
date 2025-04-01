package io.github.StarvingValley.utils;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.Interfaces.IBuildableEntityFactory;
import io.github.StarvingValley.models.components.BuildPreviewComponent;
import io.github.StarvingValley.models.entities.BuildPreviewFactory;

public class BuildUtils {
    public static void toggleBuildPreview(String texturePath, Engine engine, IBuildableEntityFactory entityFactory) {
        ImmutableArray<Entity> previews = engine.getEntitiesFor(
                Family.all(BuildPreviewComponent.class).get());

        if (previews.size() > 0) {
            for (Entity preview : previews) {
                engine.removeEntity(preview);
            }
        } else {
            Entity preview = BuildPreviewFactory.createBuildPreview(texturePath, 0, 0, 1, 1, entityFactory);
            engine.addEntity(preview);
        }
    }

    public static boolean isBuildable(Entity entity) {
        return Mappers.buildable.has(entity);
    }
}

package io.github.StarvingValley.models.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;

import io.github.StarvingValley.models.Interfaces.IBuildableEntityFactory;
import io.github.StarvingValley.models.components.BuildPreviewComponent;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.components.PulseAlphaComponent;
import io.github.StarvingValley.models.components.SizeComponent;
import io.github.StarvingValley.models.components.SpriteComponent;

//TODO: Maybe switch from factory to builder-director pattern?
public class BuildPreviewFactory {
    public static Entity createBuildPreview(Texture texture, int tileX, int tileY, float width, float height,
            IBuildableEntityFactory entityFactory) {
        Entity entity = new Entity();

        entity
            .add(new SpriteComponent(texture))
            .add(new PositionComponent(tileX, tileY))
            .add(new SizeComponent(width, height))
            .add(new PulseAlphaComponent())
            .add(new BuildPreviewComponent(entityFactory));

        return entity;
    }
}

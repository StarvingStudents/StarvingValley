package io.github.StarvingValley.models.entities;

import com.badlogic.ashley.core.Entity;

import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.BuildPreviewComponent;
import io.github.StarvingValley.models.components.BuildableComponent;
import io.github.StarvingValley.models.components.ClickableComponent;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.components.PulseAlphaComponent;
import io.github.StarvingValley.models.components.SizeComponent;
import io.github.StarvingValley.models.components.SpriteComponent;
import io.github.StarvingValley.models.components.WorldLayerComponent;
import io.github.StarvingValley.models.types.PrefabType;
import io.github.StarvingValley.models.types.WorldLayer;

public class BuildPreviewFactory {
    public static Entity create(PrefabType prefabType, PrefabType madeFromPrefabType) {
        Entity prototype = EntityFactoryRegistry.create(prefabType);
        validatePreview(prototype);

        String texturePath = Mappers.sprite.get(prototype).getTexturePath();
        WorldLayer layer = Mappers.worldLayer.get(prototype).layer;
        PrefabType buildsType = Mappers.buildable.get(prototype).builds;
        SizeComponent size = Mappers.size.get(prototype);

        Entity preview = new Entity();
        preview.add(new BuildPreviewComponent(madeFromPrefabType));
        preview.add(new ClickableComponent());
        preview.add(new PulseAlphaComponent());
        preview.add(new SpriteComponent(texturePath));
        preview.add(new WorldLayerComponent(layer));
        preview.add(new BuildableComponent(buildsType));
        preview.add(new PositionComponent(0, 0));
        preview.add(new SizeComponent(size.width, size.height));

        return preview;
    }

    private static void validatePreview(Entity entity) {
        if (!Mappers.sprite.has(entity)
                || !Mappers.worldLayer.has(entity) || !Mappers.size.has(entity) || !Mappers.buildable.has(entity)) {
            throw new IllegalArgumentException(
                    "Build preview must have Buildable, Sprite, WorldLayer and Size components");
        }
    }
}

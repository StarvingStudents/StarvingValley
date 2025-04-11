package io.github.StarvingValley.models.entities;

import java.util.EnumMap;
import java.util.Map;

import com.badlogic.ashley.core.Entity;

import io.github.StarvingValley.models.components.BuildableComponent;
import io.github.StarvingValley.models.components.SpriteComponent;
import io.github.StarvingValley.models.types.PrefabType;

public class SeedFactory {
    private static final Map<PrefabType, String> seedTypeToTexture;

    static {
        seedTypeToTexture = new EnumMap<>(PrefabType.class);
        seedTypeToTexture.put(PrefabType.BEETROOT_SEEDS,
                "Sprout Lands - Sprites - Basic pack\\Sprout Lands - Sprites - Basic pack\\beetroot_seeds.png");
        seedTypeToTexture.put(PrefabType.WHEAT_SEEDS,
                "Sprout Lands - Sprites - Basic pack\\Sprout Lands - Sprites - Basic pack\\wheat_seeds.png");
    }

    public static Entity create(PrefabType type) {
        Entity seeds = new Entity();

        String texturePath = seedTypeToTexture.get(type);

        if (texturePath == null) {
            throw new IllegalArgumentException("Unknown seed type: " + type);
        }

        seeds.add(new SpriteComponent(texturePath));
        seeds.add(new BuildableComponent(type));

        return seeds;
    }
}

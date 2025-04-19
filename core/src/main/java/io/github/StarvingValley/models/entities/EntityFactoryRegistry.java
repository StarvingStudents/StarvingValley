package io.github.StarvingValley.models.entities;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.badlogic.ashley.core.Entity;

import io.github.StarvingValley.models.types.PrefabType;

public class EntityFactoryRegistry {
    private static final Map<PrefabType, Supplier<Entity>> factories = new HashMap<>();

    static {
        factories.put(PrefabType.SOIL, SoilFactory::createSoil);
        factories.put(PrefabType.WHEAT_CROP, () -> CropFactory.createCrop(PrefabType.WHEAT_CROP));
        factories.put(PrefabType.BEETROOT_CROP, () -> CropFactory.createCrop(PrefabType.BEETROOT_CROP));
        factories.put(PrefabType.BEETROOT_SEEDS,
                () -> SeedFactory.create(PrefabType.BEETROOT_SEEDS, PrefabType.BEETROOT_CROP));
        factories.put(PrefabType.WHEAT_SEEDS, () -> SeedFactory.create(PrefabType.WHEAT_SEEDS, PrefabType.WHEAT_CROP));
        factories.put(PrefabType.WALL, () -> WallFactory.createWall());
        factories.put(PrefabType.BEETROOT, () -> FoodFactory.create(PrefabType.BEETROOT));
        factories.put(PrefabType.WHEAT, () -> FoodFactory.create(PrefabType.WHEAT));
    }

    public static Entity create(PrefabType type) {
        Supplier<Entity> supplier = factories.get(type);
        if (supplier == null)
            throw new IllegalArgumentException("No factory registered for: " + type);
        return supplier.get();
    }
}

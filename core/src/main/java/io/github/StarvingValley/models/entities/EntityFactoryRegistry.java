package io.github.StarvingValley.models.entities;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.badlogic.ashley.core.Entity;

import io.github.StarvingValley.models.components.CropTypeComponent.CropType;
import io.github.StarvingValley.models.types.PrefabType;

public class EntityFactoryRegistry {
    private static final Map<PrefabType, Supplier<Entity>> factories = new HashMap<>();

    static {
        factories.put(PrefabType.SOIL, SoilFactory::createSoil);
        factories.put(PrefabType.TOMATO_CROP, () -> CropFactory.createCrop(CropType.TOMATO));
        factories.put(PrefabType.POTATO_CROP, () -> CropFactory.createCrop(CropType.POTATO));
    }

    public static Entity create(PrefabType type) {
        Supplier<Entity> supplier = factories.get(type);
        if (supplier == null)
            throw new IllegalArgumentException("No factory registered for: " + type);
        return supplier.get();
    }
}

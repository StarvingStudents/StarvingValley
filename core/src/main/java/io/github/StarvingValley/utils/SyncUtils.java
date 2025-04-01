package io.github.StarvingValley.utils;

import java.util.Objects;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector3;

import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.UnsyncedComponent;

public class SyncUtils {
    public static boolean markUnsyncedIfChanged(Entity entity, float oldValue,
            float newValue) {
        return markUnsyncedIfChanged(entity, oldValue != newValue);
    }

    public static boolean markUnsyncedIfChanged(Entity entity, int oldValue, int newValue) {
        return markUnsyncedIfChanged(entity, oldValue != newValue);
    }

    public static boolean markUnsyncedIfChanged(Entity entity, String oldValue,
            String newValue) {
        return markUnsyncedIfChanged(entity, !Objects.equals(oldValue, newValue));
    }

    public static boolean markUnsyncedIfChanged(Entity entity, Vector3 oldValue, Vector3 newValue) {
        boolean changed = !oldValue.epsilonEquals(newValue, 0.0001f);
        return markUnsyncedIfChanged(entity, changed);
    }

    public static boolean markUnsyncedIfChanged(Entity entity, boolean isChanged) {
        if (isChanged) {
            markUnsynced(entity);
            return true;
        }
        return false;
    }

    public static void markUnsynced(Entity entity) {
        entity.add(new UnsyncedComponent());
    }

    public static void markSynced(Entity entity) {
        if (Mappers.sync.has(entity)) {
            entity.remove(UnsyncedComponent.class);
        }

    }
}

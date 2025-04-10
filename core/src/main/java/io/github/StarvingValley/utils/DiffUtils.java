package io.github.StarvingValley.utils;

import java.util.Objects;

import com.badlogic.gdx.math.Vector3;

public class DiffUtils {
    public static boolean hasChanged(float oldValue, float newValue) {
        return oldValue != newValue;
    }

    public static boolean hasChanged(int oldValue, int newValue) {
        return oldValue != newValue;
    }

    public static boolean hasChanged(String oldValue, String newValue) {
        return !Objects.equals(oldValue, newValue);
    }

    public static boolean hasChanged(Vector3 oldValue, Vector3 newValue) {
        return !oldValue.epsilonEquals(newValue, 0.0001f);
    }
}

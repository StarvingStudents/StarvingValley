package io.github.StarvingValley.models.components;

import com.badlogic.ashley.core.Component;

public class EatingComponent implements Component {
    public float foodPoints;

    public EatingComponent(float foodPoints) {
        this.foodPoints = foodPoints;
    }
}
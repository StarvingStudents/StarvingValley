package io.github.StarvingValley.models.components;

import com.badlogic.ashley.core.Component;

// TODO: If we don't want eating to affect multiple frames, this can be replaced with an EatEvent
public class EatingComponent implements Component {
    public float foodPoints;

    public EatingComponent(float foodPoints) {
        this.foodPoints = foodPoints;
    }
}
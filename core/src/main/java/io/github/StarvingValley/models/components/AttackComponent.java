package io.github.StarvingValley.models.components;

import com.badlogic.ashley.core.Component;

public class AttackComponent implements Component {
    public float timeRemaining;

    public AttackComponent(float duration) {
        this.timeRemaining = duration;
    }
}

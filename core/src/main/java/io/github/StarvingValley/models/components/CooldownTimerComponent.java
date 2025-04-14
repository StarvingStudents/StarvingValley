package io.github.StarvingValley.models.components;

import com.badlogic.ashley.core.Component;

public class CooldownTimerComponent implements Component {
    public float cooldownTimer = 0f;
    public final float cooldownDuration;

    public CooldownTimerComponent(float cooldownDuration) {
        this.cooldownDuration = cooldownDuration;
    }

    public boolean isReady() {
        return cooldownTimer >= cooldownDuration;
    }

    public void reset() {
        cooldownTimer = 0f;
    }
} 
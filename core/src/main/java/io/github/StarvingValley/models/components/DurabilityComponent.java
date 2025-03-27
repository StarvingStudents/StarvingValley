package io.github.StarvingValley.models.components;

import com.badlogic.ashley.core.Component;

public class DurabilityComponent implements Component {
    private int MAX_HEALTH = 100;
    private int health = MAX_HEALTH;

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = Math.max(0, Math.min(MAX_HEALTH, health));
    }

    public float getHealthPercentage() {
        float healthPercentage = (float) health / MAX_HEALTH;
        return Math.max(0, Math.min(1, healthPercentage));
    }
}

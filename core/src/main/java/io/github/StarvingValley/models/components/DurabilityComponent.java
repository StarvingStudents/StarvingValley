package io.github.StarvingValley.models.components;

import com.badlogic.ashley.core.Component;

public class DurabilityComponent implements Component {
    public float maxHealth = 100;
    public float health = maxHealth;

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = Math.max(0, Math.min(maxHealth, health));
    }

    public float getHealthPercentage() {
        float healthPercentage = (float) health / maxHealth;
        return Math.max(0, Math.min(1, healthPercentage));
    }
}

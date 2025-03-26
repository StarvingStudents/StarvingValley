package io.github.StarvingValley.models.components;

import com.badlogic.ashley.core.Component;

public class DurabilityComponent implements Component {
    final public int MAX_HEALTH = 100;
    public int health = MAX_HEALTH;
    
    public float getHealthPercentage() {
        float healthPercentage = (float) health / MAX_HEALTH;
        return Math.max(0, Math.min(1, healthPercentage));
    }
}

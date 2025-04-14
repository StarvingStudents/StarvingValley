package io.github.StarvingValley.models.components;

import com.badlogic.ashley.core.Component;

public class DamageComponent implements Component {
    public final float damageAmount;
    public final float attackRange;
    public final float attackSpeed; // Attacks per second
    public float cooldownTimer = 0f;

    public DamageComponent(float damageAmount, float attackRange, float attackSpeed) {
        this.damageAmount = damageAmount;
        this.attackRange = attackRange;
        this.attackSpeed = attackSpeed;
    }

    public boolean isReady() {
        return cooldownTimer >= 1f / attackSpeed;
    }

    public void reset() {
        cooldownTimer = 0f;
    }
} 
package io.github.StarvingValley.models.components;

import com.badlogic.ashley.core.Component;

public class DamageComponent implements Component {
    public float damageAmount;
    public float attackRange;
    public float attackSpeed; // Attacks per second
    public float cooldownTimer = 0f;

    public DamageComponent(float damageAmount, float attackRange, float attackSpeed) {
        this.damageAmount = damageAmount;
        this.attackRange = attackRange;
        this.attackSpeed = attackSpeed;
    }
} 
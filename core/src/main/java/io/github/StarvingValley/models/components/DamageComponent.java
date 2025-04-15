package io.github.StarvingValley.models.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.TimeUtils;

public class DamageComponent implements Component {
    public final float damageAmount;
    public final float attackRange;
    public final float attackSpeed;

    private long lastAttackTime = 0;

    public DamageComponent(float damageAmount, float attackRange, float attackSpeed) {
        this.damageAmount = damageAmount;
        this.attackRange = attackRange;
        this.attackSpeed = attackSpeed;
    }

    public boolean isReady() {
        if (lastAttackTime == 0)
            return true;
        long elapsed = TimeUtils.timeSinceMillis(lastAttackTime);
        return elapsed >= (1000f / attackSpeed);
    }

    public void resetCooldown() {
        lastAttackTime = TimeUtils.millis();
    }
}

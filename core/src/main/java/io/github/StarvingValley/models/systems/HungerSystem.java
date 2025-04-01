package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import io.github.StarvingValley.models.components.HungerComponent;
import io.github.StarvingValley.utils.SyncUtils;

public class HungerSystem extends IteratingSystem {

    public HungerSystem() {
        super(Family.all(HungerComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        HungerComponent hunger = entity.getComponent(HungerComponent.class);

        float oldHunger = hunger.hungerPoints;

        hunger.hungerPoints -= hunger.decayRate * deltaTime;
        if (hunger.hungerPoints < 0) {
            hunger.hungerPoints = 0;
        }

        // Don't need to sync on every decimal change
        SyncUtils.markUnsyncedIfChanged(entity, (int) hunger.hungerPoints, (int) oldHunger);
    }
}

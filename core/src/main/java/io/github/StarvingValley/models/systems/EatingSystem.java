package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import io.github.StarvingValley.models.components.EatingComponent;
import io.github.StarvingValley.models.components.HungerComponent;
import io.github.StarvingValley.utils.SyncUtils;

public class EatingSystem extends IteratingSystem {
    public EatingSystem() {
        super(Family.all(HungerComponent.class, EatingComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        HungerComponent hunger = entity.getComponent(HungerComponent.class);
        EatingComponent eating = entity.getComponent(EatingComponent.class);

        float oldHunger = hunger.hungerPoints;

        hunger.hungerPoints = Math.min(hunger.maxHungerPoints, hunger.hungerPoints + eating.foodPoints); 

        SyncUtils.markUnsyncedIfChanged(entity, hunger.hungerPoints, oldHunger);
    }
}

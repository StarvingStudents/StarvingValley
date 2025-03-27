package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import io.github.StarvingValley.models.components.HungerComponent;

public class HungerSystem extends IteratingSystem {

    public HungerSystem() {
        super(Family.all(HungerComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        HungerComponent hunger = entity.getComponent(HungerComponent.class);

        hunger.hungerPoints -= hunger.decayRate * deltaTime;
        if (hunger.hungerPoints < 0) {
            hunger.hungerPoints = 0;
        }        
    }
}

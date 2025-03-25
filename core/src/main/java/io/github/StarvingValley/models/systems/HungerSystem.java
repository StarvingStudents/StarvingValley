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

        hunger.hunger_points -= hunger.decay_rate * deltaTime;
        if (hunger.hunger_points < 0) {
            hunger.hunger_points = 0;
        }        
    }
}

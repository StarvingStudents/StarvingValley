package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import io.github.StarvingValley.models.components.EatingComponent;
import io.github.StarvingValley.models.components.HungerComponent;

public class EatingSystem extends IteratingSystem {
    public EatingSystem() {
        super(Family.all(HungerComponent.class, EatingComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        HungerComponent hunger = entity.getComponent(HungerComponent.class);
        EatingComponent eating = entity.getComponent(EatingComponent.class);

        hunger.hunger_points = Math.min(hunger.max_hunger_points, hunger.hunger_points + eating.food_points); 
    }
}

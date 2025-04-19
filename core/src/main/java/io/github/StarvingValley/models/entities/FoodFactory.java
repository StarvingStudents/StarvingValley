package io.github.StarvingValley.models.entities;

import com.badlogic.ashley.core.Entity;

import io.github.StarvingValley.models.components.FoodItemComponent;
import io.github.StarvingValley.models.components.SpriteComponent;
import io.github.StarvingValley.models.types.PrefabType;

public class FoodFactory {

    public static Entity create(PrefabType type) {
        Entity food = new Entity();

        food.add(new SpriteComponent(type.getIconName()));
        food.add(new FoodItemComponent(20));

        return food;
    }

}

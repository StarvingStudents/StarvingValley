package io.github.StarvingValley.models.entities;

import com.badlogic.ashley.core.Entity;

import io.github.StarvingValley.models.components.FoodItemComponent;
import io.github.StarvingValley.models.components.SpriteComponent;
import io.github.StarvingValley.models.types.PrefabType;

public class FoodItemFactory {
    public static Entity createFoodItem(PrefabType type, float foodPoints) {
        Entity foodItem = new Entity();
        foodItem.add(new SpriteComponent(type.getIconName()));
        foodItem.add(new FoodItemComponent(foodPoints));
        return foodItem;
    }
}

package io.github.StarvingValley.models.components;

import com.badlogic.ashley.core.Component;

public class FoodItemComponent implements Component {
    public float foodPoints; // Number of food points of food item which may be added to the player

    public FoodItemComponent(float foodPoints) {
        this.foodPoints = foodPoints;
    }
}

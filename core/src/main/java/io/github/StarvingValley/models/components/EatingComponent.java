package io.github.StarvingValley.models.components;

import com.badlogic.ashley.core.Component;

public class EatingComponent implements Component {
    public float foodPoints; // Remaining number of food points of food item which may be added to the player
    public boolean isEating; // Whether the player is currently eating
    public float eatingSpeed; // Number of food points added per second
}
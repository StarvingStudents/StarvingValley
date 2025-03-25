package io.github.StarvingValley.models.components;

import com.badlogic.ashley.core.Component;

import io.github.StarvingValley.config.Config;

public class HungerComponent implements Component {
    public float hungerPoints; 
    public float maxHungerPoints = Config.MAX_HUNGER_POINTS;
    public float decayRate = Config.MAX_HUNGER_POINTS / Config.HUNGER_LENGTH;
}
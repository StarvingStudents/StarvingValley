package io.github.StarvingValley.models.components;

import com.badlogic.ashley.core.Component;

import io.github.StarvingValley.config.Config;

public class HungerComponent implements Component {
    public float hunger_points; 
    public float max_hunger_points = Config.max_hunger_points;
    public float decay_rate = Config.max_hunger_points / Config.hunger_length;
}
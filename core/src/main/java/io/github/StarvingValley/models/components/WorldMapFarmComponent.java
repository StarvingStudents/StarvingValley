package io.github.StarvingValley.models.components;

import java.util.Random;

import com.badlogic.ashley.core.Component;

public class WorldMapFarmComponent implements Component {

    String userId; 
    // String houseImage; 

    public WorldMapFarmComponent(String userId) {
        this.userId = userId;
        // String[] houses = { "BlueHouse.png", "GreenHouse.png", "House.png", "OrangeHouse.png", "PinkHouse.png", "PurpleHouse1.png" };
        // this.houseImage = houses[new Random().nextInt(houses.length)];
    }
}

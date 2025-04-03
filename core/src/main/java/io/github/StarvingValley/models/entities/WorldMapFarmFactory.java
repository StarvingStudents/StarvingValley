package io.github.StarvingValley.models.entities;

import java.util.Random;

import com.badlogic.ashley.core.Entity;

import io.github.StarvingValley.models.components.SpriteComponent;

public class WorldMapFarmFactory {

    public static Entity createFarm(String userId) {
        String[] houses = { "BlueHouse.png", "GreenHouse.png", "House.png", "OrangeHouse.png", "PinkHouse.png", "PurpleHouse1.png" };
        // this.houseImage = houses[new Random().nextInt(houses.length)];
        Entity entity = new Entity(); 
        entity.add(new SpriteComponent(houses[new Random().nextInt(houses.length)]));

        return entity; 
    }
    
}

package io.github.StarvingValley.models.entities;

import java.util.Random;

import com.badlogic.ashley.core.Entity;

import io.github.StarvingValley.models.components.SpriteComponent;

public class WorldMapFarmFactory {

    public static Entity createFarm(String userId) {
        String[] houses = { "BlueHouse.png", "GreenHouse.png", "House.png", "OrangeHouse.png", "PinkHouse.png", "PurpleHouse1.png" };
        Random random = new Random();
        String selectedHouse = houses[random.nextInt(houses.length)];
        // String selectedHouse = "House.png";

        // If SpriteComponent accepts a filename and handles texture loading internally:
        Entity entity = new Entity();
        entity.add(new SpriteComponent(selectedHouse));

        // Alternatively, if SpriteComponent should receive a Texture:
        // Texture texture = Assets.getTexture(selectedHouse);
        // entity.add(new SpriteComponent(texture));

        return entity; 
    }
    
}

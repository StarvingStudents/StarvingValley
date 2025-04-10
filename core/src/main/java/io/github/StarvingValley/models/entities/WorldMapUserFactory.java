package io.github.StarvingValley.models.entities;

import java.util.Random;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;

import io.github.StarvingValley.models.components.ActiveWorldEntityComponent;
import io.github.StarvingValley.models.components.ClickableComponent;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.components.SizeComponent;
import io.github.StarvingValley.models.components.SpriteComponent;
import io.github.StarvingValley.models.components.WorldMapFarmComponent;

public class WorldMapUserFactory {

    public static Entity create(String userId, float x, float y) {
        String[] houses = {
                "Sprout Lands - Sprites - Basic pack\\Sprout Lands - Sprites - Basic pack\\BlueHouse.png",
                "Sprout Lands - Sprites - Basic pack\\Sprout Lands - Sprites - Basic pack\\GreenHouse.png",
                "Sprout Lands - Sprites - Basic pack\\Sprout Lands - Sprites - Basic pack\\House.png",
                "Sprout Lands - Sprites - Basic pack\\Sprout Lands - Sprites - Basic pack\\OrangeHouse.png",
                "Sprout Lands - Sprites - Basic pack\\Sprout Lands - Sprites - Basic pack\\PinkHouse.png",
                "Sprout Lands - Sprites - Basic pack\\Sprout Lands - Sprites - Basic pack\\PurpleHouse1.png" };
        Random random = new Random();
        String selectedHouse = houses[random.nextInt(houses.length)];
        float size = 0.8f + random.nextFloat() * 1.4f;
        Entity entity = new Entity();
        entity.add(new PositionComponent(x, y))
                .add(new SpriteComponent(selectedHouse))
                .add(new SizeComponent(size, size))
                .add(new ClickableComponent())
                .add(new ActiveWorldEntityComponent())
                .add(new WorldMapFarmComponent(userId));

        return entity;
    }

}

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
        float size = 1.4f + random.nextFloat() * .8f;

        float positionYModifier = 0;

        if (y <= 0.5f) {
            positionYModifier = random.nextFloat() * 1.6f - 1.4f;
        } else {
            positionYModifier = random.nextFloat() * 1.6f + 1.4f;
        }

        // float positionXModifier = random.nextFloat() * 0.8f - 0.4f;
        float positionXModifier = random.nextFloat() * 0.25f - 0.125f;

        Entity entity = new Entity();
        entity.add(new PositionComponent(x + positionXModifier, y + positionYModifier))
                .add(new SpriteComponent(selectedHouse))
                .add(new SizeComponent(size, size))
                .add(new ClickableComponent())
                .add(new ActiveWorldEntityComponent())
                .add(new WorldMapFarmComponent(userId));

        return entity;
    }

}

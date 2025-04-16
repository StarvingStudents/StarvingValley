package io.github.StarvingValley.models.entities;

import java.util.Random;

import com.badlogic.ashley.core.Entity;

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

        float yPositionVariability = 1f;
        float yCenterOffset = 1f;

        if (y <= 0.5f) {
            positionYModifier = random.nextFloat() * yPositionVariability - yCenterOffset - size / 4;
        } else {
            positionYModifier = random.nextFloat() * yPositionVariability + yCenterOffset + size / 4;
        }

        float xPositionVariability = 0.25f - size / 4;
        float xCenterOffset = 0.125f;
        float positionXModifier = random.nextFloat() * xPositionVariability - xCenterOffset;

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

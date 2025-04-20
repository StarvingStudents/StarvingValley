package io.github.StarvingValley.models.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;

import io.github.StarvingValley.models.components.ButtonComponent;
import io.github.StarvingValley.models.components.ClickableComponent;
import io.github.StarvingValley.models.components.HudComponent;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.components.SizeComponent;
import io.github.StarvingValley.models.components.SpriteComponent;
import io.github.StarvingValley.models.types.ButtonType;

public class HUDButtonFactory {

    public static Entity createHUDButton(float x, float y, float width, float height, String texturePath,
            ButtonType buttonType) {
        Entity button = new Entity();

        button.add(new ClickableComponent())
                .add(new SizeComponent(width, height))
                .add(new PositionComponent(x, y))
                .add(new SpriteComponent(texturePath))
                .add(new ButtonComponent(buttonType))
                .add(new HudComponent());

        return button;
    }

    public static Entity createEatingButton() {
        return HUDButtonFactory.createHUDButton((int) (Gdx.graphics.getWidth() * 0.85),
                (int) (Gdx.graphics.getHeight() * 0.2), (int) (Gdx.graphics.getHeight()
                        * 0.15),
                (int) (Gdx.graphics.getHeight() * 0.15),
                "EAT_egg.png",
                ButtonType.EATING_BUTTON);
    }

    public static Entity createFarmToWorldMapButton() {
        return HUDButtonFactory.createHUDButton((int) (Gdx.graphics.getWidth() * 0.85),
                (int) (Gdx.graphics.getHeight() * 0.75), (int) (Gdx.graphics.getHeight()
                        * 0.15),
                (int) (Gdx.graphics.getHeight() * 0.15),
                "farm_to_world_map_button.png",
                ButtonType.FARM_TO_WORLD_MAP_BUTTON);
    }

    public static Entity createWorldMapToFarmButton() {
        return HUDButtonFactory.createHUDButton((int) (Gdx.graphics.getWidth() * 0.85),
                (int) (Gdx.graphics.getHeight() * 0.75), (int) (Gdx.graphics.getHeight()
                        * 0.15),
                (int) (Gdx.graphics.getHeight() * 0.15),
                "world_map_to_farm_button.png",
                ButtonType.WORLD_MAP_TO_FARM_BUTTON);
    }

    public static Entity createPickupButton() {
        return HUDButtonFactory.createHUDButton((int) (Gdx.graphics.getWidth() * 0.15),
                (int) (Gdx.graphics.getHeight() * 0.2), (int) (Gdx.graphics.getHeight()
                        * 0.15),
                (int) (Gdx.graphics.getHeight() * 0.15),
                "pickup_button.png",
                ButtonType.PICKUP_BUTTON);
    }
}

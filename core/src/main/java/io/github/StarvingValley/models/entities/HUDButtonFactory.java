package io.github.StarvingValley.models.entities;

import com.badlogic.ashley.core.Entity;
import io.github.StarvingValley.models.components.ButtonComponent;
import io.github.StarvingValley.models.components.ClickableComponent;
import io.github.StarvingValley.models.components.HudComponent;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.components.SizeComponent;
import io.github.StarvingValley.models.components.SpriteComponent;
import io.github.StarvingValley.models.types.GameContext;
import io.github.StarvingValley.models.types.ButtonType;

public class HUDButtonFactory {

    public static Entity createHUDButton(float x, float y, float width, float height, String texturePath,
            GameContext context, ButtonType buttonType) {
        Entity button = new Entity();

        button.add(new ClickableComponent())
                .add(new SizeComponent(width, height))
                .add(new PositionComponent(x, y))
                .add(new SpriteComponent(texturePath))
                .add(new ButtonComponent(buttonType))
                .add(new HudComponent());

        return button;
    }
}

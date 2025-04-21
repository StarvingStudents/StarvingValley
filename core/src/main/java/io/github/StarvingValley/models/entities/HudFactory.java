package io.github.StarvingValley.models.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.AttackComponent;
import io.github.StarvingValley.models.components.ButtonComponent;
import io.github.StarvingValley.models.components.ClickableComponent;
import io.github.StarvingValley.models.components.EconomyComponent;
import io.github.StarvingValley.models.components.HudComponent;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.components.SizeComponent;
import io.github.StarvingValley.models.components.SpriteComponent;
import io.github.StarvingValley.models.components.TextComponent;
import io.github.StarvingValley.models.types.ButtonType;
import io.github.StarvingValley.models.types.GameContext;
import io.github.StarvingValley.utils.TileUtils;

public class HudFactory {
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
                return HudFactory.createHUDButton((int) (Gdx.graphics.getWidth() * 0.85),
                                (int) (Gdx.graphics.getHeight() * 0.2), (int) (Gdx.graphics.getHeight()
                                                * 0.15),
                                (int) (Gdx.graphics.getHeight() * 0.15),
                                "EAT_egg.png",
                                ButtonType.EATING_BUTTON);
        }

        public static Entity createFarmToWorldMapButton() {
                return HudFactory.createHUDButton((int) (Gdx.graphics.getWidth() * 0.85),
                                (int) (Gdx.graphics.getHeight() * 0.75), (int) (Gdx.graphics.getHeight()
                                                * 0.15),
                                (int) (Gdx.graphics.getHeight() * 0.15),
                                "farm_to_world_map_button.png",
                                ButtonType.FARM_TO_WORLD_MAP_BUTTON);
        }

        public static Entity createWorldMapToFarmButton() {
                return HudFactory.createHUDButton((int) (Gdx.graphics.getWidth() * 0.85),
                                (int) (Gdx.graphics.getHeight() * 0.75), (int) (Gdx.graphics.getHeight()
                                                * 0.15),
                                (int) (Gdx.graphics.getHeight() * 0.15),
                                "world_map_to_farm_button.png",
                                ButtonType.WORLD_MAP_TO_FARM_BUTTON);
        }

        public static Entity createPickupButton() {
                return HudFactory.createHUDButton((int) (Gdx.graphics.getWidth() * 0.15),
                        (int) (Gdx.graphics.getHeight() * 0.2), (int) (Gdx.graphics.getHeight()
                                * 0.15),
                        (int) (Gdx.graphics.getHeight() * 0.15),
                        "pickup_button.png",
                        ButtonType.PICKUP_BUTTON);
        }

        public static Entity createEconomyBar(GameContext context) {
                float tileSize = TileUtils.getTileWidth();

                float x = tileSize * 2/3;
                float y = (float) (Gdx.graphics.getHeight() - tileSize * 3/2);

                Entity entity = new Entity();
                entity.add(new SizeComponent(
                                tileSize * 3,
                                tileSize))
                                .add(new PositionComponent(x, y))
                                .add(new SpriteComponent("economy_bar.png"))
                                .add(new HudComponent())
                                .add(new TextComponent(() -> {
                                        if (context.player == null)
                                                return "";

                                        EconomyComponent economy = Mappers.economy.get(context.player);
                                        return economy == null ? "" : "$" + economy.balance;

                                }, tileSize, tileSize * 2 / 3, Color.BLACK));

                return entity;
        }

        public static Entity createAttackTimerDisplay(GameContext context) {
                float tileSize = TileUtils.getTileWidth();

                float width = tileSize * 4;
                float height = tileSize * 2 / 3;

                float x = Gdx.graphics.getWidth() / 2 - (width / 2);
                float y = (float) (Gdx.graphics.getHeight() - height);

                TextComponent textComponent = new TextComponent(() -> {
                        if (context.player == null)
                                return "";

                        AttackComponent attack = Mappers.attack.get(context.player);
                        if (attack == null)
                                return "";

                        int time = Math.max(0, (int) attack.timeRemaining);
                        return "Attack ends in: " + time + "s";
                }, 0, 0, Color.BLACK);
                textComponent.scale = 6f;

                Entity entity = new Entity();
                entity.add(new SizeComponent(
                                width,
                                height))
                                .add(new PositionComponent(x, y))
                                .add(new HudComponent())
                                .add(textComponent);

                return entity;
        }
}

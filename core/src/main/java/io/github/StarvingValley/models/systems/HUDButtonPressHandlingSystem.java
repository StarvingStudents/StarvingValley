package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import io.github.StarvingValley.models.events.EatingButtonPressedEvent;
import io.github.StarvingValley.models.events.GameMenuOpenEvent;
import io.github.StarvingValley.models.events.ScreenTransitionEvent;
import io.github.StarvingValley.models.types.ButtonType;
import io.github.StarvingValley.models.types.GameContext;
import io.github.StarvingValley.models.types.ScreenType;
import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.ButtonComponent;
import io.github.StarvingValley.models.components.ClickedComponent;

public class HUDButtonPressHandlingSystem extends IteratingSystem {

    private GameContext context;

    public HUDButtonPressHandlingSystem(GameContext context) {
        super(Family.all(ButtonComponent.class, ClickedComponent.class).get());
        this.context = context;
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        ButtonComponent button = Mappers.button.get(entity);
        ClickedComponent clicked = Mappers.clicked.get(entity);

        if (button == null || clicked == null) {
            System.out.println("HUDButtonPressHandlingSystem: Button or Clicked component is null");
            return;
        }

        if (button.buttonType == ButtonType.EATING_BUTTON) {
            context.eventBus.publish(new EatingButtonPressedEvent());
        }

        if (button.buttonType == ButtonType.FARM_TO_WORLD_MAP_BUTTON) {
            context.eventBus.publish(new ScreenTransitionEvent(ScreenType.WORLD_MAP));
        }

        if (button.buttonType == ButtonType.WORLD_MAP_TO_FARM_BUTTON) {
            context.eventBus.publish(new ScreenTransitionEvent(ScreenType.FARM));
        }

        if (button.buttonType == ButtonType.MENU_BUTTON) {
            context.eventBus.publish(new GameMenuOpenEvent());
        }
    }
}

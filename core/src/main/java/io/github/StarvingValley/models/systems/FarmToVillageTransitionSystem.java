package io.github.StarvingValley.models.systems;

import static io.github.StarvingValley.config.Config.FARM_TO_VILLAGE_BOUNDARY;
import static io.github.StarvingValley.config.Config.VILLAGE_TO_FARM_BOUNDARY;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.CurrentScreenComponent;
import io.github.StarvingValley.models.components.PlayerComponent;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.events.ScreenTransitionEvent;
import io.github.StarvingValley.models.types.GameContext;
import io.github.StarvingValley.models.types.ScreenType;

public class FarmToVillageTransitionSystem extends IteratingSystem {
    private final float SPAWN_OFFSET = 0.5f;
    private final GameContext context;

    public FarmToVillageTransitionSystem(GameContext context) {
        super(Family.all(PlayerComponent.class, PositionComponent.class, CurrentScreenComponent.class).get());
        this.context = context;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent position = Mappers.position.get(entity);
        CurrentScreenComponent screen = Mappers.currScreen.get(entity);
        FirebaseSyncSystem fbSync = getEngine().getSystem(FirebaseSyncSystem.class);

        if (screen.currentScreen == ScreenType.FARM && position.position.x > FARM_TO_VILLAGE_BOUNDARY) {
            context.eventBus.publish(new ScreenTransitionEvent(ScreenType.VILLAGE));
            position.position.x = VILLAGE_TO_FARM_BOUNDARY + SPAWN_OFFSET;
            screen.currentScreen = ScreenType.VILLAGE;
            fbSync.updateInterval(); // force FB sync
        } else if (screen.currentScreen == ScreenType.VILLAGE && position.position.x < VILLAGE_TO_FARM_BOUNDARY) {
            context.eventBus.publish(new ScreenTransitionEvent(ScreenType.FARM));
            position.position.x = FARM_TO_VILLAGE_BOUNDARY - SPAWN_OFFSET;
            screen.currentScreen = ScreenType.FARM;
            fbSync.updateInterval(); // force FB sync
        }
    }
}

package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.ClickedComponent;
import io.github.StarvingValley.models.components.WorldMapFarmComponent;
import io.github.StarvingValley.models.events.WorldMapFarmClickEvent;
import io.github.StarvingValley.models.types.GameContext;

public class WorldMapTransitionSystem extends IteratingSystem {

    private GameContext context;

    public WorldMapTransitionSystem(GameContext context) {
        super(Family.all(WorldMapFarmComponent.class, ClickedComponent.class).get());
        this.context = context;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        context.eventBus.publish(new WorldMapFarmClickEvent(Mappers.worldMapFarm.get(entity).userId));
    }

}

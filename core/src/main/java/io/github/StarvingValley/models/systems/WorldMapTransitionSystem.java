package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;

import com.badlogic.ashley.systems.IteratingSystem;

import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.CameraComponent;
import io.github.StarvingValley.models.components.ClickedComponent;
import io.github.StarvingValley.models.components.MapRenderComponent;
import io.github.StarvingValley.models.components.WorldMapFarmComponent;
import io.github.StarvingValley.models.events.EventBus;
import io.github.StarvingValley.models.events.WorldMapFarmClickEvent;

public class WorldMapTransitionSystem extends IteratingSystem {

    private EventBus eventBus;

    public WorldMapTransitionSystem(EventBus eventBus) {
        super(Family.all(WorldMapFarmComponent.class, ClickedComponent.class).get());
        this.eventBus = eventBus;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        eventBus.publish(new WorldMapFarmClickEvent(Mappers.worldMapFarm.get(entity).userId));
    }

}

package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.StarvingValley.models.Interfaces.Event;
import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.AnimationComponent;
import io.github.StarvingValley.models.components.BuildableComponent;
import io.github.StarvingValley.models.components.PlayerComponent;
import io.github.StarvingValley.models.events.EntityPlacedEvent;
import io.github.StarvingValley.models.events.PlayerAttackedEntityEvent;
import io.github.StarvingValley.models.types.GameContext;
import io.github.StarvingValley.models.types.PrefabType;

public class ActionAnimationSystem extends EntitySystem {
    private final GameContext context;
    interface ActionResolver<T extends Event> {String resolveAction(T event);}
    private final Map<Class<? extends Event>, ActionResolver<? extends Event>> resolvers = new HashMap<>();


    public ActionAnimationSystem(GameContext context) {
        this.context = context;

        // "Soil" action resolver
        resolvers.put(EntityPlacedEvent.class, (EntityPlacedEvent event) -> {
            BuildableComponent buildable = Mappers.buildable.get(event.getEntity());
            if (buildable != null && buildable.builds == PrefabType.SOIL) {
                return "soil";
            }
            return null;
        });

        // "Axe" action resolver for damaging entities
        resolvers.put(PlayerAttackedEntityEvent.class, (PlayerAttackedEntityEvent event) -> "axe");

        // For new events resolvers.put(AnotherEvent.class, (AnotherEvent e) -> "some_action");
    }

    @Override
    public void update(float deltaTime) {
        for (Map.Entry<Class<? extends Event>, ActionResolver<? extends Event>> entry : resolvers.entrySet()) {
            Class<? extends Event> eventType = entry.getKey();
            ActionResolver resolver = entry.getValue();

            List<? extends Event> events = context.eventBus.getEvents(eventType);
            for (Event event : events) {
                String action = resolver.resolveAction(event);
                if (action != null) {
                    triggerPlayerAction(action);
                }
            }
        }
    }

    private void triggerPlayerAction(String actionBase) {
        ImmutableArray<Entity> players = getEngine().getEntitiesFor(
            Family.all(PlayerComponent.class, AnimationComponent.class).get());

        if (players.size() == 0) return;

        Entity player = players.first();
        AnimationComponent anim = Mappers.animation.get(player);

        String direction = "down";
        if (anim.currentAnimation.contains("_")) {
            String[] parts = anim.currentAnimation.split("_");
            if (parts.length > 1) direction = parts[1];
        }

        anim.currentAnimation = "action_" + actionBase + "_" + direction;
        anim.stateTime = 0f;
    }
}

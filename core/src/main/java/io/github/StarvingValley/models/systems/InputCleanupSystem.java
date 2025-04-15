package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import io.github.StarvingValley.models.components.ClickedComponent;
import io.github.StarvingValley.models.components.DragEndComponent;
import io.github.StarvingValley.models.components.DraggingComponent;

public class InputCleanupSystem extends EntitySystem {
    @Override
    public void update(float delta) {
        ImmutableArray<Entity> clickedEntities = getEngine().getEntitiesFor(Family.all(ClickedComponent.class).get());
        for (Entity clickedEntity : clickedEntities) {
            clickedEntity.remove(ClickedComponent.class);
        }

        ImmutableArray<Entity> draggedEntities = getEngine()
                .getEntitiesFor(Family.all(DraggingComponent.class, DragEndComponent.class).get());
        for (Entity draggedEntity : draggedEntities) {
            draggedEntity.remove(DraggingComponent.class);
            draggedEntity.remove(DragEndComponent.class);
        }
    }
}

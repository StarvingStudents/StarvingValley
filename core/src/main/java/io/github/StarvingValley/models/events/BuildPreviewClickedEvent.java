package io.github.StarvingValley.models.events;

import com.badlogic.ashley.core.Entity;

public class BuildPreviewClickedEvent extends EntityEvent {

    public BuildPreviewClickedEvent(Entity entity) {
        super(entity);
    }
}

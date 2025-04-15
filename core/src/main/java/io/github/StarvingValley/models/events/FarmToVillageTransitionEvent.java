package io.github.StarvingValley.models.events;

import io.github.StarvingValley.models.Interfaces.Event;
import io.github.StarvingValley.models.types.ViewType;

public class FarmToVillageTransitionEvent implements Event {
    private ViewType targetView;

    public FarmToVillageTransitionEvent(ViewType targetView) {
        this.targetView = targetView;
    }

    public ViewType getTargetView() {
        return targetView;
    }
}

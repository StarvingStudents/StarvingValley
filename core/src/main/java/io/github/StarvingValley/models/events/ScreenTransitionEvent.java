package io.github.StarvingValley.models.events;

import io.github.StarvingValley.models.interfaces.Event;
import io.github.StarvingValley.models.types.ScreenType;

public class ScreenTransitionEvent implements Event {
    private ScreenType targetScreen;

    public ScreenTransitionEvent(ScreenType targetScreen) {
        this.targetScreen = targetScreen;
    }

    public ScreenType getTargetScreen() {
        return targetScreen;
    }
}

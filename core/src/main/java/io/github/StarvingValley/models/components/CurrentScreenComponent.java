package io.github.StarvingValley.models.components;

import com.badlogic.ashley.core.Component;

import io.github.StarvingValley.models.types.ScreenType;

public class CurrentScreenComponent implements Component {
    public ScreenType currentScreen;

    public CurrentScreenComponent(ScreenType currentScreen) {
        this.currentScreen = currentScreen;
    }
}

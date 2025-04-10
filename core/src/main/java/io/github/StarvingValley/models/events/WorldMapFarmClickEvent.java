package io.github.StarvingValley.models.events;

import io.github.StarvingValley.models.Interfaces.Event;

public class WorldMapFarmClickEvent implements Event {

    public String userId;

    public WorldMapFarmClickEvent(String userId) {
        this.userId = userId;
    }
}

package io.github.StarvingValley.models.components;

import com.badlogic.ashley.core.Component;

public class WorldMapFarmComponent implements Component{

    public String userId; 

    public WorldMapFarmComponent(String userId) {
        this.userId = userId;
    }
    
}

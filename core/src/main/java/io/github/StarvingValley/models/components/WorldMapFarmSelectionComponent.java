package io.github.StarvingValley.models.components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;

import io.github.StarvingValley.models.entities.WorldMapFarmFactory;

// import java.awt.Point;

public class WorldMapFarmSelectionComponent implements Component {

    // Singleton instance
    private static WorldMapFarmSelectionComponent instance = null;

    public List<Entity> farms = new ArrayList<>(); // List to store farm entities
    public Map<Vector2, Entity> gridToFarm = new HashMap<>(); // Map to store grid points and their corresponding farm entities

    public static synchronized WorldMapFarmSelectionComponent getInstance(List<String> userIds) {
        if (instance == null) {
            instance = new WorldMapFarmSelectionComponent(userIds);
        }
        return instance;
    }

    // Made constructor private for singleton
    private WorldMapFarmSelectionComponent(List<String> userIds) {
        // Generate all grid Vector2s using Config constants
        List<Vector2> gridPoints = new ArrayList<>();
        for (int x = 0; x < io.github.StarvingValley.config.Config.WORLD_MAP_GRID_WIDTH; x++) {
            for (int y = 0; y < io.github.StarvingValley.config.Config.WORLD_MAP_GRID_HEIGHT; y++) {
                gridPoints.add(new Vector2(x, y));
            }
        }
        Collections.shuffle(gridPoints);
        int numberOfPoints = Math.min(userIds.size(), gridPoints.size());
        List<Vector2> selectedPoints = gridPoints.subList(0, numberOfPoints);
        
        // Retrieve and shuffle farm entities
        for (String userId : userIds) {
            farms.add(WorldMapFarmFactory.createFarm(userId));
        }
        Collections.shuffle(farms);
        
        for (int i = 0; i < numberOfPoints && i < farms.size(); i++) {
            gridToFarm.put(selectedPoints.get(i), farms.get(i));
        }
    }
}

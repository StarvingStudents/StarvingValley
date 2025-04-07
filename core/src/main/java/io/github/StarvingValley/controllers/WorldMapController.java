package io.github.StarvingValley.controllers;

import java.util.List;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;

import io.github.StarvingValley.models.Interfaces.IFirebaseRepository;
import io.github.StarvingValley.models.entities.WorldMapFarmFactory;

import java.util.ArrayList;
import java.util.Collections; // added import
// import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

public class WorldMapController {

    private IFirebaseRepository firebaseRepository;


    public WorldMapController(IFirebaseRepository firebaseRepository) {
        this.firebaseRepository = firebaseRepository;
    }
    
    
    public List<String> randomUserIds(int numberOfUsers) {
        List<String> allUserIds = firebaseRepository.getAllUserIds();
        
        Collections.shuffle(allUserIds); // shuffle the list
        List<String> userIds = new ArrayList<>();
        for (int i = 0; i < numberOfUsers && i < allUserIds.size(); i++) {
            userIds.add(allUserIds.get(i));
        }
        return userIds;
    }
    
    public List<Entity> getAllFarms(List<String> userIds) {
        List<Entity> farms = new ArrayList<>();
        for (String userId : userIds) {
            farms.add(WorldMapFarmFactory.createFarm(userId)); // replaced createFarm(userId) with WorldMapFarmFactory.createFarm(userId)
        }
        return farms;
    }
    
    // New method: assigns farm entities to random grid points
    public Map<Vector2, Entity> assignFarmsToGrid(List<String> userIds) {
        // Generate all grid points using Config constants
        List<Vector2> gridPoints = new ArrayList<>();
        for (int x = 0; x < io.github.StarvingValley.config.Config.WORLD_MAP_GRID_WIDTH; x++) {
            for (int y = 0; y < io.github.StarvingValley.config.Config.WORLD_MAP_GRID_HEIGHT; y++) {
                gridPoints.add(new Vector2(x, y));
            }
        }
        Collections.shuffle(gridPoints);
        int numberOfPoints = (int) Math.min(io.github.StarvingValley.config.Config.ATTACKABLE_FARMS, gridPoints.size());
        List<Vector2> selectedPoints = gridPoints.subList(0, numberOfPoints);
        
        // Retrieve and shuffle farm entities
        List<Entity> farms = getAllFarms(userIds);
        Collections.shuffle(farms);
        
        Map<Vector2, Entity> gridToFarm = new HashMap<>();
        for (int i = 0; i < numberOfPoints && i < farms.size(); i++) {
            gridToFarm.put(selectedPoints.get(i), farms.get(i));
        }
        return gridToFarm;
    }
}

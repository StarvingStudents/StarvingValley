package io.github.StarvingValley.controllers;

import java.util.List;

import com.badlogic.ashley.core.Entity;

import io.github.StarvingValley.models.Interfaces.IFirebaseRepository;
import io.github.StarvingValley.models.entities.WorldMapFarmFactory;

import java.util.ArrayList;
import java.util.Collections; // added import

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
}

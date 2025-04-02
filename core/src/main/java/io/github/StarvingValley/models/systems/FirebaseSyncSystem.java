package io.github.StarvingValley.models.systems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IntervalSystem;
import com.badlogic.ashley.utils.ImmutableArray;

import io.github.StarvingValley.config.Config;
import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.Interfaces.IFirebaseRepository;
import io.github.StarvingValley.models.Interfaces.PushCallback;
import io.github.StarvingValley.models.components.SyncComponent;
import io.github.StarvingValley.models.components.SyncDeletionRequestComponent;
import io.github.StarvingValley.models.components.UnsyncedComponent;
import io.github.StarvingValley.utils.EntitySerializer;

public class FirebaseSyncSystem extends IntervalSystem {
    private final IFirebaseRepository firebaseRepository;

    public FirebaseSyncSystem(IFirebaseRepository firebaseRepository) {
        super(Config.FIREBASE_SYNC_INTERVAL);

        this.firebaseRepository = firebaseRepository;
    }

    @Override
    protected void updateInterval() {
        Engine engine = getEngine();

        ImmutableArray<Entity> unsyncedEntities = engine
                .getEntitiesFor(Family.all(SyncComponent.class, UnsyncedComponent.class).get());

        ImmutableArray<Entity> entitiesMarkedForRemoval = engine
                .getEntitiesFor(Family.all(SyncDeletionRequestComponent.class).get());

        Map<String, Object> batchData = new HashMap<>();

        List<String> batchDeletionIds = new ArrayList<>();
        for (Entity entity : entitiesMarkedForRemoval) {
            SyncDeletionRequestComponent syncDeletionRequest = Mappers.syncDeletionRequest.get(entity);

            batchDeletionIds.add(syncDeletionRequest.id);
        }

        for (Entity entity : unsyncedEntities) {
            SyncComponent sync = Mappers.sync.get(entity);
            if (batchDeletionIds.contains(sync.id)) {
                continue;
            }

            Object serialized = EntitySerializer.serialize(entity);
            batchData.put(sync.id, serialized);
        }

        if (!batchData.isEmpty()) {
            firebaseRepository.pushEntities(batchData, new PushCallback() {
                @Override
                public void onSuccess() {
                    for (Entity entity : unsyncedEntities) {
                        SyncComponent sync = Mappers.sync.get(entity);

                        entity.remove(UnsyncedComponent.class);
                        System.out.println("Synced entity " + sync.id);

                    }
                }

                @Override
                public void onFailure(String error) {
                    System.err.println("Firebase sync failed: " + error);
                }
            });
        }

        if (!batchDeletionIds.isEmpty()) {
            firebaseRepository.pushEntityDeletions(batchDeletionIds, new PushCallback() {
                @Override
                public void onSuccess() {
                    for (Entity entity : entitiesMarkedForRemoval) {
                        SyncDeletionRequestComponent syncDeletionRequest = Mappers.syncDeletionRequest.get(entity);
                        engine.removeEntity(entity);
                        System.out.println("Deleted entity " + syncDeletionRequest.id);
                    }
                }

                @Override
                public void onFailure(String error) {
                    System.err.println("Firebase deletion failed: " + error);
                }
            });
        }
    }
}

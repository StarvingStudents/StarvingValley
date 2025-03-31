package io.github.StarvingValley.models.systems;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IntervalSystem;
import com.badlogic.ashley.utils.ImmutableArray;

import io.github.StarvingValley.config.Config;
import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.Interfaces.IFirebaseRepository;
import io.github.StarvingValley.models.Interfaces.PushCallback;
import io.github.StarvingValley.models.components.SyncComponent;
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
        ImmutableArray<Entity> unsyncedEntities = getEngine()
                .getEntitiesFor(Family.all(SyncComponent.class, UnsyncedComponent.class).get());

        Map<String, Object> batchData = new HashMap<>();

        for (Entity entity : unsyncedEntities) {
            SyncComponent sync = Mappers.sync.get(entity);
            System.out.println("Synced entity " + sync.id);

            Object serialized = EntitySerializer.serialize(entity);
            batchData.put(sync.id, serialized);
        }

        if (!batchData.isEmpty()) {
            firebaseRepository.pushEntities(batchData, new PushCallback() {
                @Override
                public void onSuccess() {
                    for (Entity entity : unsyncedEntities) {
                        entity.remove(UnsyncedComponent.class);
                    }
                }

                @Override
                public void onFailure(String error) {
                    System.err.println("Firebase sync failed: " + error);
                }
            });
        }
    }
}

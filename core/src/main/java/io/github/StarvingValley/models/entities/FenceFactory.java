package io.github.StarvingValley.models.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.assets.AssetManager;

import io.github.StarvingValley.models.components.ActiveWorldEntityComponent;
import io.github.StarvingValley.models.components.BuildableComponent;

import io.github.StarvingValley.models.components.DurabilityComponent;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.components.SizeComponent;
import io.github.StarvingValley.models.components.SpriteComponent;
import io.github.StarvingValley.models.components.SyncComponent;
import io.github.StarvingValley.models.components.TileOccupierComponent;
import io.github.StarvingValley.models.components.WorldLayerComponent;
import io.github.StarvingValley.models.types.PrefabType;
import io.github.StarvingValley.models.types.WorldLayer;

public class FenceFactory {
    public static Entity createFence(float x, float y, float width, float height, float speed, AssetManager assetManager, Entity camera) {
        Entity fence = new Entity();

        // Basic components
        fence.add(new WorldLayerComponent(WorldLayer.TERRAIN));
        fence.add(new TileOccupierComponent());
        fence.add(new SyncComponent());
        fence.add(new ActiveWorldEntityComponent());

        // Visual components
        fence.add(new SizeComponent(width, height));
        fence.add(new SpriteComponent("Fences.png"));

        // Position component
        fence.add(new PositionComponent(x, y, 0));

        // Durability component
        DurabilityComponent durability = new DurabilityComponent();
        durability.maxHealth = 100;
        durability.health = durability.maxHealth;
        fence.add(durability);

        // Add buildable component
        fence.add(new BuildableComponent(PrefabType.FENCE));

        return fence;
    }

    public static Entity createFence() {
        return createFence(0, 0, 1f, 1f, null, null);
    }

    public static Entity createFence(float x, float y, float width, float height, AssetManager assetManager, Entity camera) {
        return createFence(x, y, width, height, 0, assetManager, camera);
    }
}

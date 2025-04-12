package io.github.StarvingValley.models.entities;

import java.util.Arrays;
import java.util.List;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Vector3;

import io.github.StarvingValley.models.components.ActiveWorldEntityComponent;
import io.github.StarvingValley.models.components.BuildableComponent;
import io.github.StarvingValley.models.components.ClickableComponent;
import io.github.StarvingValley.models.components.DropComponent;
import io.github.StarvingValley.models.components.DurabilityComponent;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.components.SizeComponent;
import io.github.StarvingValley.models.components.SpriteComponent;
import io.github.StarvingValley.models.components.SyncComponent;
import io.github.StarvingValley.models.components.TileOccupierComponent;
import io.github.StarvingValley.models.components.WorldLayerComponent;
import io.github.StarvingValley.models.types.ItemDrop;
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

        /*// Add destroyable component
        DestroyableComponent destroyable = new DestroyableComponent();
        destroyable.destroyRange = 100.03f;
        destroyable.canDestroy = true;
        fence.add(destroyable);*/

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

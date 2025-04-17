package io.github.StarvingValley.models.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

/**
 * Component that marks an entity as pickup-able.
 * Contains information about the pickup range
 */
public class PickupComponent implements Component, Poolable {
    public float pickupRange = 1.5f;
    public boolean isPickupable = true;

    @Override
    public void reset() {
        pickupRange = 1.5f;
        isPickupable = true;
    }
}

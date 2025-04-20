package io.github.StarvingValley.models.components;

import com.badlogic.ashley.core.Component;

public class PickupComponent implements Component {
    public float pickupRange;

    public PickupComponent(float pickupRange) {
        this.pickupRange = pickupRange;
    }
}
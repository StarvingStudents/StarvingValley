package io.github.StarvingValley.models.components;

import com.badlogic.ashley.core.Component;

public class ItemComponent implements Component {
    public String id;
    public String itemName;

    // Constructor for convenience
    public ItemComponent(String id, String itemname) {
        this.id = id;
        this.itemName = itemname;
    }
}

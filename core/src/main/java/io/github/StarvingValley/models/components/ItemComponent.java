package io.github.StarvingValley.models.components;

import com.badlogic.ashley.core.Component;

public class ItemComponent implements Component {
    public static String id;
    public static String itemName;

    // Constructor for convenience
    public ItemComponent(String id, String itemname) {
        this.id = id;
        this.itemName = itemname;
    }
}

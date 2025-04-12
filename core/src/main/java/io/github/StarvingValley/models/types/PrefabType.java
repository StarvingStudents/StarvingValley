package io.github.StarvingValley.models.types;

public enum PrefabType {
    PLAYER("DogBasic.png"),
    BEETROOT_SEEDS("Sprout Lands - Sprites - Basic pack\\Sprout Lands - Sprites - Basic pack\\beetroot_seeds.png"),
    BEETROOT_CROP("Sprout Lands - Sprites - Basic pack\\Sprout Lands - Sprites - Basic pack\\beetroot_3.png"),//TODO: Use 0 or 3?
    BEETROOT("Sprout Lands - Sprites - Basic pack\\Sprout Lands - Sprites - Basic pack\\beetroot_item.png"),
    WHEAT_CROP("Sprout Lands - Sprites - Basic pack\\Sprout Lands - Sprites - Basic pack\\wheat_3.png"),//TODO: Use 0 or 3?
    WHEAT_SEEDS("Sprout Lands - Sprites - Basic pack\\Sprout Lands - Sprites - Basic pack\\wheat_seeds.png"),
    WHEAT("Sprout Lands - Sprites - Basic pack\\Sprout Lands - Sprites - Basic pack\\wheat_item.png"),
    SOIL("dirt.png");

    private final String iconName;

    PrefabType(String iconName) {
        this.iconName = iconName;
    }

    public String getIconName() {
        return iconName;
    }
}

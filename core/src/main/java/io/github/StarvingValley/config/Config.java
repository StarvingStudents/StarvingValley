package io.github.StarvingValley.config;

public class Config {
    public static final String FIREBASE_DATABASE_URL = "https://starving-valley-default-rtdb.europe-west1.firebasedatabase.app";

    public static final float MAX_HUNGER_POINTS = 100; 
    public static final float HUNGER_LENGTH = 3600; // In seconds
    public static final float PIXELS_PER_TILE = 16;
    public static final float UNIT_SCALE = 1f / PIXELS_PER_TILE;
    public static final String MAP_COLLISION_LAYER_NAME = "colisiones";
    public static final float BOUNDS_BOTTOM_COLLISION_RATIO = 0.4f;
}

package io.github.StarvingValley.config;

public class Config {
  public static final String FIREBASE_DATABASE_URL =
      "https://starving-valley-default-rtdb.europe-west1.firebasedatabase.app";
  public static final float FIREBASE_SYNC_INTERVAL = 0.5f;
  public static final float MAX_HUNGER_POINTS = 100;
  public static final float HUNGER_LENGTH = 3600; // In seconds
  public static final float PIXELS_PER_TILE = 16;
  public static final float UNIT_SCALE = 1f / PIXELS_PER_TILE;
  public static final int CAMERA_TILES_WIDE = 14;
  public static final String MAP_COLLISION_LAYER_NAME = "colisiones";
  public static final String MAP_NON_PLACEMENT_LAYER_NAME = "nonplacement";
  public static final float BOUNDS_BOTTOM_COLLISION_RATIO = 0.4f;
  public static final float BUILD_GRID_LINE_THICKNESS = 1f / Config.PIXELS_PER_TILE;
  public static final float INVENTORY_SLOT_TILE_SIZE_MULTIPLIER = 1.2f;
  public static final float MAX_TAP_DISTANCE = 15f;
  public static final float MAX_TAP_DURATION = 300;
  public static final float COLLISION_MARGIN = 0.1f;
  public static final int ATTACKABLE_FARMS = 5;
  public static final float STARTING_BALANCE = 100;
  public static final float DEFAULT_ATTACK_RANGE =
      100.03f; // TODO: This should probably be in tiles not pixels
  public static final float DEFAULT_ATTACK_SPEED = 1.5f;
  public static final float DEFAULT_DAMAGE_AMOUNT = 10f;
  public static final float FARM_TO_VILLAGE_BOUNDARY = 39.5f;
  public static final float VILLAGE_TO_FARM_BOUNDARY = 0f;
  public static final float ATTACK_DURATION = 60f; // in seconds
}

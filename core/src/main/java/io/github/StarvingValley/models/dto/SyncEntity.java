package io.github.StarvingValley.models.dto;

import io.github.StarvingValley.models.components.CropTypeComponent.CropType;
import io.github.StarvingValley.models.types.WorldLayer;

public class SyncEntity {
    public Float x;
    public Float y;
    public Float z;

    public Boolean isBuildable;
    public Boolean isCollidable;
    public Boolean isEnvironmentCollidable;
    public Boolean isHidden;
    public Boolean occupiesTiles;

    public Boolean isPlayer;
    public Boolean hasInput;
    public Boolean cameraShouldFollow;
    public Boolean hasVelocity;

    public Boolean shouldSync;
    public String id;

    public Float maxHealth;
    public Float health;

    public Float foodPoints;

    public Float hungerPoints;
    public Float maxHungerPoints;
    public Float hungerDecayRate;

    public Float pulseBaseAlpha;
    public Float pulseAmplitude;
    public Float pulseSpeed;

    public Float width;
    public Float height;

    public String texture;

    public Float speed;

    public WorldLayer worldLayer;

    public CropType cropType;

    public Integer growthStage;

    public Boolean canHarvest;

    public Integer timeToGrow;
    public Integer growthProgress;
    public Float growthTimeAccumulator;

    // Required: no-arg constructor for Firebase
    public SyncEntity() {
    }
}

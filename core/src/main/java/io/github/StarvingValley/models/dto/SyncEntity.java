package io.github.StarvingValley.models.dto;

import java.util.List;

import io.github.StarvingValley.models.components.CropTypeComponent.CropType;
import io.github.StarvingValley.models.types.InventoryInfo;
import io.github.StarvingValley.models.types.InventoryType;
import io.github.StarvingValley.models.types.ItemStack;
import io.github.StarvingValley.models.types.PrefabType;
import io.github.StarvingValley.models.types.ScreenType;
import io.github.StarvingValley.models.types.WorldLayer;

public class SyncEntity {
    public Float x;
    public Float y;
    public Float z;

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

    public String plantedTimestamp;

    public Long growthDurationSeconds;

    public Boolean isClickable;
    public Boolean isActiveWorldEntity;

    public PrefabType builds;

    public List<ItemStack> drops;

    public Float balance;

    public InventoryInfo inventory;
    public InventoryInfo hotbar;

    public PrefabType inventoryItemType;
    public Integer inventoryItemQuantity;
    public Integer inventoryItemSlotX;
    public Integer inventoryItemSlotY;
    public String inventoryItemInventoryId;

    public Boolean isHudEntity;
    public InventoryType inventoryTypeEntity;
    public int tradeablePrice;

    public String text;
    public Float textOffsetX = 0f;
    public Float textOffsetY = 0f;

    public Float damageAmount;
    public Float attackRange;
    public Float attackSpeed;

    public ScreenType screen;

    public Float pickupRange;

    // Required: no-arg constructor for Firebase
    public SyncEntity() {
    }
}

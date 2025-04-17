package io.github.StarvingValley.utils;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Vector3;

import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.ActiveWorldEntityComponent;
import io.github.StarvingValley.models.components.AnimationComponent;
import io.github.StarvingValley.models.components.BuildableComponent;
import io.github.StarvingValley.models.components.CameraFollowComponent;
import io.github.StarvingValley.models.components.ClickableComponent;
import io.github.StarvingValley.models.components.CollidableComponent;
import io.github.StarvingValley.models.components.CropTypeComponent;
import io.github.StarvingValley.models.components.CurrentScreenComponent;
import io.github.StarvingValley.models.components.DamageComponent;
import io.github.StarvingValley.models.components.DropComponent;
import io.github.StarvingValley.models.components.DurabilityComponent;
import io.github.StarvingValley.models.components.EconomyComponent;
import io.github.StarvingValley.models.components.EnvironmentCollidableComponent;
import io.github.StarvingValley.models.components.GrowthStageComponent;
import io.github.StarvingValley.models.components.HarvestingComponent;
import io.github.StarvingValley.models.components.HiddenComponent;
import io.github.StarvingValley.models.components.HotbarComponent;
import io.github.StarvingValley.models.components.HudComponent;
import io.github.StarvingValley.models.components.HungerComponent;
import io.github.StarvingValley.models.components.InputComponent;
import io.github.StarvingValley.models.components.InventoryComponent;
import io.github.StarvingValley.models.components.InventoryItemComponent;
import io.github.StarvingValley.models.components.PartOfHotbarComponent;
import io.github.StarvingValley.models.components.PlayerComponent;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.components.PulseAlphaComponent;
import io.github.StarvingValley.models.components.SizeComponent;
import io.github.StarvingValley.models.components.SpeedComponent;
import io.github.StarvingValley.models.components.SpriteComponent;
import io.github.StarvingValley.models.components.SyncComponent;
import io.github.StarvingValley.models.components.TextComponent;
import io.github.StarvingValley.models.components.TileOccupierComponent;
import io.github.StarvingValley.models.components.TimeToGrowComponent;
import io.github.StarvingValley.models.components.VelocityComponent;
import io.github.StarvingValley.models.components.WorldLayerComponent;
import io.github.StarvingValley.models.dto.SyncEntity;
import io.github.StarvingValley.models.types.InventoryInfo;

public class EntitySerializer {

  public static SyncEntity serialize(Entity entity) {
    SyncEntity dto = new SyncEntity();

    // Sync ID
    SyncComponent sync = Mappers.sync.get(entity);
    if (sync != null) {
      dto.id = sync.id;
      dto.shouldSync = true;
    }

    // Position
    PositionComponent position = Mappers.position.get(entity);
    if (position != null) {
      Vector3 pos = position.position;
      dto.x = pos.x;
      dto.y = pos.y;
      dto.z = pos.z;
    }

    // CurrentScreen
    CurrentScreenComponent screen = Mappers.currScreen.get(entity);
    if (screen != null) {
      dto.screen = screen.currentScreen;
    }

    // Speed
    SpeedComponent speed = Mappers.speed.get(entity);
    if (speed != null) {
      dto.speed = speed.speed;
    }

    // Size
    SizeComponent size = Mappers.size.get(entity);
    if (size != null) {
      dto.width = size.width;
      dto.height = size.height;
    }

    // Durability
    DurabilityComponent durability = Mappers.durability.get(entity);
    if (durability != null) {
      dto.health = durability.health;
      dto.maxHealth = durability.maxHealth;
    }

    // PulseAlpha
    PulseAlphaComponent pulse = Mappers.pulseAlpha.get(entity);
    if (pulse != null) {
      dto.pulseBaseAlpha = pulse.baseAlpha;
      dto.pulseAmplitude = pulse.amplitude;
      dto.pulseSpeed = pulse.speed;
    }

    // Hunger
    HungerComponent hunger = Mappers.hunger.get(entity);
    if (hunger != null) {
      dto.hungerPoints = hunger.hungerPoints;
      dto.maxHungerPoints = hunger.maxHungerPoints;
      dto.hungerDecayRate = hunger.decayRate;
    }

    // Texture
    SpriteComponent sprite = Mappers.sprite.get(entity);
    if (sprite != null) {
      dto.texture = sprite.getTexturePath();
    }

    // Layer
    WorldLayerComponent layer = Mappers.worldLayer.get(entity);
    if (layer != null) {
      dto.worldLayer = layer.layer;
    }

    // Crop type
    CropTypeComponent cropType = Mappers.cropType.get(entity);
    if (cropType != null) {
      dto.cropType = cropType.cropType;
    }

    // Growth stage
    GrowthStageComponent growthStage = Mappers.growthStage.get(entity);
    if (growthStage != null) {
      dto.growthStage = growthStage.growthStage;
    }

    // Harvesting
    HarvestingComponent harvesting = Mappers.harvesting.get(entity);
    if (harvesting != null) {
      dto.canHarvest = harvesting.canHarvest;
    }

    // Time to grow
    TimeToGrowComponent timeToGrow = Mappers.timeToGrow.get(entity);
    if (timeToGrow != null) {
      dto.plantedTimestamp =
          timeToGrow.plantedTime != null ? timeToGrow.plantedTime.toString() : null;
      dto.growthDurationSeconds =
          timeToGrow.growthDuration != null ? timeToGrow.growthDuration.getSeconds() : null;
    }

    // Buildable
    BuildableComponent buildable = Mappers.buildable.get(entity);
    if (buildable != null) {
      dto.builds = buildable.builds;
    }

    // Drop
    DropComponent drop = Mappers.drop.get(entity);
    if (drop != null && drop.drops != null && drop.drops.size() > 0) {
      dto.drops = drop.drops;
    }

    // Economy
    EconomyComponent economy = Mappers.economy.get(entity);
    if (economy != null) {
      dto.balance = economy.balance;
    }

    // Inventory
    InventoryComponent inventory = Mappers.inventory.get(entity);
    if (inventory != null) {
      dto.inventory = inventory.info;
    }

    // Hotbar
    HotbarComponent hotbar = Mappers.hotbar.get(entity);
    if (hotbar != null) {
      dto.hotbar = hotbar.info;
    }

    // Inventory item
    InventoryItemComponent item = Mappers.inventoryItem.get(entity);
    if (item != null) {
      dto.inventoryItemInventoryId = item.inventoryId;
      dto.inventoryItemQuantity = item.quantity;
      dto.inventoryItemType = item.type;
      dto.inventoryItemSlotX = item.slotX;
      dto.inventoryItemSlotY = item.slotY;
    }

    // Damage
    DamageComponent damage = Mappers.damage.get(entity);
    if (damage != null) {
      dto.damageAmount = damage.damageAmount;
      dto.attackRange = damage.attackRange;
      dto.attackSpeed = damage.attackSpeed;
    }
    
    // Text
    TextComponent text = Mappers.text.get(entity);
    if (text != null) {
      dto.text = text.text;
      dto.textOffsetX = text.offsetX;
      dto.textOffsetY = text.offsetY;
    }
    
    dto.isCollidable = Mappers.collidable.has(entity);
    dto.isEnvironmentCollidable = Mappers.environmentCollider.has(entity);
    dto.isHidden = Mappers.hidden.has(entity);
    dto.occupiesTiles = Mappers.tileOccupier.has(entity);
    dto.isPlayer = Mappers.player.has(entity);
    dto.hasInput = Mappers.input.has(entity);
    dto.cameraShouldFollow = Mappers.cameraFollow.has(entity);
    dto.hasVelocity = Mappers.velocity.has(entity);
    dto.isClickable = Mappers.clickable.has(entity);
    dto.isActiveWorldEntity = Mappers.activeWorldEntity.has(entity);
    dto.isHudEntity = Mappers.hud.has(entity);
    dto.isHotbarEntity = Mappers.partOfHotbar.has(entity);

    return dto;
  }

  public static Entity deserialize(SyncEntity dto, Entity camera, AssetManager assetManager) {
    Entity entity = new Entity();

    // Position
    if (dto.x != null && dto.y != null) {
      PositionComponent position = new PositionComponent(dto.x, dto.y, dto.z != null ? dto.z : 0f);
      entity.add(position);
    }

    // CurrentScreen
    if (dto.screen != null) {
      CurrentScreenComponent screen = new CurrentScreenComponent(dto.screen);
      entity.add(screen);
    }

    // Speed
    if (dto.speed != null) {
      SpeedComponent speed = new SpeedComponent(dto.speed);
      entity.add(speed);
    }

    // Size
    if (dto.width != null && dto.height != null) {
      SizeComponent size = new SizeComponent(dto.width, dto.height);
      entity.add(size);
    }

    // Durability
    if (dto.health != null && dto.maxHealth != null) {
      DurabilityComponent durability = new DurabilityComponent();
      durability.health = dto.health;
      durability.maxHealth = dto.maxHealth;
      entity.add(durability);
    }

    // PulseAlpha
    if (dto.pulseBaseAlpha != null && dto.pulseAmplitude != null && dto.pulseSpeed != null) {
      PulseAlphaComponent pulse = new PulseAlphaComponent();
      pulse.baseAlpha = dto.pulseBaseAlpha;
      pulse.amplitude = dto.pulseAmplitude;
      pulse.speed = dto.pulseSpeed;
      entity.add(pulse);
    }

    // Hunger
    if (dto.hungerPoints != null) {
      HungerComponent hunger = new HungerComponent();
      hunger.hungerPoints = dto.hungerPoints;
      hunger.maxHungerPoints = dto.maxHungerPoints != null ? dto.maxHungerPoints : 0f;
      hunger.decayRate = dto.hungerDecayRate != null ? dto.hungerDecayRate : 0f;
      entity.add(hunger);
    }

    // // Eating
    // if (dto.foodPoints != null) {
    // EatingComponent eating = new EatingComponent(dto.foodPoints);
    // entity.add(eating);
    // }

    // Animation OR Sprite
    if (dto.builds != null) {
      AnimationComponent anim = AnimationFactory.createAnimationsForType(dto.builds, assetManager);
      if (anim != null) {
        entity.add(anim);
      } else if (dto.texture != null) {
        entity.add(new SpriteComponent(dto.texture));
      }
    } else if (dto.texture != null) {
      entity.add(new SpriteComponent(dto.texture));
    }

    // Layer
    if (dto.worldLayer != null) {
      WorldLayerComponent layer = new WorldLayerComponent(dto.worldLayer);
      entity.add(layer);
    }

    // Sync
    if (dto.shouldSync != null && dto.shouldSync) {
      SyncComponent sync = new SyncComponent();
      sync.id = dto.id != null ? dto.id : UUID.randomUUID().toString();
      entity.add(sync);
    }

    // Crop type
    if (dto.cropType != null) {
      entity.add(new CropTypeComponent(dto.cropType));
    }

    // Growth stage
    if (dto.growthStage != null) {
      entity.add(new GrowthStageComponent(dto.growthStage));
    }

    // Harvesting
    if (dto.canHarvest != null) {
      entity.add(new HarvestingComponent(dto.canHarvest));
    }

    // Time to grow
    if (dto.plantedTimestamp != null && dto.growthDurationSeconds != null) {
      Instant plantedTime = Instant.parse(dto.plantedTimestamp);
      Duration growthDuration = Duration.ofSeconds(dto.growthDurationSeconds);
      TimeToGrowComponent timeToGrowComponent =
          new TimeToGrowComponent(plantedTime, growthDuration);
      entity.add(timeToGrowComponent);
    }

    // Buildable
    if (dto.builds != null) {
      entity.add(new BuildableComponent(dto.builds));
    }

    // Drop
    if (dto.drops != null && dto.drops.size() > 0) {
      entity.add(new DropComponent(dto.drops));
    }

    // Economy
    if (dto.balance != null) {
      entity.add(new EconomyComponent(dto.balance));
    }

    // Inventory
    if (dto.inventory != null) {
      entity.add(
          new InventoryComponent(
              new InventoryInfo(
                  dto.inventory.inventoryId, dto.inventory.width, dto.inventory.height)));
    }

    // Hotbar
    if (dto.hotbar != null) {
      entity.add(
          new HotbarComponent(
              new InventoryInfo(dto.hotbar.inventoryId, dto.hotbar.width, dto.hotbar.height)));
    }

    // Inventory item
    if (dto.inventoryItemInventoryId != null) {
      entity.add(new InventoryItemComponent(dto.inventoryItemType, dto.inventoryItemQuantity, dto.inventoryItemSlotX,
          dto.inventoryItemSlotY, dto.inventoryItemInventoryId));

      entity.remove(PositionComponent.class);
    }
    
    // Damage
    if (dto.damageAmount != null && dto.attackRange != null && dto.attackSpeed != null) {
      entity.add(new DamageComponent(dto.damageAmount, dto.attackRange, dto.attackSpeed));
    }

    // Text
    if (dto.text != null) {
      entity.add(new TextComponent(dto.text, dto.textOffsetX, dto.textOffsetY));
    }

    // Boolean tags
    if (Boolean.TRUE.equals(dto.isCollidable)) entity.add(new CollidableComponent());
    if (Boolean.TRUE.equals(dto.isEnvironmentCollidable))
      entity.add(new EnvironmentCollidableComponent());
    if (Boolean.TRUE.equals(dto.isHidden)) entity.add(new HiddenComponent());
    if (Boolean.TRUE.equals(dto.occupiesTiles)) entity.add(new TileOccupierComponent());
    if (Boolean.TRUE.equals(dto.isPlayer)) entity.add(new PlayerComponent());
    if (Boolean.TRUE.equals(dto.hasInput)) entity.add(new InputComponent());
    if (Boolean.TRUE.equals(dto.cameraShouldFollow)) entity.add(new CameraFollowComponent(camera));
    if (Boolean.TRUE.equals(dto.hasVelocity)) entity.add(new VelocityComponent());
    if (Boolean.TRUE.equals(dto.isClickable)) entity.add(new ClickableComponent());
    if (Boolean.TRUE.equals(dto.isActiveWorldEntity)) entity.add(new ActiveWorldEntityComponent());
    if (Boolean.TRUE.equals(dto.isHudEntity)) entity.add(new HudComponent());
    if (Boolean.TRUE.equals(dto.isHotbarEntity)) entity.add(new PartOfHotbarComponent());

    return entity;
  }
}

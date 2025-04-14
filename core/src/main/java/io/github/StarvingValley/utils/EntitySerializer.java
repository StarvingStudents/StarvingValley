package io.github.StarvingValley.utils;

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
import io.github.StarvingValley.models.components.DropComponent;
import io.github.StarvingValley.models.components.DurabilityComponent;
import io.github.StarvingValley.models.components.EatingComponent;
import io.github.StarvingValley.models.components.EconomyComponent;
import io.github.StarvingValley.models.components.EnvironmentCollidableComponent;
import io.github.StarvingValley.models.components.GrowthStageComponent;
import io.github.StarvingValley.models.components.HarvestingComponent;
import io.github.StarvingValley.models.components.HiddenComponent;
import io.github.StarvingValley.models.components.HungerComponent;
import io.github.StarvingValley.models.components.InputComponent;
import io.github.StarvingValley.models.components.PlayerComponent;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.components.PulseAlphaComponent;
import io.github.StarvingValley.models.components.SizeComponent;
import io.github.StarvingValley.models.components.SpeedComponent;
import io.github.StarvingValley.models.components.SpriteComponent;
import io.github.StarvingValley.models.components.SyncComponent;
import io.github.StarvingValley.models.components.TileOccupierComponent;
import io.github.StarvingValley.models.components.TimeToGrowComponent;
import io.github.StarvingValley.models.components.VelocityComponent;
import io.github.StarvingValley.models.components.WorldLayerComponent;
import io.github.StarvingValley.models.dto.SyncEntity;
import io.github.StarvingValley.models.types.GameContext;
import io.github.StarvingValley.models.components.DamageComponent;
import io.github.StarvingValley.config.Config;

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

    // Eating
    EatingComponent eating = Mappers.eating.get(entity);
    if (eating != null) {
      dto.foodPoints = eating.foodPoints;
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
      dto.timeToGrow = timeToGrow.timeToGrow;
      dto.growthProgress = timeToGrow.growthProgress;
      dto.growthTimeAccumulator = timeToGrow.growthTimeAccumulator;
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

    // Damage
    DamageComponent damage = Mappers.damage.get(entity);
    if (damage != null) {
      dto.damageAmount = damage.damageAmount;
      dto.attackRange = damage.attackRange;
      dto.attackSpeed = damage.attackSpeed;
      dto.cooldownTimer = damage.cooldownTimer;
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

    return dto;
  }

  public static Entity deserialize(SyncEntity dto, Entity camera, AssetManager assetManager) {
    Entity entity = new Entity();

    // Position
    if (dto.x != null && dto.y != null) {
      PositionComponent position = new PositionComponent(dto.x, dto.y, dto.z != null ? dto.z : 0f);
      entity.add(position);
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

    // Eating
    if (dto.foodPoints != null) {
      EatingComponent eating = new EatingComponent(dto.foodPoints);
      entity.add(eating);
    }

      // Animation OR Sprite
      if (dto.builds != null) {
          AnimationComponent anim = AnimationFactory.createAnimationsForType(dto.builds,assetManager );
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
    if (dto.timeToGrow != null && dto.growthProgress != null && dto.growthTimeAccumulator != null) {
      TimeToGrowComponent timeToGrowComponent = new TimeToGrowComponent(0);
      timeToGrowComponent.timeToGrow = dto.timeToGrow;
      timeToGrowComponent.growthProgress = dto.growthProgress;
      timeToGrowComponent.growthTimeAccumulator = dto.growthTimeAccumulator;

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

    // Damage
    if (dto.damageAmount != null) {
      float damageAmount = dto.damageAmount != null ? dto.damageAmount : Config.DEFAULT_DAMAGE_AMOUNT;
      float attackRange = dto.attackRange != null ? dto.attackRange : Config.DEFAULT_ATTACK_RANGE;
      float attackSpeed = dto.attackSpeed != null ? dto.attackSpeed : Config.DEFAULT_ATTACK_SPEED;
      DamageComponent damage = new DamageComponent(damageAmount, attackRange, attackSpeed);
      damage.cooldownTimer = dto.cooldownTimer != null ? dto.cooldownTimer : 0f;
      entity.add(damage);
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
    if (Boolean.TRUE.equals(dto.isClickable))
      entity.add(new ClickableComponent());
    if (Boolean.TRUE.equals(dto.isActiveWorldEntity))
      entity.add(new ActiveWorldEntityComponent());

    return entity;
  }
}

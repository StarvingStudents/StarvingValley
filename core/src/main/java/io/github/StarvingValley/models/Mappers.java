package io.github.StarvingValley.models;

import com.badlogic.ashley.core.ComponentMapper;

import io.github.StarvingValley.models.components.ActiveWorldEntityComponent;
import io.github.StarvingValley.models.components.AnimationComponent;
import io.github.StarvingValley.models.components.AttackComponent;
import io.github.StarvingValley.models.components.BuildPreviewComponent;
import io.github.StarvingValley.models.components.BuildableComponent;
import io.github.StarvingValley.models.components.ButtonComponent;
import io.github.StarvingValley.models.components.CameraComponent;
import io.github.StarvingValley.models.components.CameraFollowComponent;
import io.github.StarvingValley.models.components.ClickableComponent;
import io.github.StarvingValley.models.components.ClickedComponent;
import io.github.StarvingValley.models.components.CollidableComponent;
import io.github.StarvingValley.models.components.CropTypeComponent;
import io.github.StarvingValley.models.components.CurrentScreenComponent;
import io.github.StarvingValley.models.components.DamageComponent;
import io.github.StarvingValley.models.components.DragEndComponent;
import io.github.StarvingValley.models.components.DraggableComponent;
import io.github.StarvingValley.models.components.DraggingComponent;
import io.github.StarvingValley.models.components.DropComponent;
import io.github.StarvingValley.models.components.DurabilityComponent;
import io.github.StarvingValley.models.components.EconomyComponent;
import io.github.StarvingValley.models.components.EnvironmentCollidableComponent;
import io.github.StarvingValley.models.components.FoodItemComponent;
import io.github.StarvingValley.models.components.GrowthStageComponent;
import io.github.StarvingValley.models.components.HarvestingComponent;
import io.github.StarvingValley.models.components.HiddenComponent;
import io.github.StarvingValley.models.components.HotbarComponent;
import io.github.StarvingValley.models.components.HudComponent;
import io.github.StarvingValley.models.components.HungerComponent;
import io.github.StarvingValley.models.components.InputComponent;
import io.github.StarvingValley.models.components.InventoryComponent;
import io.github.StarvingValley.models.components.InventoryItemComponent;
import io.github.StarvingValley.models.components.InventorySlotComponent;
import io.github.StarvingValley.models.components.InventoryToggleButtonComponent;
import io.github.StarvingValley.models.components.MapRenderComponent;
import io.github.StarvingValley.models.components.PartOfHotbarComponent;
import io.github.StarvingValley.models.components.PlayerComponent;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.components.PrefabTypeComponent;
import io.github.StarvingValley.models.components.PulseAlphaComponent;
import io.github.StarvingValley.models.components.SelectedHotbarEntryComponent;
import io.github.StarvingValley.models.components.SizeComponent;
import io.github.StarvingValley.models.components.SpeedComponent;
import io.github.StarvingValley.models.components.SpriteComponent;
import io.github.StarvingValley.models.components.SyncComponent;
import io.github.StarvingValley.models.components.SyncDeletionRequestComponent;
import io.github.StarvingValley.models.components.TextComponent;
import io.github.StarvingValley.models.components.TileOccupierComponent;
import io.github.StarvingValley.models.components.TiledMapComponent;
import io.github.StarvingValley.models.components.TimeToGrowComponent;
import io.github.StarvingValley.models.components.TradeableComponent;
import io.github.StarvingValley.models.components.TradingComponent;
import io.github.StarvingValley.models.components.UnsyncedComponent;
import io.github.StarvingValley.models.components.VelocityComponent;
import io.github.StarvingValley.models.components.WorldLayerComponent;
import io.github.StarvingValley.models.components.WorldMapFarmComponent;
import io.github.StarvingValley.models.components.PickupComponent;

public class Mappers {
        public static final ComponentMapper<PositionComponent> position = ComponentMapper
                        .getFor(PositionComponent.class);
        public static final ComponentMapper<VelocityComponent> velocity = ComponentMapper
                        .getFor(VelocityComponent.class);
        public static final ComponentMapper<SizeComponent> size = ComponentMapper.getFor(SizeComponent.class);
        public static final ComponentMapper<CollidableComponent> collidable = ComponentMapper
                        .getFor(CollidableComponent.class);
        public static final ComponentMapper<EnvironmentCollidableComponent> environmentCollider = ComponentMapper
                        .getFor(EnvironmentCollidableComponent.class);
        public static final ComponentMapper<CameraComponent> camera = ComponentMapper.getFor(CameraComponent.class);
        public static final ComponentMapper<CameraFollowComponent> cameraFollow = ComponentMapper
                        .getFor(CameraFollowComponent.class);
        public static final ComponentMapper<UnsyncedComponent> unsynced = ComponentMapper
                        .getFor(UnsyncedComponent.class);
        public static final ComponentMapper<SyncComponent> sync = ComponentMapper.getFor(SyncComponent.class);
        public static final ComponentMapper<MapRenderComponent> mapRender = ComponentMapper
                        .getFor(MapRenderComponent.class);
        public static final ComponentMapper<PlayerComponent> player = ComponentMapper.getFor(PlayerComponent.class);
        public static final ComponentMapper<SpeedComponent> speed = ComponentMapper.getFor(SpeedComponent.class);
        public static final ComponentMapper<ActiveWorldEntityComponent> activeWorldEntity = ComponentMapper
                        .getFor(ActiveWorldEntityComponent.class);
        public static final ComponentMapper<InputComponent> input = ComponentMapper.getFor(InputComponent.class);
        public static final ComponentMapper<ClickableComponent> clickable = ComponentMapper
                        .getFor(ClickableComponent.class);
        public static final ComponentMapper<DraggableComponent> draggable = ComponentMapper
                        .getFor(DraggableComponent.class);
        public static final ComponentMapper<ClickedComponent> clicked = ComponentMapper.getFor(ClickedComponent.class);
        public static final ComponentMapper<DraggingComponent> dragging = ComponentMapper
                        .getFor(DraggingComponent.class);
        public static final ComponentMapper<DragEndComponent> dragEnd = ComponentMapper.getFor(DragEndComponent.class);
        public static final ComponentMapper<SpriteComponent> sprite = ComponentMapper.getFor(SpriteComponent.class);
        public static final ComponentMapper<TextComponent> text = ComponentMapper.getFor(TextComponent.class);
        public static final ComponentMapper<TiledMapComponent> tiledMap = ComponentMapper
                        .getFor(TiledMapComponent.class);
        public static final ComponentMapper<TileOccupierComponent> tileOccupier = ComponentMapper
                        .getFor(TileOccupierComponent.class);
        public static final ComponentMapper<BuildPreviewComponent> buildPreview = ComponentMapper
                        .getFor(BuildPreviewComponent.class);
        public static final ComponentMapper<HiddenComponent> hidden = ComponentMapper.getFor(HiddenComponent.class);
        public static final ComponentMapper<PulseAlphaComponent> pulseAlpha = ComponentMapper
                        .getFor(PulseAlphaComponent.class);
        public static final ComponentMapper<WorldLayerComponent> worldLayer = ComponentMapper
                        .getFor(WorldLayerComponent.class);
        public static final ComponentMapper<BuildableComponent> buildable = ComponentMapper
                        .getFor(BuildableComponent.class);
        public static final ComponentMapper<HungerComponent> hunger = ComponentMapper.getFor(HungerComponent.class);
        public static final ComponentMapper<DurabilityComponent> durability = ComponentMapper
                        .getFor(DurabilityComponent.class);
        public static final ComponentMapper<GrowthStageComponent> growthStage = ComponentMapper
                        .getFor(GrowthStageComponent.class);
        public static final ComponentMapper<TimeToGrowComponent> timeToGrow = ComponentMapper
                        .getFor(TimeToGrowComponent.class);
        public static final ComponentMapper<HarvestingComponent> harvesting = ComponentMapper
                        .getFor(HarvestingComponent.class);
        public static final ComponentMapper<CropTypeComponent> cropType = ComponentMapper
                        .getFor(CropTypeComponent.class);
        public static final ComponentMapper<SyncDeletionRequestComponent> syncDeletionRequest = ComponentMapper
                        .getFor(SyncDeletionRequestComponent.class);
        public static final ComponentMapper<AnimationComponent> animation = ComponentMapper
                        .getFor(AnimationComponent.class);
        public static final ComponentMapper<DropComponent> drop = ComponentMapper.getFor(DropComponent.class);
        public static final ComponentMapper<WorldMapFarmComponent> worldMapFarm = ComponentMapper
                        .getFor(WorldMapFarmComponent.class);
        public static final ComponentMapper<ButtonComponent> button = ComponentMapper.getFor(ButtonComponent.class);
        public static final ComponentMapper<HudComponent> hud = ComponentMapper.getFor(HudComponent.class);
        public static final ComponentMapper<InventoryItemComponent> inventoryItem = ComponentMapper
                        .getFor(InventoryItemComponent.class);
        public static final ComponentMapper<FoodItemComponent> foodItem = ComponentMapper
                        .getFor(FoodItemComponent.class);
        public static final ComponentMapper<TradingComponent> trading = ComponentMapper.getFor(TradingComponent.class);
        public static final ComponentMapper<TradeableComponent> tradeable = ComponentMapper
                        .getFor(TradeableComponent.class);
        public static final ComponentMapper<EconomyComponent> economy = ComponentMapper.getFor(EconomyComponent.class);
        public static final ComponentMapper<InventoryComponent> inventory = ComponentMapper
                        .getFor(InventoryComponent.class);
        public static final ComponentMapper<InventorySlotComponent> inventorySlot = ComponentMapper
                        .getFor(InventorySlotComponent.class);
        public static final ComponentMapper<HotbarComponent> hotbar = ComponentMapper.getFor(HotbarComponent.class);
        public static final ComponentMapper<SelectedHotbarEntryComponent> selectedHotbarItem = ComponentMapper
                        .getFor(SelectedHotbarEntryComponent.class);
        public static final ComponentMapper<PrefabTypeComponent> prefabType = ComponentMapper
                        .getFor(PrefabTypeComponent.class);
        public static final ComponentMapper<DamageComponent> damage = ComponentMapper.getFor(DamageComponent.class);
        public static final ComponentMapper<CurrentScreenComponent> currScreen = ComponentMapper
                        .getFor(CurrentScreenComponent.class);
        public static final ComponentMapper<AttackComponent> attack = ComponentMapper.getFor(AttackComponent.class);
        public static final ComponentMapper<PartOfHotbarComponent> partOfHotbar = ComponentMapper
                        .getFor(PartOfHotbarComponent.class);
        public static final ComponentMapper<InventoryToggleButtonComponent> inventoryToggleButton = ComponentMapper
                        .getFor(InventoryToggleButtonComponent.class);
        public static final ComponentMapper<PickupComponent> pickup = ComponentMapper.getFor(PickupComponent.class);
}

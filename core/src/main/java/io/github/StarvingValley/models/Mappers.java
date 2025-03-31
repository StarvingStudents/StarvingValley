package io.github.StarvingValley.models;

import com.badlogic.ashley.core.ComponentMapper;

import io.github.StarvingValley.models.components.BuildPreviewComponent;
import io.github.StarvingValley.models.components.BuildableComponent;
import io.github.StarvingValley.models.components.CameraComponent;
import io.github.StarvingValley.models.components.CameraFollowComponent;
import io.github.StarvingValley.models.components.CollidableComponent;
import io.github.StarvingValley.models.components.DurabilityComponent;
import io.github.StarvingValley.models.components.EnvironmentCollidableComponent;
import io.github.StarvingValley.models.components.HiddenComponent;
import io.github.StarvingValley.models.components.HungerComponent;
import io.github.StarvingValley.models.components.MapRenderComponent;
import io.github.StarvingValley.models.components.PlaceRequestComponent;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.components.PulseAlphaComponent;
import io.github.StarvingValley.models.components.SizeComponent;
import io.github.StarvingValley.models.components.SpeedComponent;
import io.github.StarvingValley.models.components.SpriteComponent;
import io.github.StarvingValley.models.components.TileOccupierComponent;
import io.github.StarvingValley.models.components.TileOverlapComponent;
import io.github.StarvingValley.models.components.TiledMapComponent;
import io.github.StarvingValley.models.components.VelocityComponent;
import io.github.StarvingValley.models.components.WorldLayerComponent;

public class Mappers {
    public static final ComponentMapper<PositionComponent> position = ComponentMapper.getFor(PositionComponent.class);
    public static final ComponentMapper<VelocityComponent> velocity = ComponentMapper.getFor(VelocityComponent.class);
    public static final ComponentMapper<SizeComponent> size = ComponentMapper.getFor(SizeComponent.class);
    public static final ComponentMapper<CollidableComponent> collidable = ComponentMapper.getFor(CollidableComponent.class);
    public static final ComponentMapper<EnvironmentCollidableComponent> environmentCollider = ComponentMapper.getFor(EnvironmentCollidableComponent.class);
    public static final ComponentMapper<CameraComponent> camera = ComponentMapper.getFor(CameraComponent.class);
    public static final ComponentMapper<CameraFollowComponent> cameraFollow = ComponentMapper.getFor(CameraFollowComponent.class);
    public static final ComponentMapper<MapRenderComponent> mapRender = ComponentMapper.getFor(MapRenderComponent.class);
    public static final ComponentMapper<SpeedComponent> speed = ComponentMapper.getFor(SpeedComponent.class);
        public static final ComponentMapper<InputComponent> input = ComponentMapper.getFor(InputComponent.class);
    public static final ComponentMapper<SpriteComponent> sprite = ComponentMapper.getFor(SpriteComponent.class);
    public static final ComponentMapper<TiledMapComponent> tiledMap = ComponentMapper.getFor(TiledMapComponent.class);
    public static final ComponentMapper<TileOverlapComponent> tileOccupancy = ComponentMapper
            .getFor(TileOverlapComponent.class);
    public static final ComponentMapper<TileOccupierComponent> placementOccupied = ComponentMapper
                    .getFor(TileOccupierComponent.class);
    public static final ComponentMapper<BuildPreviewComponent> buildPreview = ComponentMapper
                    .getFor(BuildPreviewComponent.class);
    public static final ComponentMapper<HiddenComponent> hidden = ComponentMapper.getFor(HiddenComponent.class);
    public static final ComponentMapper<PulseAlphaComponent> pulseAlpha = ComponentMapper
                    .getFor(PulseAlphaComponent.class);
    public static final ComponentMapper<PlaceRequestComponent> placeRequest = ComponentMapper
                    .getFor(PlaceRequestComponent.class);
    public static final ComponentMapper<WorldLayerComponent> worldLayer = ComponentMapper
                    .getFor(WorldLayerComponent.class);
    public static final ComponentMapper<BuildableComponent> buildable = ComponentMapper
                    .getFor(BuildableComponent.class);
    public static final ComponentMapper<HungerComponent> hunger = ComponentMapper.getFor(HungerComponent.class);
    public static final ComponentMapper<DurabilityComponent> durability = ComponentMapper.getFor(DurabilityComponent.class);
}

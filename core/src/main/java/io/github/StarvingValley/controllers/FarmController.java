package io.github.StarvingValley.controllers;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.github.StarvingValley.config.Config;
import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.Interfaces.IFirebaseRepository;
import io.github.StarvingValley.models.components.CameraComponent;
import io.github.StarvingValley.models.components.TiledMapComponent;
import io.github.StarvingValley.models.entities.CameraFactory;
import io.github.StarvingValley.models.entities.MapFactory;
import io.github.StarvingValley.models.events.EventBus;
import io.github.StarvingValley.models.systems.AlphaPulseSystem;
import io.github.StarvingValley.models.systems.BuildGridRenderSystem;
import io.github.StarvingValley.models.systems.BuildPlacementSystem;
import io.github.StarvingValley.models.systems.BuildPreviewSystem;
import io.github.StarvingValley.models.systems.CameraSystem;
import io.github.StarvingValley.models.systems.ClickedCleanupSystem;
import io.github.StarvingValley.models.systems.ClickSystem;
import io.github.StarvingValley.models.systems.CropGrowthSystem;
import io.github.StarvingValley.models.systems.DurabilityRenderSystem;
import io.github.StarvingValley.models.systems.EnvironmentCollisionSystem;
import io.github.StarvingValley.models.systems.EventCleanupSystem;
import io.github.StarvingValley.models.systems.FirebaseSyncSystem;
import io.github.StarvingValley.models.systems.HarvestingSystem;
import io.github.StarvingValley.models.systems.HungerRenderSystem;
import io.github.StarvingValley.models.systems.HungerSystem;
import io.github.StarvingValley.models.systems.MapRenderSystem;
import io.github.StarvingValley.models.systems.MovementSystem;
import io.github.StarvingValley.models.systems.RenderSystem;
import io.github.StarvingValley.models.systems.SpriteSystem;
import io.github.StarvingValley.models.systems.SyncMarkingSystem;
import io.github.StarvingValley.models.systems.VelocitySystem;
import io.github.StarvingValley.models.types.WorldLayer;
import io.github.StarvingValley.utils.MapUtils;

public class FarmController {

    private final Engine engine;
    private final SpriteBatch batch;
    private final EventBus eventBus;
    private final AssetManager assetManager;
    private final IFirebaseRepository firebaseRepository;

    private Entity camera;
    private Entity map;

    public FarmController(IFirebaseRepository firebaseRepository, EventBus eventBus, AssetManager assetManager) {
        this.firebaseRepository = firebaseRepository;
        this.eventBus = eventBus;
        this.assetManager = assetManager;
        this.engine = new Engine();
        this.batch = new SpriteBatch();
        initGame();
    }

    private void initGame() {
        int tilesWide = Config.CAMERA_TILES_WIDE;
        float tilesHigh = MapUtils.calculateVerticalTileCount(tilesWide);

        camera = CameraFactory.createCamera(tilesWide, tilesHigh);
        CameraComponent cameraComponent = Mappers.camera.get(camera);

        map = MapFactory.createMap("FarmMap.tmx", Config.UNIT_SCALE, cameraComponent);

        engine.addEntity(camera);
        engine.addEntity(map);

        // TODO: Since there's some stuff we send to multiple systems (eventBus, camera,
        // batch etc), maybe we should have a GameContext class that holds them so we
        // just pass around that?
engine.addSystem(new ClickSystem(eventBus));
        engine.addSystem(new MapRenderSystem());
        engine.addSystem(new TileOverlapSystem());
        engine.addSystem(new BuildPreviewSystem(cameraComponent.camera));
        engine.addSystem(new BuildPlacementSystem(eventBus));
        engine.addSystem(new AlphaPulseSystem());
        engine.addSystem(new VelocitySystem());
        engine.addSystem(new EnvironmentCollisionSystem());
        engine.addSystem(new MovementSystem(eventBus));
        engine.addSystem(new CameraSystem());
        engine.addSystem(new CropGrowthSystem(eventBus));
        engine.addSystem(new HarvestingSystem(eventBus));
        engine.addSystem(new RenderSystem(batch));
        engine.addSystem(new BuildGridRenderSystem(cameraComponent.camera));
        engine.addSystem(new HungerSystem(eventBus));
        engine.addSystem(new SpriteSystem(assetManager));
        engine.addSystem(new HungerRenderSystem(batch));
        engine.addSystem(new DurabilityRenderSystem(engine, batch, eventBus));
        engine.addSystem(new SyncMarkingSystem(eventBus));
        engine.addSystem(new FirebaseSyncSystem(firebaseRepository));
engine.addSystem(new ClickedCleanupSystem());
        engine.addSystem(new EventCleanupSystem(eventBus));

        TiledMapComponent tiledMap = Mappers.tiledMap.get(map);
        MapUtils.loadEnvCollidables(tiledMap.tiledMap, Config.UNIT_SCALE, engine);
        MapUtils.loadPlacementBlockers(tiledMap.tiledMap, Config.UNIT_SCALE, WorldLayer.TERRAIN, engine);
    }

    public Engine getEngine() {
        return engine;
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public Entity getCamera() {
        return camera;
    }

    public void dispose() {
        batch.dispose();
    }
}

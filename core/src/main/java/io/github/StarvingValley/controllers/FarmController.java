package io.github.StarvingValley.controllers;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.github.StarvingValley.config.Config;
import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.Interfaces.AuthCallback;
import io.github.StarvingValley.models.Interfaces.IFirebaseRepository;
import io.github.StarvingValley.models.components.CameraComponent;
import io.github.StarvingValley.models.components.TiledMapComponent;
import io.github.StarvingValley.models.entities.CameraFactory;
import io.github.StarvingValley.models.entities.HUDButtonFactory;
import io.github.StarvingValley.models.entities.MapFactory;
import io.github.StarvingValley.models.entities.PlayerFactory;
import io.github.StarvingValley.models.events.EventBus;
import io.github.StarvingValley.models.systems.ActionAnimationSystem;
import io.github.StarvingValley.models.systems.AlphaPulseSystem;
import io.github.StarvingValley.models.systems.AnimationSystem;
import io.github.StarvingValley.models.systems.BuildGridRenderSystem;
import io.github.StarvingValley.models.systems.BuildPlacementSystem;
import io.github.StarvingValley.models.systems.BuildPreviewSystem;
import io.github.StarvingValley.models.systems.CameraSystem;
import io.github.StarvingValley.models.systems.ClickSystem;
import io.github.StarvingValley.models.systems.ClickedCleanupSystem;
import io.github.StarvingValley.models.systems.CropGrowthSystem;
import io.github.StarvingValley.models.systems.DurabilityRenderSystem;
import io.github.StarvingValley.models.systems.EnvironmentCollisionSystem;
import io.github.StarvingValley.models.systems.EventCleanupSystem;
import io.github.StarvingValley.models.systems.FirebaseSyncSystem;
import io.github.StarvingValley.models.systems.HUDButtonPressHandlingSystem;
import io.github.StarvingValley.models.systems.HUDButtonPressSystem;
import io.github.StarvingValley.models.systems.HUDRenderSystem;
import io.github.StarvingValley.models.systems.HarvestingSystem;
import io.github.StarvingValley.models.systems.HungerRenderSystem;
import io.github.StarvingValley.models.systems.HungerSystem;
import io.github.StarvingValley.models.systems.MapRenderSystem;
import io.github.StarvingValley.models.systems.MovementSystem;
import io.github.StarvingValley.models.systems.RenderSystem;
import io.github.StarvingValley.models.systems.SpriteSystem;
import io.github.StarvingValley.models.systems.SyncMarkingSystem;
import io.github.StarvingValley.models.systems.TradingSystem;
import io.github.StarvingValley.models.systems.VelocitySystem;
import io.github.StarvingValley.models.types.GameContext;
import io.github.StarvingValley.models.types.WorldLayer;
import io.github.StarvingValley.utils.MapUtils;

public class FarmController {

    private final Engine engine;
    private final SpriteBatch batch;
    private final EventBus eventBus;
    private final AssetManager assetManager;
    private final IFirebaseRepository firebaseRepository;
    public GameContext gameContext;

    private Entity camera;
    private Entity map;
    private Entity player;

    public FarmController(IFirebaseRepository firebaseRepository, EventBus eventBus, AssetManager assetManager) {
        this.firebaseRepository = firebaseRepository;
        this.eventBus = eventBus;
        this.assetManager = assetManager;
        this.engine = new Engine();
        this.batch = new SpriteBatch();
        gameContext = new GameContext();
        gameContext.spriteBatch = this.batch;
        gameContext.eventBus = this.eventBus;
        gameContext.assetManager = this.assetManager;
        gameContext.firebaseRepository = this.firebaseRepository;
        gameContext.engine = this.engine;
        initGame();
    }

    private void initGame() {
        int tilesWide = Config.CAMERA_TILES_WIDE;
        float tilesHigh = MapUtils.calculateVerticalTileCount(tilesWide);

        camera = CameraFactory.createCamera(tilesWide, tilesHigh);
        CameraComponent cameraComponent = Mappers.camera.get(camera);

        gameContext.camera = cameraComponent.camera;

        map = MapFactory.createMap("FarmMap.tmx", Config.UNIT_SCALE, cameraComponent);

        engine.addEntity(camera);
        engine.addEntity(map);

        engine.addSystem(new ClickSystem(gameContext));
        engine.addSystem(new MapRenderSystem());
        engine.addSystem(new BuildPreviewSystem(gameContext));
        engine.addSystem(new BuildPlacementSystem(gameContext));
        engine.addSystem(new AlphaPulseSystem());
        engine.addSystem(new VelocitySystem());
        engine.addSystem(new AnimationSystem(gameContext));
        engine.addSystem(new EnvironmentCollisionSystem());
        engine.addSystem(new MovementSystem(gameContext));
        engine.addSystem(new CameraSystem());
        engine.addSystem(new CropGrowthSystem(gameContext));
        engine.addSystem(new HarvestingSystem(gameContext));
        engine.addSystem(new TradingSystem(gameContext));
        engine.addSystem(new RenderSystem(gameContext));
        engine.addSystem(new BuildGridRenderSystem(gameContext));
        engine.addSystem(new HungerSystem(gameContext));
        engine.addSystem(new SpriteSystem(gameContext));
        engine.addSystem(new HungerRenderSystem(gameContext));
        engine.addSystem(new DurabilityRenderSystem(gameContext));
        engine.addSystem(new HUDRenderSystem());
        engine.addSystem(new HUDButtonPressSystem(gameContext));
        engine.addSystem(new HUDButtonPressHandlingSystem(gameContext));
        engine.addSystem(new SyncMarkingSystem(gameContext));
        engine.addSystem(new FirebaseSyncSystem(gameContext));
        engine.addSystem(new ClickedCleanupSystem());
        engine.addSystem(new EventCleanupSystem(gameContext));
        engine.addSystem(new ActionAnimationSystem(gameContext));

        // engine.addEntity();

        // player = PlayerFactory.createPlayer(10, 10, 1f, 1f, 3.5f, assetManager,
        // camera);
        // engine.addEntity(player);

        TiledMapComponent tiledMap = Mappers.tiledMap.get(map);
        MapUtils.loadEnvCollidables(tiledMap.tiledMap, Config.UNIT_SCALE, engine);
        MapUtils.loadPlacementBlockers(tiledMap.tiledMap, Config.UNIT_SCALE, WorldLayer.TERRAIN, engine);

        firebaseRepository.registerOrSignInWithDeviceId(
                new AuthCallback() {
                    @Override
                    public void onSuccess() {
                        MapUtils.loadSyncedEntities(gameContext, getCamera());
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        // TODO: Fail gracefully
                        throw new RuntimeException("Authentication failed: " + errorMessage);
                    }
                });
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

    public Entity getPlayer() {
        return player;
    }

    public void dispose() {
        batch.dispose();
    }
}

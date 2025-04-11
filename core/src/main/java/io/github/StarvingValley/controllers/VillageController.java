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
import io.github.StarvingValley.models.systems.ActionAnimationSystem;
import io.github.StarvingValley.models.systems.AnimationSystem;
import io.github.StarvingValley.models.systems.CameraSystem;
import io.github.StarvingValley.models.systems.ClickSystem;
import io.github.StarvingValley.models.systems.ClickedCleanupSystem;
import io.github.StarvingValley.models.systems.EnvironmentCollisionSystem;
import io.github.StarvingValley.models.systems.EventCleanupSystem;
import io.github.StarvingValley.models.systems.FirebaseSyncSystem;
import io.github.StarvingValley.models.systems.HungerRenderSystem;
import io.github.StarvingValley.models.systems.HungerSystem;
import io.github.StarvingValley.models.systems.MapRenderSystem;
import io.github.StarvingValley.models.systems.MovementSystem;
import io.github.StarvingValley.models.systems.RenderSystem;
import io.github.StarvingValley.models.systems.SpriteSystem;
import io.github.StarvingValley.models.systems.SyncMarkingSystem;
import io.github.StarvingValley.models.systems.VelocitySystem;
import io.github.StarvingValley.models.types.GameContext;
import io.github.StarvingValley.models.types.WorldLayer;
import io.github.StarvingValley.utils.MapUtils;

public class VillageController {

    private final Engine engine;
    private final SpriteBatch batch;
    private final EventBus eventBus;
    private final AssetManager assetManager;
    private final IFirebaseRepository firebaseRepository;

    public GameContext gameContext;

    private Entity camera;
    private Entity map;

    public VillageController(IFirebaseRepository firebaseRepository, EventBus eventBus, AssetManager assetManager) {
        this.firebaseRepository = firebaseRepository;
        this.eventBus = eventBus;
        this.assetManager = assetManager;
        this.engine = new Engine();
        this.batch = new SpriteBatch();
        this.gameContext = new GameContext();
        this.gameContext.spriteBatch = this.batch;
        this.gameContext.eventBus = this.eventBus;
        this.gameContext.assetManager = this.assetManager;
        this.gameContext.firebaseRepository = this.firebaseRepository;
        this.gameContext.engine = this.engine;
        initGame();
    }

    private void initGame() {
        int tilesWide = Config.CAMERA_TILES_WIDE;
        float tilesHigh = MapUtils.calculateVerticalTileCount(tilesWide);

        camera = CameraFactory.createCamera(tilesWide, tilesHigh);
        CameraComponent cameraComponent = Mappers.camera.get(camera);

        gameContext.camera = cameraComponent.camera;

        map = MapFactory.createMap("TownMap.tmx", Config.UNIT_SCALE, cameraComponent);

        engine.addEntity(camera);
        engine.addEntity(map);

        engine.addSystem(new ClickSystem(gameContext));
        engine.addSystem(new MapRenderSystem());
        engine.addSystem(new VelocitySystem());
        engine.addSystem(new AnimationSystem(gameContext));
        engine.addSystem(new EnvironmentCollisionSystem());
        engine.addSystem(new MovementSystem(gameContext));
        engine.addSystem(new CameraSystem());
        engine.addSystem(new RenderSystem(gameContext));
        engine.addSystem(new HungerSystem(gameContext));
        engine.addSystem(new SpriteSystem(gameContext));
        engine.addSystem(new HungerRenderSystem(gameContext));
        engine.addSystem(new SyncMarkingSystem(gameContext));
        engine.addSystem(new FirebaseSyncSystem(gameContext));
        engine.addSystem(new ClickedCleanupSystem());
        engine.addSystem(new EventCleanupSystem(gameContext));

        TiledMapComponent tiledMap = Mappers.tiledMap.get(map);
        MapUtils.loadEnvCollidables(tiledMap.tiledMap, Config.UNIT_SCALE, engine);
        MapUtils.loadPlacementBlockers(tiledMap.tiledMap, Config.UNIT_SCALE, WorldLayer.TERRAIN, engine);

        MapUtils.loadSyncedEntities(gameContext, getCamera());
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

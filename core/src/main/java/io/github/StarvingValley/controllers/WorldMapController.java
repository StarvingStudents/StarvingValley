package io.github.StarvingValley.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.github.StarvingValley.config.Config;
import io.github.StarvingValley.models.Interfaces.AuthCallback;
import io.github.StarvingValley.models.Interfaces.IFirebaseRepository;
import io.github.StarvingValley.models.Interfaces.UserIdsCallback;
import io.github.StarvingValley.models.entities.CameraFactory;
import io.github.StarvingValley.models.entities.WorldMapUserFactory;
import io.github.StarvingValley.models.events.EventBus;
import io.github.StarvingValley.models.events.WorldMapFarmClickEvent;
import io.github.StarvingValley.models.systems.CameraSystem;
import io.github.StarvingValley.models.systems.ClickedCleanupSystem;
import io.github.StarvingValley.models.systems.ClickSystem;
import io.github.StarvingValley.models.systems.EventCleanupSystem;
import io.github.StarvingValley.models.systems.FirebaseSyncSystem;
import io.github.StarvingValley.models.systems.RenderSystem;
import io.github.StarvingValley.models.systems.SpriteSystem;
import io.github.StarvingValley.models.systems.SyncMarkingSystem;
import io.github.StarvingValley.models.systems.WorldMapTransitionSystem;
import io.github.StarvingValley.utils.MapUtils;

public class WorldMapController {

    private final Engine engine;
    private final SpriteBatch batch;
    private final EventBus eventBus;
    private final AssetManager assetManager;
    private final IFirebaseRepository firebaseRepository;

    private Entity camera;

    public WorldMapController(IFirebaseRepository firebaseRepository, EventBus eventBus, AssetManager assetManager) {
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

        engine.addEntity(camera);

        // TODO: Since there's some stuff we send to multiple systems (eventBus, camera,
        // batch etc), maybe we should have a GameContext class that holds them so we
        // just pass around that?
        engine.addSystem(new ClickSystem(eventBus));
        engine.addSystem(new WorldMapTransitionSystem(eventBus));
        engine.addSystem(new CameraSystem());
        engine.addSystem(new RenderSystem(batch));
        engine.addSystem(new SpriteSystem(assetManager));
        engine.addSystem(new SyncMarkingSystem(eventBus));
        engine.addSystem(new FirebaseSyncSystem(firebaseRepository));
        engine.addSystem(new ClickedCleanupSystem());
        engine.addSystem(new EventCleanupSystem(eventBus));

        // loadUserEntities();
    }

    public void update(float deltaTime) {

        List<WorldMapFarmClickEvent> events = eventBus.getEvents(WorldMapFarmClickEvent.class);
        if (events.size() > 0) {
            System.out.println(events.get(0).userId);
        }
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

    public void loadUserEntities(IFirebaseRepository firebaseRepository) {
        firebaseRepository.registerOrSignInWithDeviceId(
                new AuthCallback() {
                    @Override
                    public void onSuccess() {
                        firebaseRepository.getAllUserIds(
                                new UserIdsCallback() {
                                    @Override
                                    public void onSuccess(List<String> data) {
                                        MapUtils.loadWorldMapFarmEntities(data, engine);
                                    }

                                    @Override
                                    public void onFailure(String errorMessage) {
                                        System.err.println("Failed to load entities: " + errorMessage);
                                    }
                                });
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        // TODO: Fail gracefully
                        throw new RuntimeException("Authentication failed: " + errorMessage);
                    }
                });
    }

}

package io.github.StarvingValley.controllers;

import java.util.List;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.github.StarvingValley.config.Config;
import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.CameraComponent;
import io.github.StarvingValley.models.components.PlayerComponent;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.components.TiledMapComponent;
import io.github.StarvingValley.models.entities.CameraFactory;
import io.github.StarvingValley.models.entities.HudFactory;
import io.github.StarvingValley.models.entities.MapFactory;
import io.github.StarvingValley.models.entities.TraderFactory;
import io.github.StarvingValley.models.events.EventBus;
import io.github.StarvingValley.models.events.ScreenTransitionEvent;
import io.github.StarvingValley.models.interfaces.PlayerDataRepository;
import io.github.StarvingValley.models.systems.AnimationSystem;
import io.github.StarvingValley.models.systems.CameraSystem;
import io.github.StarvingValley.models.systems.EnvironmentCollisionSystem;
import io.github.StarvingValley.models.systems.EventCleanupSystem;
import io.github.StarvingValley.models.systems.FarmToVillageTransitionSystem;
import io.github.StarvingValley.models.systems.FirebaseSyncSystem;
import io.github.StarvingValley.models.systems.HudRenderSystem;
import io.github.StarvingValley.models.systems.InputCleanupSystem;
import io.github.StarvingValley.models.systems.InputSystem;
import io.github.StarvingValley.models.systems.InventoryDragSystem;
import io.github.StarvingValley.models.systems.InventoryOpenSystem;
import io.github.StarvingValley.models.systems.InventorySystem;
import io.github.StarvingValley.models.systems.MapRenderSystem;
import io.github.StarvingValley.models.systems.MovementSystem;
import io.github.StarvingValley.models.systems.RenderSystem;
import io.github.StarvingValley.models.systems.SpriteSystem;
import io.github.StarvingValley.models.systems.SyncMarkingSystem;
import io.github.StarvingValley.models.systems.TraderClickSystem;
import io.github.StarvingValley.models.systems.TradingSystem;
import io.github.StarvingValley.models.systems.VelocitySystem;
import io.github.StarvingValley.models.types.GameContext;
import io.github.StarvingValley.models.types.ItemTrade;
import io.github.StarvingValley.models.types.PrefabType;
import io.github.StarvingValley.models.types.ScreenType;
import io.github.StarvingValley.models.types.WorldLayer;
import io.github.StarvingValley.utils.AnimationUtils;
import io.github.StarvingValley.utils.Assets;
import io.github.StarvingValley.utils.MapUtils;

public class VillageController {

    private final Engine engine;
    private final SpriteBatch batch;
    private final EventBus eventBus;

    public GameContext gameContext;

    private Entity camera;
    private Entity map;

    private StarvingValley game;

    public VillageController(StarvingValley game, PlayerDataRepository firebaseRepository, EventBus eventBus,
                    AssetManager assetManager) {
        this.game = game;
        this.eventBus = eventBus;
        this.engine = new Engine();
        this.batch = new SpriteBatch();

        this.gameContext = new GameContext(eventBus, batch, assetManager, firebaseRepository, engine,
                new Assets(assetManager));
                
        AnimationUtils.loadTexturesForAnimation(assetManager);
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

        engine.addSystem(new InputSystem(gameContext));
        engine.addSystem(new TradingSystem(gameContext));
        engine.addSystem(new InventoryOpenSystem(gameContext));
        engine.addSystem(new InventoryDragSystem(gameContext));
        engine.addSystem(new MapRenderSystem());
        engine.addSystem(new InventorySystem(gameContext));
        engine.addSystem(new VelocitySystem());
        engine.addSystem(new AnimationSystem());
        engine.addSystem(new EnvironmentCollisionSystem());
        engine.addSystem(new MovementSystem(gameContext));
        engine.addSystem(new CameraSystem());
        engine.addSystem(new TraderClickSystem(gameContext));
        engine.addSystem(new RenderSystem(gameContext));
        engine.addSystem(new SpriteSystem(gameContext));
        engine.addSystem(new HudRenderSystem());
        engine.addSystem(new SyncMarkingSystem(gameContext));
        engine.addSystem(new FirebaseSyncSystem(gameContext));
        engine.addSystem(new InputCleanupSystem());
        engine.addSystem(new EventCleanupSystem(gameContext));
        engine.addSystem(new FarmToVillageTransitionSystem(gameContext));

        addTownTraders();

        TiledMapComponent tiledMap = Mappers.tiledMap.get(map);
        MapUtils.loadEnvCollidables(tiledMap.tiledMap, Config.UNIT_SCALE, engine);
        MapUtils.loadPlacementBlockers(tiledMap.tiledMap, Config.UNIT_SCALE, WorldLayer.TERRAIN, engine);

        MapUtils.loadSyncedVillageEntities(gameContext, getCamera());

        engine.addEntity(HudFactory.createEconomyBar(gameContext));
    }

    public void update(float deltaTime) {
        List<ScreenTransitionEvent> events = eventBus.getEvents(ScreenTransitionEvent.class);
        if (events.isEmpty())
            return;

        if (events.get(0).getTargetScreen() == ScreenType.FARM) {

            ImmutableArray<Entity> players = engine.getEntitiesFor(
                    Family.all(PlayerComponent.class, PositionComponent.class).get() // currentMapComponent
            );
            if (players.size() == 0) {
                return;
            }

            game.requestViewSwitch(ScreenType.FARM);
        }
    }

    private void addTownTraders() {

        TraderFactory.addTraderToEngine(engine, eventBus, 15, 10, List.of(new ItemTrade(PrefabType.SOIL, 4),
                new ItemTrade(PrefabType.WHEAT_SEEDS, 15), new ItemTrade(PrefabType.BEETROOT_SEEDS, 20)),
                "DogBasic.png");

        TraderFactory.addTraderToEngine(engine, eventBus, 32, 24, List.of(new ItemTrade(PrefabType.WALL, 20)),
                "Bear.png");

        TraderFactory.addTraderToEngine(engine, eventBus, 9, 12, List.of(new ItemTrade(PrefabType.SOIL, 3)),
                "PinkCharacter.png");

        TraderFactory.addTraderToEngine(engine, eventBus, 14, 19, List.of(new ItemTrade(PrefabType.WHEAT_SEEDS, 10),
                new ItemTrade(PrefabType.BEETROOT_SEEDS, 22)), "FoxBasic.png");

        TraderFactory.addTraderToEngine(engine, eventBus, 20, 14,
                List.of(new ItemTrade(PrefabType.WALL, 22), new ItemTrade(PrefabType.SOIL, 2)), "OrangeCharacter.png");

    }

    public Entity getPlayer() {
        return gameContext.player;
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

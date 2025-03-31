package io.github.StarvingValley.views;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import io.github.StarvingValley.config.Config;
import io.github.StarvingValley.controllers.JoystickController;
import io.github.StarvingValley.input.TapInputAdapter;
import io.github.StarvingValley.models.Interfaces.IBuildableEntityFactory;
import io.github.StarvingValley.models.Interfaces.IFirebaseRepository;
import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.BuildPreviewComponent;
import io.github.StarvingValley.models.components.CameraComponent;
import io.github.StarvingValley.models.components.CameraFollowComponent;
import io.github.StarvingValley.models.components.CropTypeComponent;
import io.github.StarvingValley.models.components.PlaceRequestComponent;
import io.github.StarvingValley.models.components.TiledMapComponent;
import io.github.StarvingValley.models.dto.WorldObjectConfig;
import io.github.StarvingValley.models.entities.CameraFactory;
import io.github.StarvingValley.models.entities.CropFactory;
import io.github.StarvingValley.models.entities.MapFactory;
import io.github.StarvingValley.models.entities.PlayerFactory;
import io.github.StarvingValley.models.entities.WorldObjectFactory;
import io.github.StarvingValley.models.systems.AlphaPulseSystem;
import io.github.StarvingValley.models.systems.BuildGridRenderSystem;
import io.github.StarvingValley.models.systems.BuildPlacementSystem;
import io.github.StarvingValley.models.systems.BuildPreviewSystem;
import io.github.StarvingValley.models.systems.CameraSystem;
import io.github.StarvingValley.models.systems.CropGrowthSystem;
import io.github.StarvingValley.models.systems.DurabilityRenderSystem;
import io.github.StarvingValley.models.systems.EnvironmentCollisionSystem;
import io.github.StarvingValley.models.systems.HarvestingSystem;
import io.github.StarvingValley.models.systems.HungerRenderSystem;
import io.github.StarvingValley.models.systems.HungerSystem;
import io.github.StarvingValley.models.systems.MapRenderSystem;
import io.github.StarvingValley.models.systems.MovementSystem;
import io.github.StarvingValley.models.systems.RenderSystem;
import io.github.StarvingValley.models.systems.TileOverlapSystem;
import io.github.StarvingValley.models.systems.VelocitySystem;
import io.github.StarvingValley.models.types.WorldLayer;
import io.github.StarvingValley.utils.BuildUtils;
import io.github.StarvingValley.utils.MapUtils;

//TODO: Maybe move logic to a controller and rename to FarmScreen/FarmView
public class GameScreen extends ScreenAdapter {
  IFirebaseRepository _firebaseRepository;
  private Engine engine;
  private SpriteBatch batch;
  private Entity player;
  private Entity camera;
  private Entity map;
  private Entity tomato;
  private Entity tomato2;
  private Entity potato;

  private JoystickOverlay joystickOverlay;
  private InputAdapter inputAdapter;
  private InputAdapter tapInputAdapter;

  public GameScreen(IFirebaseRepository firebaseRepository) {
    _firebaseRepository = firebaseRepository;

    // TODO: Temp logic. When inventory is implemented it should handle this, and it
    // should only be possible on entities
    // with BuildableComponent. Use BuildUtils.isBuildable
    inputAdapter = new InputAdapter() {
      Texture dogTexture = new Texture("DogBasic.png");

      @Override
      public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.C) {
          BuildUtils.toggleBuildPreview(
              dogTexture,
              engine,
              new IBuildableEntityFactory() {
                @Override
                public Entity createAt(GridPoint2 tile) {
                  WorldObjectConfig config = new WorldObjectConfig();
                  config.blocksMovement = true;
                  config.blocksPlacement = true;
                  config.worldLayer = WorldLayer.CROP;
                  config.texture = dogTexture;

                  return WorldObjectFactory.createWorldObject(
                      new Rectangle(tile.x, tile.y, 1, 1),
                      config);
                }

                @Override
                public WorldLayer getWorldLayer() {
                  return WorldLayer.CROP;
                }
              });
        }
        return true;
      }
    };

    tapInputAdapter = new TapInputAdapter(() -> {
      ImmutableArray<Entity> previews = engine.getEntitiesFor(
          Family.all(BuildPreviewComponent.class).get());

      for (Entity preview : previews) {
        if (!Mappers.placeRequest.has(preview))
          preview.add(new PlaceRequestComponent());
      }
    });
  }

  @Override
  public void show() {
    batch = new SpriteBatch();

    int tilesWide = Config.CAMERA_TILES_WIDE;
    float tilesHigh = MapUtils.calculateVerticalTileCount(tilesWide);

    camera = CameraFactory.createCamera(tilesWide, tilesHigh);

    CameraFollowComponent cameraFollowComponent = new CameraFollowComponent();
    cameraFollowComponent.targetCamera = camera;

    CameraComponent cameraComponent = Mappers.camera.get(camera);

    map = MapFactory.CreateMap("FarmMap.tmx", Config.UNIT_SCALE, cameraComponent);

    engine = new Engine();

    player = PlayerFactory.createPlayer(35, 15, 1, 1, 5f, "DogBasic.png");
    player.add(cameraFollowComponent);
    tomato = CropFactory.createCrop(33, 17, CropTypeComponent.CropType.TOMATO);
    tomato2 = CropFactory.createCrop(33, 16, CropTypeComponent.CropType.TOMATO);
    potato = CropFactory.createCrop(33, 15, CropTypeComponent.CropType.POTATO);

    engine.addEntity(player);
    engine.addEntity(camera);
    engine.addEntity(map);
    engine.addEntity(tomato);
    engine.addEntity(tomato2);
    engine.addEntity(potato);
    engine.addSystem(new MapRenderSystem());
    engine.addSystem(new TileOverlapSystem());
    engine.addSystem(new BuildPreviewSystem(cameraComponent.camera));
    engine.addSystem(new BuildPlacementSystem());
    engine.addSystem(new AlphaPulseSystem());
    engine.addSystem(new VelocitySystem());
    engine.addSystem(new EnvironmentCollisionSystem());
    engine.addSystem(new MovementSystem());
    engine.addSystem(new CameraSystem());
    engine.addSystem(new RenderSystem(batch));
    engine.addSystem(new BuildGridRenderSystem(cameraComponent.camera));
    engine.addSystem(new DurabilityRenderSystem(engine, batch));
    engine.addSystem(new HungerSystem());
    engine.addSystem(new HungerRenderSystem(engine, batch));
    engine.addSystem(new CropGrowthSystem());
    engine.addSystem(new HarvestingSystem(player));

    JoystickController joystickController = new JoystickController();
    joystickOverlay = new JoystickOverlay(joystickController);

    TiledMapComponent tiledMap = Mappers.tiledMap.get(map);

    MapUtils.loadCollidables(tiledMap.tiledMap, Config.UNIT_SCALE, engine);
    MapUtils.loadPlacementBlockers(tiledMap.tiledMap, Config.UNIT_SCALE, WorldLayer.TERRAIN, engine);

    InputAdapter joystickInputAdapter = joystickOverlay.getInputAdapter();

    InputMultiplexer multiplexer = new InputMultiplexer();
    multiplexer.addProcessor(tapInputAdapter);
    multiplexer.addProcessor(inputAdapter);
    multiplexer.addProcessor(joystickInputAdapter);

    Gdx.input.setInputProcessor(multiplexer);
  }

  @Override
  public void render(float delta) {
    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    Gdx.gl.glEnable(GL20.GL_BLEND);
    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

    CameraComponent cameraComponent = Mappers.camera.get(camera);
    if (cameraComponent != null) {
      batch.setProjectionMatrix(cameraComponent.camera.combined);
    }

    engine.update(delta);

    joystickOverlay.render();
  }

  @Override
  public void dispose() {
    batch.dispose();
  }
}

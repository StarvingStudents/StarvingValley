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
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;

import io.github.StarvingValley.config.Config;
import io.github.StarvingValley.controllers.JoystickController;
import io.github.StarvingValley.controllers.WorldMapController;
import io.github.StarvingValley.input.TapInputAdapter;
import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.Interfaces.AuthCallback;
import io.github.StarvingValley.models.Interfaces.IBuildableEntityFactory;
import io.github.StarvingValley.models.Interfaces.IFirebaseRepository;
import io.github.StarvingValley.models.components.BuildPreviewComponent;
import io.github.StarvingValley.models.components.CameraComponent;
import io.github.StarvingValley.models.components.CropTypeComponent;
import io.github.StarvingValley.models.components.EnvironmentCollidableComponent;
import io.github.StarvingValley.models.components.PlaceRequestComponent;
import io.github.StarvingValley.models.components.SpriteComponent;
import io.github.StarvingValley.models.components.TiledMapComponent;
import io.github.StarvingValley.models.components.WorldMapFarmSelectionComponent;
import io.github.StarvingValley.models.entities.CameraFactory;
import io.github.StarvingValley.models.entities.CropFactory;
import io.github.StarvingValley.models.entities.MapFactory;
import io.github.StarvingValley.models.systems.AlphaPulseSystem;
import io.github.StarvingValley.models.systems.BuildGridRenderSystem;
import io.github.StarvingValley.models.systems.BuildPlacementSystem;
import io.github.StarvingValley.models.systems.BuildPreviewSystem;
import io.github.StarvingValley.models.systems.CameraSystem;
import io.github.StarvingValley.models.systems.CropGrowthSystem;
import io.github.StarvingValley.models.systems.DurabilityRenderSystem;
import io.github.StarvingValley.models.systems.EnvironmentCollisionSystem;
import io.github.StarvingValley.models.systems.FirebaseSyncSystem;
import io.github.StarvingValley.models.systems.HarvestingSystem;
import io.github.StarvingValley.models.systems.HungerRenderSystem;
import io.github.StarvingValley.models.systems.HungerSystem;
import io.github.StarvingValley.models.systems.MapRenderSystem;
import io.github.StarvingValley.models.systems.MovementSystem;
import io.github.StarvingValley.models.systems.RenderSystem;
import io.github.StarvingValley.models.systems.SpriteSystem;
import io.github.StarvingValley.models.systems.TileOverlapSystem;
import io.github.StarvingValley.models.systems.VelocitySystem;
import io.github.StarvingValley.models.types.WorldLayer;
import io.github.StarvingValley.utils.BuildUtils;
import io.github.StarvingValley.utils.MapUtils;
import io.github.StarvingValley.models.entities.WorldMapFarmFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.awt.Point;


// TODO: Maybe move logic to a controller and rename to FarmScreen/FarmView
public class WorldMapScreen extends ScreenAdapter {

  private final ShapeRenderer shapeRenderer = new ShapeRenderer();

  public AssetManager assetManager;
  IFirebaseRepository _firebaseRepository;
  private Engine engine;
  private SpriteBatch batch;
  private Entity camera;
//   private Entity map;
  private JoystickOverlay joystickOverlay;
  private InputAdapter inputAdapter;
//   private InputAdapter tapInputAdapter;
  private WorldMapFarmSelectionComponent farmSelection;

  public WorldMapScreen(IFirebaseRepository firebaseRepository) {
    _firebaseRepository = firebaseRepository;

    // TODO: Here we can pre-load some assets that we know we always need.
    // Potentially add assetManager.finishLoading(); to wait
    assetManager = new AssetManager();
    // assetManager.load("DogBasic.png", Texture.class);
    // assetManager.load("tomato1.png", Texture.class);
    // assetManager.load("potato1.png", Texture.class);

    // TODO: Temp logic. When inventory is implemented it should handle this, and it
    // should only be possible on entities
    // with BuildableComponent. Use BuildUtils.isBuildable
    inputAdapter = new InputAdapter() {
      @Override
      public boolean keyDown(int keycode) {
        return true; 
        // if (keycode == Input.Keys.C) {
        //   BuildUtils.toggleBuildPreview(
        //       "DogBasic.png",
        //       engine,
        //       new IBuildableEntityFactory() {
        //         @Override
        //         public Entity createAt(GridPoint2 tile) {
        //           Entity entity = MapFactory.createEnvPlacementBlocker(tile.x, tile.y, 1, 1);
        //           entity.add(new SpriteComponent("DogBasic.png"));
        //           entity.add(new EnvironmentCollidableComponent());

        //           return entity;
        //         }

        //         @Override
        //         public WorldLayer getWorldLayer() {
        //           return WorldLayer.CROP;
        //         }
        //       });
        // } else if (keycode == Input.Keys.D) {
        //   BuildUtils.toggleBuildPreview(
        //       "tomato1.png",
        //       engine,
        //       new IBuildableEntityFactory() {
        //         @Override
        //         public Entity createAt(GridPoint2 tile) {
        //           return CropFactory.createCrop(
        //               tile.x, tile.y, CropTypeComponent.CropType.TOMATO);
        //         }

        //         @Override
        //         public WorldLayer getWorldLayer() {
        //           return WorldLayer.CROP;
        //         }
        //       });
        // } else if (keycode == Input.Keys.E) {
        //   BuildUtils.toggleBuildPreview(
        //       "potato1.png",
        //       engine,
        //       new IBuildableEntityFactory() {
        //         @Override
        //         public Entity createAt(GridPoint2 tile) {
        //           return CropFactory.createCrop(
        //               tile.x, tile.y, CropTypeComponent.CropType.POTATO);
        //         }

        //         @Override
        //         public WorldLayer getWorldLayer() {
        //           return WorldLayer.CROP;
        //         }
        //       });
        // }
        // return true;
      }
    };

    WorldMapController controller = new WorldMapController(_firebaseRepository);
    this.farmSelection = WorldMapFarmSelectionComponent.getInstance(
      controller.randomUserIds(Config.ATTACKABLE_FARMS)
      // controller.randomUserIds(1)
      );

    // tapInputAdapter = new TapInputAdapter(
    //     () -> {
    //       ImmutableArray<Entity> previews = engine.getEntitiesFor(Family.all(BuildPreviewComponent.class).get());

    //       for (Entity preview : previews) {
    //         if (!Mappers.placeRequest.has(preview))
    //           preview.add(new PlaceRequestComponent());
    //       }
    //     });
  }

  @Override
  public void show() {
    batch = new SpriteBatch();

    int tilesWide = Config.CAMERA_TILES_WIDE;
    float tilesHigh = MapUtils.calculateVerticalTileCount(tilesWide);

    camera = CameraFactory.createCamera(tilesWide, tilesHigh);

    // CameraComponent cameraComponent = Mappers.camera.get(camera);

    // map = MapFactory.createMap("FarmMap.tmx", Config.UNIT_SCALE, cameraComponent);

    engine = new Engine();

    engine.addEntity(camera);
    // engine.addEntity(map);
    // engine.addSystem(new MapRenderSystem());
    // engine.addSystem(new TileOverlapSystem());
    // engine.addSystem(new BuildPreviewSystem(cameraComponent.camera));
    // engine.addSystem(new BuildPlacementSystem());
    // engine.addSystem(new AlphaPulseSystem());
    // engine.addSystem(new VelocitySystem());
    // engine.addSystem(new EnvironmentCollisionSystem());
    engine.addSystem(new MovementSystem());
    engine.addSystem(new CameraSystem());
    // engine.addSystem(new CropGrowthSystem());
    // engine.addSystem(new HarvestingSystem());
    engine.addSystem(new RenderSystem(batch));
    // engine.addSystem(new BuildGridRenderSystem(cameraComponent.camera));
    // engine.addSystem(new HungerSystem());
    engine.addSystem(new SpriteSystem(assetManager));
    engine.addSystem(new RenderSystem(batch));
    // engine.addSystem(new HungerRenderSystem(batch));
    // engine.addSystem(new DurabilityRenderSystem(engine, batch));
    engine.addSystem(new FirebaseSyncSystem(_firebaseRepository));

    JoystickController joystickController = new JoystickController(engine);
    joystickOverlay = new JoystickOverlay(joystickController);

    // TiledMapComponent tiledMap = Mappers.tiledMap.get(map);

    // MapUtils.loadEnvCollidables(tiledMap.tiledMap, Config.UNIT_SCALE, engine);
    // MapUtils.loadPlacementBlockers(tiledMap.tiledMap, Config.UNIT_SCALE, WorldLayer.TERRAIN, engine);

    InputAdapter joystickInputAdapter = joystickOverlay.getInputAdapter();

    InputMultiplexer multiplexer = new InputMultiplexer();
    // multiplexer.addProcessor(tapInputAdapter);
    multiplexer.addProcessor(inputAdapter);
    multiplexer.addProcessor(joystickInputAdapter);

    Gdx.input.setInputProcessor(multiplexer);

    _firebaseRepository.registerOrSignInWithDeviceId(new AuthCallback() {
      @Override
      public void onSuccess() {
        MapUtils.loadSyncedEntities(_firebaseRepository, engine, camera);
      }

      @Override
      public void onFailure(String errorMessage) {
        // TODO: Fail gracefully
        throw new RuntimeException("Authentication failed: " + errorMessage);
      }
    });
  }

  @Override
  public void render(float delta) {
    assetManager.update();

    Gdx.gl.glClearColor(75.3f/100f, 83.1f/100f, 43.9f/100f, 0);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    Gdx.gl.glEnable(GL20.GL_BLEND);
    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

    CameraComponent cameraComponent = Mappers.camera.get(camera);
    if (cameraComponent != null) {
      batch.setProjectionMatrix(cameraComponent.camera.combined);
    }

    Vector2 camCenter = new Vector2(cameraComponent.camera.position.x, cameraComponent.camera.position.y);
    float viewH = cameraComponent.camera.viewportHeight;

    Vector2[][] gridCenters = computeGridSquareCenters(camCenter, viewH);

    // Use gridCenters as needed, e.g., for rendering markers at each center
    // for (int row = 0; row < gridCenters.length; row++) {
    //     for (int col = 0; col < gridCenters[row].length; col++) {
    //         Vector2 center = gridCenters[row][col];
    //         // render something at center.x, center.y
    //     }
    // }

    // // Render selected farms on grid squares
    // final int rows = Config.WORLD_MAP_GRID_HEIGHT;
    // final int cols = Config.WORLD_MAP_GRID_WIDTH;


    // Vector2 camCenter = new Vector2(cameraComponent.camera.position.x, cameraComponent.camera.position.y);
    // float viewW = cameraComponent.camera.viewportWidth;
    // float viewH = cameraComponent.camera.viewportHeight;

    // shapeRenderer.setProjectionMatrix(cameraComponent.camera.combined);

    // shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
    // shapeRenderer.setColor(1, 1, 1, 0.5f);

    // int startX = (int) (camCenter.x - viewW / 2);
    //     int endX = (int) (camCenter.x + viewW / 2 + 1);
    //     int startY = (int) (camCenter.y - viewH / 2);
    //     int endY = (int) (camCenter.y + viewH / 2 + 1);

    //     float thickness = Config.BUILD_GRID_LINE_THICKNESS;

    //     for (int x = startX; x <= endX; x++) {
    //         shapeRenderer.rect(x - thickness / 2f, startY, thickness, endY - startY);
    //     }

    //     for (int y = startY; y <= endY; y++) {
    //         shapeRenderer.rect(startX, y - thickness / 2f, endX - startX, thickness);
    //     }
    //     shapeRenderer.end();

    // Render each farm's image at its corresponding grid square
    batch.begin();
    for (Map.Entry<Vector2, Entity> entry : farmSelection.gridToFarm.entrySet()) {
      Vector2 gridPos = entry.getKey(); // gridPos.x is col, gridPos.y is row
      Vector2 center = gridCenters[(int) gridPos.y][(int) gridPos.x];
      // Retrieve SpriteComponent (assumed field 'textureKey')
      SpriteComponent sprite = Mappers.sprite.get(entry.getValue());
      if (sprite != null) {
        Texture texture = assetManager.get(sprite.getTexturePath(), Texture.class); // replaced sprite.textureKey with sprite.texture
        // Draw texture centered at the grid square (adjust offset if needed)
        batch.draw(texture, center.x - texture.getWidth() / 2f, center.y - texture.getHeight() / 2f);
      }
    }
    batch.end();

    engine.update(delta);

    joystickOverlay.render();
  }

  private Vector2[][] computeGridSquareCenters(Vector2 camCenter, float viewH) {
    final int rows = Config.WORLD_MAP_GRID_HEIGHT; 
    final int cols = Config.WORLD_MAP_GRID_WIDTH; 
    // Total grid height is 90% of the viewport height.
    float gridHeight = viewH * 0.9f;
    // Each square is a square so its side equals gridHeight divided by the number of rows.
    float squareSize = gridHeight / rows;
    float gridWidth = squareSize * cols;

    // Bottom left corner of the grid.
    float gridLeft = camCenter.x - gridWidth / 2;
    float gridBottom = camCenter.y - gridHeight / 2;

    // Create an array to hold the center positions.
    Vector2[][] centers = new Vector2[rows][cols];
    for (int row = 0; row < rows; row++) {
        for (int col = 0; col < cols; col++) {
            float x = gridLeft + (col + 0.5f) * squareSize;
            float y = gridBottom + (row + 0.5f) * squareSize;
            centers[row][col] = new Vector2(x, y);
        }
    }
    return centers;
  }

  @Override
  public void dispose() {
    batch.dispose();
  }
}

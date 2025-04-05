package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.GridPoint2;

import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.ActiveWorldEntity;
import io.github.StarvingValley.models.components.BuildPreviewComponent;
import io.github.StarvingValley.models.components.HiddenComponent;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.components.SpriteComponent;
import io.github.StarvingValley.models.components.TileOccupierComponent;
import io.github.StarvingValley.models.components.WorldLayerComponent;
import io.github.StarvingValley.models.types.WorldLayer;
import io.github.StarvingValley.utils.PlacementRules;
import io.github.StarvingValley.utils.TileUtils;

public class BuildPreviewSystem extends IteratingSystem {

  private final Camera camera;

  public BuildPreviewSystem(Camera camera) {
    super(
        Family.all(
            BuildPreviewComponent.class,
            PositionComponent.class,
            SpriteComponent.class,
            WorldLayerComponent.class)
            .get());

    this.camera = camera;
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    Engine engine = getEngine();
    WorldLayer worldLayer = Mappers.worldLayer.get(entity).layer;

    GridPoint2 hoveredTile = TileUtils.getHoveredTile(camera);

    ImmutableArray<Entity> blockingEntities = engine.getEntitiesFor(
        Family.all(
            TileOccupierComponent.class, WorldLayerComponent.class, ActiveWorldEntity.class)
            .get());

    boolean canPlaceEntity = PlacementRules.canPlace(hoveredTile, worldLayer, blockingEntities);

    BuildPreviewComponent buildPreview = Mappers.buildPreview.get(entity);
    buildPreview.isBlocked = !canPlaceEntity;

    if (canPlaceEntity) {
      entity.remove(HiddenComponent.class);
    } else {
      entity.add(new HiddenComponent());
    }

    PositionComponent positionComponent = Mappers.position.get(entity);
    positionComponent.position.x = hoveredTile.x;
    positionComponent.position.y = hoveredTile.y;
  }
}

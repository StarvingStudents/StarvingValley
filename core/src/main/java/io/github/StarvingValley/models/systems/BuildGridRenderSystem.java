package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import io.github.StarvingValley.config.Config;
import io.github.StarvingValley.models.components.BuildPreviewComponent;
import io.github.StarvingValley.models.types.GameContext;

public class BuildGridRenderSystem extends EntitySystem {
    private final ShapeRenderer shapeRenderer;
    private GameContext context;

    public BuildGridRenderSystem(GameContext context) {
        this.context = context;
        this.shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void update(float deltaTime) {
        // Build mode is only enabled when there are preview entities
        if (getEngine().getEntitiesFor(Family.all(BuildPreviewComponent.class).get()).size() == 0) {
            return;
        }

        shapeRenderer.setProjectionMatrix(context.camera.combined);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(1, 1, 1, 0.3f);

        Vector2 camCenter = new Vector2(context.camera.position.x, context.camera.position.y);
        float viewW = context.camera.viewportWidth;
        float viewH = context.camera.viewportHeight;

        // Only render the grid on tiles visible to the camera
        int startX = (int) (camCenter.x - viewW / 2);
        int endX = (int) (camCenter.x + viewW / 2 + 1);
        int startY = (int) (camCenter.y - viewH / 2);
        int endY = (int) (camCenter.y + viewH / 2 + 1);

        float thickness = Config.BUILD_GRID_LINE_THICKNESS;

        for (int x = startX; x <= endX; x++) {
            shapeRenderer.rect(x - thickness / 2f, startY, thickness, endY - startY);
        }

        for (int y = startY; y <= endY; y++) {
            shapeRenderer.rect(startX, y - thickness / 2f, endX - startX, thickness);
        }
        shapeRenderer.end();
    }
}

package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import io.github.StarvingValley.config.Config;
import io.github.StarvingValley.models.components.BuildPreviewComponent;

public class BuildGridRenderSystem extends EntitySystem {

    private final Camera camera;
    private final ShapeRenderer shapeRenderer;

    public BuildGridRenderSystem(Camera camera) {
        this.camera = camera;
        this.shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void update(float deltaTime) {
        // Build mode is only enabled when there are preview entities
        if (getEngine().getEntitiesFor(Family.all(BuildPreviewComponent.class).get()).size() == 0) {
            return;
        }

        shapeRenderer.setProjectionMatrix(camera.combined);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(1, 1, 1, 0.3f);

        Vector2 camCenter = new Vector2(camera.position.x, camera.position.y);
        float viewW = camera.viewportWidth;
        float viewH = camera.viewportHeight;

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

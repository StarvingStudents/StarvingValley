package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import io.github.StarvingValley.models.components.BuildPreviewComponent;
import io.github.StarvingValley.models.components.EatingComponent;

public class EatHeldFoodButtonSystem extends EntitySystem {

    private final Camera camera; 
    private final ShapeRenderer shapeRenderer;

    public EatHeldFoodButtonSystem(Camera camera) {
        this.camera = camera;
        this.shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void update(float deltaTime) {
        if (getEngine().getEntitiesFor(Family.all(BuildPreviewComponent.class).get()).size() == 0) {
            return; 
        }

        Gdx.gl.glEnable(GL20.GL_BLEND);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(1, 1, 1, 0.3f);

        Vector2 camCenter = new Vector2(camera.position.x, camera.position.y);
        float viewW = camera.viewportWidth;
        float viewH = camera.viewportHeight;

        shapeRenderer.rect(camCenter.x + viewW * 6, camCenter.y + viewH * 80, viewH * 60, viewH * 30); 
        shapeRenderer.end(); 
    }
    
}

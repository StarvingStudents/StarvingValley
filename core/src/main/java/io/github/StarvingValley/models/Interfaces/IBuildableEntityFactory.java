package io.github.StarvingValley.models.Interfaces;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.GridPoint2;

import io.github.StarvingValley.models.types.WorldLayer;

//TODO: Remove prefix 'I' from interfaces
public interface IBuildableEntityFactory {
    Entity createAt(GridPoint2 tile);
    WorldLayer getWorldLayer();
}

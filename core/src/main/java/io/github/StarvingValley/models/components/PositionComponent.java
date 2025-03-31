package io.github.StarvingValley.models.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector3;

public class PositionComponent implements Component {
    public Vector3 position = new Vector3();

    public PositionComponent(float x, float y) {
        this.position = new Vector3(x, y, 0);
    }

    public PositionComponent(float x, float y, float z) {
        this.position = new Vector3(x, y, z);
    }
}

package io.github.StarvingValley.models.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class InputComponent implements Component {
    public Vector2 movingDirection;

    public InputComponent() {
        movingDirection = new Vector2();
    }

    public InputComponent(Vector2 movingDirection) {
        this.movingDirection = movingDirection;
    }
}

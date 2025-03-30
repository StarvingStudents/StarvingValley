package io.github.StarvingValley.controllers;

import com.badlogic.gdx.math.Vector2;

import io.github.StarvingValley.models.state.InputState;

public class JoystickController {
    float threshold = 0.1f;

    public void handleJoystickDrag(Vector2 direction) {
        if (direction.len() < threshold) {
            direction.setLength(0);
        }
        
        InputState.movingDirection = direction;
    }

    public void resetJoystick() {
        InputState.movingDirection = new Vector2();
    }
}

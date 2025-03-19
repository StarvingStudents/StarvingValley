package io.github.StarvingValley.controllers;

import com.badlogic.gdx.math.Vector2;

import io.github.StarvingValley.models.state.InputState;

public class JoystickController {
    float threshold = 10f;

    public void handleJoystickDrag(Vector2 direction) {
        InputState.isMovingRight = direction.x > threshold;
        InputState.isMovingLeft = direction.x < -threshold;
        InputState.isMovingUp = direction.y > threshold;
        InputState.isMovingDown = direction.y < -threshold;
    }

    public void resetJoystick() {
        InputState.isMovingDown = false;
        InputState.isMovingUp = false;
        InputState.isMovingRight = false;
        InputState.isMovingLeft = false;
    }
}

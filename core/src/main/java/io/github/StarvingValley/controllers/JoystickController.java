package io.github.StarvingValley.controllers;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;

import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.InputComponent;
import io.github.StarvingValley.models.components.PlayerComponent;

public class JoystickController {
    private final Engine engine;
    private final float threshold = 0.1f;

    public JoystickController(Engine engine) {
        this.engine = engine;
    }

    public void handleJoystickDrag(Vector2 direction) {
        if (direction.len() < threshold) {
            direction.setLength(0);
        }

        for (Entity player : getPlayers()) {
            InputComponent input = Mappers.input.get(player);
            if (input != null) {
                input.movingDirection.set(direction);
            }
        }
    }

    public void resetJoystick() {
        for (Entity player : getPlayers()) {
            InputComponent input = Mappers.input.get(player);
            if (input != null) {
                input.movingDirection.setZero();
            }
        }
    }

    private ImmutableArray<Entity> getPlayers() {
        ImmutableArray<Entity> players = engine
                .getEntitiesFor(Family.all(PlayerComponent.class, InputComponent.class).get());
        return players;
    }
}

package io.github.StarvingValley.controllers;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.InputAdapter;

import io.github.StarvingValley.models.components.TargetComponent;

public class InputController extends InputAdapter {
    private final Entity player;

    public InputController(Entity player) {
        this.player = player;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        TargetComponent target = player.getComponent(TargetComponent.class);
        if (target != null) {
            target.target.set(screenX, 480 - screenY);
        }
        return true;
    }
}

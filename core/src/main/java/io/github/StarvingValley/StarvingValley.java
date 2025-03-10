package io.github.StarvingValley;

import com.badlogic.gdx.Game;

import io.github.StarvingValley.view.GameScreen;

public class StarvingValley extends Game {
    @Override
    public void create() {
        setScreen(new GameScreen());
    }
}

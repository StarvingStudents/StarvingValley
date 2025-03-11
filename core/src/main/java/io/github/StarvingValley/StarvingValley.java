package io.github.StarvingValley;

import com.badlogic.gdx.Game;

import io.github.StarvingValley.models.Interfaces.IFirebaseRepository;
import io.github.StarvingValley.views.GameScreen;

public class StarvingValley extends Game {

    IFirebaseRepository _firebaseRepository;

    public StarvingValley(IFirebaseRepository firebaseRepository) {
        _firebaseRepository = firebaseRepository;
    }

    @Override
    public void create() {
        setScreen(new GameScreen(_firebaseRepository));
    }
}

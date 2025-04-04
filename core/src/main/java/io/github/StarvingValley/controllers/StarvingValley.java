package io.github.StarvingValley.controllers;

import com.badlogic.gdx.Game;

import io.github.StarvingValley.models.Interfaces.IFirebaseRepository;
import io.github.StarvingValley.views.FarmView;

public class StarvingValley extends Game {

    IFirebaseRepository _firebaseRepository;

    public StarvingValley(IFirebaseRepository firebaseRepository) {
        _firebaseRepository = firebaseRepository;
    }

    @Override
    public void create() {
        setScreen(new FarmView(_firebaseRepository));
    }
}

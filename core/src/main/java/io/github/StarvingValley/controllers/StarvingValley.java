package io.github.StarvingValley.controllers;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import io.github.StarvingValley.models.Interfaces.IFirebaseRepository;
import io.github.StarvingValley.views.FarmView;
import io.github.StarvingValley.views.WorldMapView;

public class StarvingValley extends Game {

  IFirebaseRepository _firebaseRepository;
  StarvingValley game;

  public StarvingValley(IFirebaseRepository firebaseRepository) {
    _firebaseRepository = firebaseRepository;
  }

  @Override
  public void create() {
    // TODO: Comment out FarmView and uncomment WorldMapView to test the world map
        //setScreen(new FarmView(_firebaseRepository));


    setScreen(new WorldMapView(this, _firebaseRepository));
    //    setScreen(new VisitFarmView(_firebaseRepository, "kA22VW0RofdBTDkHN09gCH355rI3"));
  }

  public void switchView(Screen newScreen) {
    // Prevent disposing of the same screen
    if (getScreen() == newScreen) {
      return;

    }

    // is this needed? switching view only works when this is not included
    // Dispose of the current screen before switching
    //    if (getScreen() != null) {
    //      getScreen().dispose();
    //    }

    setScreen(newScreen);
  } // Now able to switch between screens by calling switchView(newScreen) from any
  // screen
}

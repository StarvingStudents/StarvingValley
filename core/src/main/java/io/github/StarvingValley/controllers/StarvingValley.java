package io.github.StarvingValley.controllers;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

import io.github.StarvingValley.models.interfaces.AuthCallback;
import io.github.StarvingValley.models.interfaces.PlayerDataRepository;
import io.github.StarvingValley.models.types.ScreenType;
import io.github.StarvingValley.views.FarmView;
import io.github.StarvingValley.views.VillageView;
import io.github.StarvingValley.views.WorldMapView;

public class StarvingValley extends Game {
  PlayerDataRepository _firebaseRepository;
  StarvingValley game;

  private boolean isAuthenticated = false;
  private ScreenType pendingScreenType;

  public StarvingValley(PlayerDataRepository firebaseRepository) {
    _firebaseRepository = firebaseRepository;
  }

  @Override
  public void create() {
    _firebaseRepository.registerOrSignInWithDeviceId(
        new AuthCallback() {
          @Override
          public void onSuccess() {
            isAuthenticated = true;
          }

          @Override
          public void onFailure(String errorMessage) {
            // TODO: Fail gracefully - error screen
            throw new RuntimeException("Authentication failed: " + errorMessage);
          }
        });
    ScreenType currentScreen = _firebaseRepository.getCurrentScreen();
    pendingScreenType = (currentScreen == null) ? ScreenType.FARM : currentScreen;
  }

  @Override
  public void render() {
    super.render();

    // Process pending view switch at the end of the render cycle => avoid potential
    // buffer errors
    if (isAuthenticated && pendingScreenType != null) {
      Screen oldScreen = getScreen();
      setScreen(null);
      if (oldScreen != null) {
        oldScreen.dispose();
      }

      if (pendingScreenType == ScreenType.VILLAGE) {
        setScreen(new VillageView(this, _firebaseRepository));
      } else if (pendingScreenType == ScreenType.FARM) {
        setScreen(new FarmView(this, _firebaseRepository));
      } else if (pendingScreenType == ScreenType.WORLD_MAP) {
        setScreen(new WorldMapView(this, _firebaseRepository));
      }

      pendingScreenType = null;
    }
  }

  // TODO phase out - use requestViewSwitch insted => switches view at end of
  // render loop
  @Deprecated
  public void switchView(Screen newScreen) {
    // Prevent disposing of the same screen
    if (getScreen() == newScreen) {
      return;
    }

    // is this needed? switching view only works when this is not included
    // Dispose of the current screen before switching
    // if (getScreen() != null) {
    // getScreen().dispose();
    // }
    setScreen(newScreen);
  } // Now able to switch between screens by calling switchView(newScreen) from any

  public void requestViewSwitch(ScreenType screenType) {
    pendingScreenType = screenType;
  }
}

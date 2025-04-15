package io.github.StarvingValley.controllers;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

import io.github.StarvingValley.models.Interfaces.AuthCallback;
import io.github.StarvingValley.models.Interfaces.IFirebaseRepository;
import io.github.StarvingValley.models.types.ViewType;
import io.github.StarvingValley.views.FarmView;
import io.github.StarvingValley.views.VillageView;

public class StarvingValley extends Game {

    IFirebaseRepository _firebaseRepository;
    StarvingValley game;

    private boolean isAuthenticated = false;
    private ViewType pendingViewType = ViewType.FARM;

    public StarvingValley(IFirebaseRepository firebaseRepository) {
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
    }

    @Override
    public void render() {
        super.render();

        // Process pending view switch at the end of the render cycle => avoid potential buffer errors
        if (isAuthenticated && pendingViewType != null) {
            Screen oldScreen = getScreen();
            setScreen(null);
            if (oldScreen != null) {
                oldScreen.dispose();
            }

            // Create new screen based on type
            if (pendingViewType == ViewType.VILLAGE) {
                setScreen(new VillageView(this, _firebaseRepository));
            } else if (pendingViewType == ViewType.FARM) {
                setScreen(new FarmView(this, _firebaseRepository));
            }

            pendingViewType = null;
        } // else if (isAuthenticated && getScreen() == null) {
    }

    // TODO phase out - use requestViewSwitch insted => switches view at end of render loop
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

    public void requestViewSwitch(ViewType viewType) {
        pendingViewType = viewType;
    }
}

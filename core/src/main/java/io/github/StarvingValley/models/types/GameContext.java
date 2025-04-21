package io.github.StarvingValley.models.types;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.github.StarvingValley.models.events.EventBus;
import io.github.StarvingValley.models.interfaces.PlayerDataRepository;
import io.github.StarvingValley.utils.Assets;

public class GameContext {
    public Entity player;
    public OrthographicCamera camera;
    public EventBus eventBus;
    public SpriteBatch spriteBatch;
    public AssetManager assetManager;
    public PlayerDataRepository firebaseRepository;
    public Engine engine;
    public Assets assets;

    public GameContext(EventBus eventBus, SpriteBatch spriteBatch,
            AssetManager assetManager, PlayerDataRepository firebaseRepository, Engine engine, Assets assets) {
        this.eventBus = eventBus;
        this.spriteBatch = spriteBatch;
        this.assetManager = assetManager;
        this.firebaseRepository = firebaseRepository;
        this.engine = engine;
        this.assets = assets;
    }
}

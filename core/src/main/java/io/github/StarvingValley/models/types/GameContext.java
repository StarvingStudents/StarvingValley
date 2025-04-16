package io.github.StarvingValley.models.types;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.github.StarvingValley.models.Interfaces.IFirebaseRepository;
import io.github.StarvingValley.models.events.EventBus;
import io.github.StarvingValley.utils.Assets;

public class GameContext {
    public Entity player;
    public OrthographicCamera camera;
    public EventBus eventBus;
    public SpriteBatch spriteBatch;
    public AssetManager assetManager;
    public IFirebaseRepository firebaseRepository;
    public Engine engine;
    public Assets assets;
}

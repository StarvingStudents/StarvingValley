package io.github.StarvingValley.models.dto;

import com.badlogic.gdx.graphics.Texture;

import io.github.StarvingValley.models.types.WorldLayer;

//TODO: Replace with more methods/builder-actor pattern
public class WorldObjectConfig {
    public boolean blocksMovement = false;
    public boolean blocksPlacement = false;
    public WorldLayer worldLayer = null;
    public Texture texture;
}

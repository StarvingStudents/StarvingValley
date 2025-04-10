package io.github.StarvingValley.models.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;
import java.util.Map;

public class AnimationComponent implements Component {
    public Map<String, Animation<TextureRegion>> animations = new HashMap<>();
    public String currentAnimation = "idle_down";
    public float stateTime = 0f;
}

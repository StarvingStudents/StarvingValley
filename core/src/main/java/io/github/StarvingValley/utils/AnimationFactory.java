package io.github.StarvingValley.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;

import io.github.StarvingValley.models.components.AnimationComponent;
import io.github.StarvingValley.models.types.PrefabType;

public class AnimationFactory {

    public static AnimationComponent createAnimationsForType(PrefabType type, AssetManager assetManager) {
        switch (type) {
            case PLAYER:
                return createPlayerAnimations(assetManager);
            default:
                return null;
        }
    }
    private static AnimationComponent createPlayerAnimations(AssetManager manager) {
        AnimationComponent anim = new AnimationComponent();

        // Frame data: 2 frames, 16x32 per sprite
        int cols = 2, rows = 1;
        float speed = 0.2f;

        anim.animations.put("idle_down", AnimationUtils.loadFromSheet(manager, "idle_down.png", cols, rows, speed, PlayMode.LOOP));
        anim.animations.put("idle_up", AnimationUtils.loadFromSheet(manager, "idle_up.png", cols, rows, speed, PlayMode.LOOP));
        anim.animations.put("idle_left", AnimationUtils.loadFromSheet(manager, "idle_left.png", cols, rows, speed, PlayMode.LOOP));
        anim.animations.put("idle_right", AnimationUtils.loadFromSheet(manager, "idle_right.png", cols, rows, speed, PlayMode.LOOP));

        anim.animations.put("walking_down", AnimationUtils.loadFromSheet(manager, "walking_down.png", cols, rows, speed, PlayMode.LOOP));
        anim.animations.put("walking_up", AnimationUtils.loadFromSheet(manager, "walking_up.png", cols, rows, speed, PlayMode.LOOP));
        anim.animations.put("walking_left", AnimationUtils.loadFromSheet(manager, "walking_left.png", cols, rows, speed, PlayMode.LOOP));
        anim.animations.put("walking_right", AnimationUtils.loadFromSheet(manager, "walking_right.png", cols, rows, speed, PlayMode.LOOP));

        anim.animations.put("action_soil_down", AnimationUtils.loadFromSheet(manager, "action_soil_down.png", cols, rows, speed, PlayMode.NORMAL));
        anim.animations.put("action_soil_up", AnimationUtils.loadFromSheet(manager, "action_soil_up.png", cols, rows, speed, PlayMode.NORMAL));
        anim.animations.put("action_soil_left", AnimationUtils.loadFromSheet(manager, "action_soil_left.png", cols, rows, speed, PlayMode.NORMAL));
        anim.animations.put("action_soil_right", AnimationUtils.loadFromSheet(manager, "action_soil_right.png", cols, rows, speed, PlayMode.NORMAL));

        anim.currentAnimation = "idle_down";
        return anim;
    }
}

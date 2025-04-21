package io.github.StarvingValley.utils;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;

import io.github.StarvingValley.models.components.AnimationComponent;
import io.github.StarvingValley.models.types.PrefabType;

public class AnimationFactory {

    public static AnimationComponent createAnimationsForType(PrefabType type, Assets assets) {
        switch (type) {
            case PLAYER:
                return createPlayerAnimations(assets);
            default:
                return null;
        }
    }
    private static AnimationComponent createPlayerAnimations(Assets assets) {
        AnimationComponent anim = new AnimationComponent();

        // Frame data: 2 frames, 16x32 per sprite
        int cols = 2, rows = 1;
        float speed = 0.2f;

        anim.animations.put("idle_down", AnimationUtils.loadFromSheet(assets, "idle_down.png", cols, rows, speed, PlayMode.LOOP));
        anim.animations.put("idle_up", AnimationUtils.loadFromSheet(assets, "idle_up.png", cols, rows, speed, PlayMode.LOOP));
        anim.animations.put("idle_left", AnimationUtils.loadFromSheet(assets, "idle_left.png", cols, rows, speed, PlayMode.LOOP));
        anim.animations.put("idle_right", AnimationUtils.loadFromSheet(assets, "idle_right.png", cols, rows, speed, PlayMode.LOOP));

        anim.animations.put("walking_down", AnimationUtils.loadFromSheet(assets, "walking_down.png", cols, rows, speed, PlayMode.LOOP));
        anim.animations.put("walking_up", AnimationUtils.loadFromSheet(assets, "walking_up.png", cols, rows, speed, PlayMode.LOOP));
        anim.animations.put("walking_left", AnimationUtils.loadFromSheet(assets, "walking_left.png", cols, rows, speed, PlayMode.LOOP));
        anim.animations.put("walking_right", AnimationUtils.loadFromSheet(assets, "walking_right.png", cols, rows, speed, PlayMode.LOOP));

        anim.animations.put("action_soil_down", AnimationUtils.loadFromSheet(assets, "action_soil_down.png", cols, rows, speed, PlayMode.NORMAL));
        anim.animations.put("action_soil_up", AnimationUtils.loadFromSheet(assets, "action_soil_up.png", cols, rows, speed, PlayMode.NORMAL));
        anim.animations.put("action_soil_left", AnimationUtils.loadFromSheet(assets, "action_soil_left.png", cols, rows, speed, PlayMode.NORMAL));
        anim.animations.put("action_soil_right", AnimationUtils.loadFromSheet(assets, "action_soil_right.png", cols, rows, speed, PlayMode.NORMAL));

        // Add axe action animations
        anim.animations.put("action_axe_down", AnimationUtils.loadFromSheet(assets, "action_axe_down.png", cols, rows, speed, PlayMode.NORMAL));
        anim.animations.put("action_axe_up", AnimationUtils.loadFromSheet(assets, "action_axe_up.png", cols, rows, speed, PlayMode.NORMAL));
        anim.animations.put("action_axe_left", AnimationUtils.loadFromSheet(assets, "action_axe_left.png", cols, rows, speed, PlayMode.NORMAL));
        anim.animations.put("action_axe_right", AnimationUtils.loadFromSheet(assets, "action_axe_right.png", cols, rows, speed, PlayMode.NORMAL));

        anim.currentAnimation = "idle_down";
        return anim;
    }
}

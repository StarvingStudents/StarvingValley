package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;

import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.*;

public class AnimationSystem extends IteratingSystem {

    public AnimationSystem() {
        super(Family.all(AnimationComponent.class, SpriteComponent.class, InputComponent.class, VelocityComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        AnimationComponent anim = Mappers.animation.get(entity);
        SpriteComponent sprite = Mappers.sprite.get(entity);
        InputComponent input = Mappers.input.get(entity);
        VelocityComponent velocity = Mappers.velocity.get(entity);

        Animation<TextureRegion> currentAnim = anim.animations.get(anim.currentAnimation);
        if (currentAnim == null) return;

        boolean isAction = anim.currentAnimation.startsWith("action_");
        boolean isNormal = currentAnim.getPlayMode() == Animation.PlayMode.NORMAL;

        anim.stateTime += deltaTime;

        // wait for action to finish
        if (isAction && isNormal) {
            TextureRegion frame = currentAnim.getKeyFrame(anim.stateTime, false);
            sprite.sprite.setRegion(frame);
            sprite.sprite.setSize(frame.getRegionWidth(), frame.getRegionHeight());

            // reset to idle after action
            if (currentAnim.isAnimationFinished(anim.stateTime)) {
                String[] parts = anim.currentAnimation.split("_");
                String direction = parts.length >= 3 ? parts[2] : "down";
                anim.currentAnimation = "idle_" + direction;
                anim.stateTime = 0f;
            }

            return;
        }

        // walking/idle animation
        Vector2 dir = input.movingDirection;
        String nextState = anim.currentAnimation;

        if (velocity.velocity.len2() > 0.001f) {
            if (Math.abs(dir.x) > Math.abs(dir.y)) {
                nextState = dir.x > 0 ? "walking_right" : "walking_left";
            } else {
                nextState = dir.y > 0 ? "walking_up" : "walking_down";
            }
        } else if (anim.currentAnimation.startsWith("walking_")) {
            String lastDir = anim.currentAnimation.substring("walking_".length());
            nextState = "idle_" + lastDir;
        }

        if (!anim.currentAnimation.equals(nextState)) {
            anim.currentAnimation = nextState;
            anim.stateTime = 0f;
            currentAnim = anim.animations.get(anim.currentAnimation);
        }

        if (currentAnim != null) {
            TextureRegion frame = currentAnim.getKeyFrame(anim.stateTime, currentAnim.getPlayMode() != Animation.PlayMode.NORMAL);
            sprite.sprite.setRegion(frame);
            sprite.sprite.setSize(frame.getRegionWidth(), frame.getRegionHeight());
        }
    }
}

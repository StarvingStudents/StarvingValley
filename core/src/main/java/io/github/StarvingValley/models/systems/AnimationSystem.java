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

        anim.stateTime += deltaTime;

        // Determine direction from input
        Vector2 dir = input.movingDirection;
        String nextState = anim.currentAnimation;

        if (velocity.velocity.len2() > 0.001f) {
            if (Math.abs(dir.x) > Math.abs(dir.y)) {
                nextState = dir.x > 0 ? "walking_right" : "walking_left";
            } else {
                nextState = dir.y > 0 ? "walking_up" : "walking_down";
            }
        }else{ // Preserve direction from last animation
            if (anim.currentAnimation.startsWith("walking_")) {
                String lastDir = anim.currentAnimation.substring("walking_".length());
                nextState = "idle_" + lastDir;
            } else if (anim.currentAnimation.startsWith("idle_")) {
                nextState = anim.currentAnimation; // stay idle in last known direction
            } else {
                nextState = "idle_down"; // fallback default
            }
        }


        if (!anim.currentAnimation.equals(nextState)) {
            anim.currentAnimation = nextState;
            anim.stateTime = 0f;
        }

        Animation<TextureRegion> animation = anim.animations.get(anim.currentAnimation);
        if (animation != null) {
            TextureRegion frame = animation.getKeyFrame(anim.stateTime, anim.loop);
            sprite.sprite.setRegion(frame);
        }
    }
}

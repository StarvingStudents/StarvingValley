package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import io.github.StarvingValley.config.Config;
import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.AnimationComponent;
import io.github.StarvingValley.models.components.InputComponent;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.components.SizeComponent;
import io.github.StarvingValley.models.components.SpriteComponent;
import io.github.StarvingValley.models.components.VelocityComponent;

public class AnimationSystem extends IteratingSystem {
    public AnimationSystem() {
        super(Family.all(AnimationComponent.class, InputComponent.class, VelocityComponent.class,
                PositionComponent.class, SizeComponent.class, SpriteComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        AnimationComponent anim = Mappers.animation.get(entity);
        InputComponent input = Mappers.input.get(entity);
        VelocityComponent velocity = Mappers.velocity.get(entity);
        SizeComponent size = Mappers.size.get(entity);
        SpriteComponent spriteComponent = Mappers.sprite.get(entity);

        Animation<TextureRegion> currentAnim = anim.animations.get(anim.currentAnimation);
        if (currentAnim == null)
            return;

        boolean isAction = anim.currentAnimation.startsWith("action_");
        boolean isNormal = currentAnim.getPlayMode() == Animation.PlayMode.NORMAL;

        anim.stateTime += deltaTime;
        Sprite sprite = spriteComponent.sprite;

        TextureRegion frame = currentAnim.getKeyFrame(anim.stateTime, false);
        sprite.setRegion(frame);

        float widthInTiles = frame.getRegionWidth() / Config.PIXELS_PER_TILE;
        float heightInTiles = frame.getRegionHeight() / Config.PIXELS_PER_TILE;

        if (isAction && isNormal) {
            sprite.setSize(widthInTiles, heightInTiles);
        } else {
            sprite.setSize(size.width, size.height);
        }

        if (isAction && isNormal && currentAnim.isAnimationFinished(anim.stateTime)) {
            String[] parts = anim.currentAnimation.split("_");
            String direction = parts.length >= 3 ? parts[2] : "down";
            anim.currentAnimation = "idle_" + direction;
            anim.stateTime = 0f;
        }

        if (!isAction) {
            String nextState = anim.currentAnimation;
            Vector2 dir = input.movingDirection;

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
            }
        }

        size.useRegionSize = true;
        spriteComponent.sprite = sprite;
    }
}

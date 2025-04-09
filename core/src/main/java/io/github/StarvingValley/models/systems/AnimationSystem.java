package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;
import java.util.Map;

import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.*;
import io.github.StarvingValley.models.types.GameContext;
import io.github.StarvingValley.config.Config;

public class AnimationSystem extends IteratingSystem {

    private final GameContext context;
    private final Map<Entity, Sprite> animatedSprites = new HashMap<>();
    public AnimationSystem(GameContext context) {
        super(Family.all(AnimationComponent.class, InputComponent.class, VelocityComponent.class, PositionComponent.class, SizeComponent.class).get());
        this.context = context;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        AnimationComponent anim = Mappers.animation.get(entity);
        InputComponent input = Mappers.input.get(entity);
        VelocityComponent velocity = Mappers.velocity.get(entity);
        PositionComponent position = Mappers.position.get(entity);
        SizeComponent size = Mappers.size.get(entity);

        Animation<TextureRegion> currentAnim = anim.animations.get(anim.currentAnimation);
        if (currentAnim == null) return;

        boolean isAction = anim.currentAnimation.startsWith("action_");
        boolean isNormal = currentAnim.getPlayMode() == Animation.PlayMode.NORMAL;

        anim.stateTime += deltaTime;
        Sprite sprite = animatedSprites.computeIfAbsent(entity, e -> new Sprite());

        TextureRegion frame = currentAnim.getKeyFrame(anim.stateTime, false);
        sprite.setRegion(frame);

        float widthInTiles = frame.getRegionWidth() / Config.PIXELS_PER_TILE;
        float heightInTiles = frame.getRegionHeight() / Config.PIXELS_PER_TILE;

        if (isAction && isNormal) {
            sprite.setSize(widthInTiles, heightInTiles);
        } else {
            sprite.setSize(size.width, size.height);
        }

        float renderX = position.position.x - sprite.getWidth() / 2f + size.width / 2f;
        float renderY = position.position.y - sprite.getHeight() / 2f + size.height / 2f;
        sprite.setPosition(renderX, renderY);

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

        context.spriteBatch.begin();
        sprite.draw(context.spriteBatch);
        context.spriteBatch.end();

    }
}

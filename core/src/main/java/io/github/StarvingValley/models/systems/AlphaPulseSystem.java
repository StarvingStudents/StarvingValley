package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;

import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.PulseAlphaComponent;
import io.github.StarvingValley.models.components.SpriteComponent;

public class AlphaPulseSystem extends IteratingSystem {

    public AlphaPulseSystem() {
        super(Family.all(PulseAlphaComponent.class, SpriteComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PulseAlphaComponent pulse = Mappers.pulseAlpha.get(entity);
        SpriteComponent sprite = Mappers.sprite.get(entity);

        pulse.time += deltaTime;

        sprite.sprite.setAlpha(pulse.baseAlpha + pulse.amplitude * MathUtils.sin(pulse.time * pulse.speed));
    }
}

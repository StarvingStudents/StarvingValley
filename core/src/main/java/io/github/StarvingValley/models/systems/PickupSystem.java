package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;
import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.PickupComponent;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.components.SyncComponent;
import io.github.StarvingValley.models.components.BuildPreviewComponent;
import io.github.StarvingValley.models.events.EventBus;
import io.github.StarvingValley.models.events.ItemPickupAttemptEvent;
import io.github.StarvingValley.models.events.ItemPickupSuccessEvent;
import io.github.StarvingValley.models.events.PickupModeActivatedEvent;
import io.github.StarvingValley.models.events.PickupModeDeactivatedEvent;
import io.github.StarvingValley.models.events.EntityRemovedEvent;
import io.github.StarvingValley.models.types.GameContext;
import io.github.StarvingValley.models.utils.PickupUtils;

public class PickupSystem extends IteratingSystem {
    private final GameContext context;
    private boolean isPickupModeActive = false;
    private float pickupModeTimer = 0f;
    private static final float PICKUP_MODE_TIMEOUT = 5f;
    private final ShapeRenderer shapeRenderer;
    private final Vector3 touchPos = new Vector3();

    // Visuals for pickupable items when in pickup mode - can be removed/simplified/changed for later
    private float rippleTimer = 0f;
    private static final float RIPPLE_SPEED = 1.5f;
    private static final float RIPPLE_DURATION = 1.0f;
    private static final float MAX_RIPPLE_SIZE = 0.5f;

    public PickupSystem(GameContext context) {
        super(Family.all(PickupComponent.class, PositionComponent.class).get());
        this.context = context;
        this.shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void update(float deltaTime) {
        if (isPickupModeActive) {
            pickupModeTimer += deltaTime;
            rippleTimer += deltaTime * RIPPLE_SPEED;
            if (pickupModeTimer >= PICKUP_MODE_TIMEOUT) {
                deactivatePickupMode(PickupModeDeactivatedEvent.DeactivationReason.TIMEOUT);
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            if (isPickupModeActive) {
                deactivatePickupMode(PickupModeDeactivatedEvent.DeactivationReason.MANUAL);
            } else {
                activatePickupMode();
            }
        }

        if (isPickupModeActive) {
            ImmutableArray<Entity> buildPreviews = getEngine().getEntitiesFor(
                Family.all(BuildPreviewComponent.class).get()
            );
            if (buildPreviews.size() > 0) {
                deactivatePickupMode(PickupModeDeactivatedEvent.DeactivationReason.BUILD_MODE_ACTIVATED);
            }
        }

        super.update(deltaTime);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        if (!isPickupModeActive) return;

        PositionComponent pos = Mappers.position.get(entity);
        PickupComponent pickup = Mappers.pickup.get(entity);
        if (pos == null || pickup == null) return;

        // Draw visual feedback for pickup-able items
        shapeRenderer.setProjectionMatrix(context.camera.combined);
        shapeRenderer.begin(ShapeType.Line);

        boolean isInRange = PickupUtils.isWithinPickupRange(context.player, entity);

        float rippleProgress = rippleTimer % RIPPLE_DURATION;
        float rippleSize = (rippleProgress / RIPPLE_DURATION) * MAX_RIPPLE_SIZE;
        float alpha = 1.0f - (rippleProgress / RIPPLE_DURATION);

        if (isInRange) {
            // Draw green ripple when in range
            shapeRenderer.setColor(new Color(0, 1, 0, alpha * 0.8f));
            shapeRenderer.circle(pos.position.x, pos.position.y, rippleSize);
        } else {
            // Draw red ripple when out of range
            shapeRenderer.setColor(new Color(1, 0, 0, alpha * 0.8f));
            shapeRenderer.circle(pos.position.x, pos.position.y, rippleSize);
        }

        shapeRenderer.end();

        if (isInRange && Gdx.input.isTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            context.camera.unproject(touchPos);

            // Check if click is within the pickup range of this entity
            float distance = touchPos.dst2(pos.position.x, pos.position.y, 0);
            if (distance <= pickup.pickupRange * pickup.pickupRange) {
                handlePickup(entity);
            }
        }
    }

    private void activatePickupMode() {
        isPickupModeActive = true;
        pickupModeTimer = 0f;
        rippleTimer = 0f;
        Entity player = context.player;
        context.eventBus.publish(new PickupModeActivatedEvent(player));
    }

    private void deactivatePickupMode(PickupModeDeactivatedEvent.DeactivationReason reason) {
        isPickupModeActive = false;
        pickupModeTimer = 0f;
        rippleTimer = 0f;
        Entity player = context.player;
        context.eventBus.publish(new PickupModeDeactivatedEvent(player, reason));
    }

    private void handlePickup(Entity entity) {
        Entity player = context.player;
        if (player == null) return;

        context.eventBus.publish(new ItemPickupAttemptEvent(player, entity));
        String itemName = getItemName(entity);
        context.eventBus.publish(new ItemPickupSuccessEvent(player, entity, itemName));

        context.eventBus.publish(new EntityRemovedEvent(entity));

        getEngine().removeEntity(entity);

        deactivatePickupMode(PickupModeDeactivatedEvent.DeactivationReason.ITEM_PICKED);
    }

    private String getItemName(Entity entity) {
        return "Item";
    }
    public boolean isPickupModeActive() {
        return isPickupModeActive;
    }
}

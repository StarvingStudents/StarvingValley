package io.github.StarvingValley.models.systems;

import java.util.List;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;

import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.ButtonComponent;
import io.github.StarvingValley.models.components.ClickableComponent;
import io.github.StarvingValley.models.components.ClickedComponent;
import io.github.StarvingValley.models.components.PositionComponent;
import io.github.StarvingValley.models.components.SizeComponent;
import io.github.StarvingValley.models.events.TapEvent;
import io.github.StarvingValley.models.types.GameContext;
import io.github.StarvingValley.utils.TileUtils;

public class HUDButtonPressSystem extends EntitySystem {
    private GameContext context;

    public HUDButtonPressSystem(GameContext context) {
        this.context = context;
    }

    @Override
    public void update(float delta) {
        List<TapEvent> events = context.eventBus.getEvents(TapEvent.class);

        ImmutableArray<Entity> clickableEntities = getEngine()
                .getEntitiesFor(
                        Family.all(ButtonComponent.class, ClickableComponent.class)
                                .get());

        for (TapEvent clickEvent : events) {
            for (Entity entity : clickableEntities) {
                PositionComponent position = Mappers.position.get(entity);
                SizeComponent size = Mappers.size.get(entity);

                if (isOverlapping(position, size, clickEvent.tile)) {
                    // tile coordinate system as the button.
                    entity.add(new ClickedComponent());
                }
            }
        }
    }

    private boolean isOverlapping(PositionComponent position, SizeComponent size, GridPoint2 tile) {
        float left = position.position.x;
        float right = position.position.x + size.width;
        float bottom = position.position.y;
        float top = position.position.y + size.height;

        return tile.x >= left && tile.x <= right && tile.y >= bottom && tile.y <= top;
    }
}

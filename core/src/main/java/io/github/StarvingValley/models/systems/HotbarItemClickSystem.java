package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.ClickedComponent;
import io.github.StarvingValley.models.components.InventoryItemComponent;
import io.github.StarvingValley.models.components.SelectedHotbarItemComponent;
import io.github.StarvingValley.models.entities.EntityFactoryRegistry;
import io.github.StarvingValley.utils.BuildUtils;
import io.github.StarvingValley.utils.InventoryUtils;

public class HotbarItemClickSystem extends EntitySystem {
    @Override
    public void update(float deltaTime) {
        ImmutableArray<Entity> entities = getEngine().getEntitiesFor(
                Family.all(ClickedComponent.class, InventoryItemComponent.class).get());

        if (InventoryUtils.isInventoryOpen(getEngine()))
            return;

        for (Entity entity : entities) {
            if (Mappers.inventorySelectedItem.has(entity)) {
                entity.remove(SelectedHotbarItemComponent.class);
                BuildUtils.disableBuildPreview(getEngine());
            } else {
                entity.add(new SelectedHotbarItemComponent());
                triggerBuildMode(entity);
            }
        }
    }

    private void triggerBuildMode(Entity entity) {
        InventoryItemComponent inventoryItem = Mappers.inventoryItem.get(entity);
        if (inventoryItem == null)
            return;

        Entity item = EntityFactoryRegistry.create(inventoryItem.type);
        if (!Mappers.buildable.has(item))
            return;

        BuildUtils.enableBuildPreview(BuildUtils.getBuildsTypeFromType(inventoryItem.type), inventoryItem.type,
                getEngine());
    }
}

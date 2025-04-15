package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.ClickedComponent;
import io.github.StarvingValley.models.components.InventoryItemComponent;
import io.github.StarvingValley.models.components.SelectedHotbarItemComponent;
import io.github.StarvingValley.models.components.SelectedHotbarSlotComponent;
import io.github.StarvingValley.models.components.SpriteComponent;
import io.github.StarvingValley.models.entities.EntityFactoryRegistry;
import io.github.StarvingValley.utils.BuildUtils;
import io.github.StarvingValley.utils.InventoryUtils;

public class HotbarItemClickSystem extends EntitySystem {
    @Override
    public void update(float deltaTime) {
        if (InventoryUtils.isInventoryOpen(getEngine())) {
            return;
        }

        ImmutableArray<Entity> entities = getEngine()
                .getEntitiesFor(Family.all(ClickedComponent.class, InventoryItemComponent.class).get());

        for (Entity entity : entities) {
            InventoryItemComponent itemData = Mappers.inventoryItem.get(entity);

            Entity slot = InventoryUtils.getSlotEntity(getEngine(), itemData.slotX, itemData.slotY, true);

            boolean disabled = Mappers.selectedHotbarItem.has(entity);

            if (disabled) {
                entity.remove(SelectedHotbarItemComponent.class);
                if (slot != null)
                    slot.remove(SelectedHotbarSlotComponent.class);

                BuildUtils.disableBuildPreview(getEngine());
            } else {
                InventoryUtils.unselectSelectedHotbarItems(getEngine());

                entity.add(new SelectedHotbarItemComponent());
                if (slot != null)
                    slot.add(new SelectedHotbarSlotComponent());

                triggerBuildMode(entity);
            }

            updateSlotTexture(slot, disabled);
        }
    }

    private void triggerBuildMode(Entity entity) {
        InventoryItemComponent inventoryItem = Mappers.inventoryItem.get(entity);
        if (inventoryItem == null)
            return;

        Entity item = EntityFactoryRegistry.create(inventoryItem.type);
        if (!Mappers.buildable.has(item))
            return;

        BuildUtils.enableBuildPreview(
                BuildUtils.getBuildsTypeFromType(inventoryItem.type), inventoryItem.type, getEngine());
    }

    private void updateSlotTexture(Entity slot, boolean disabled) {
        SpriteComponent slotSprite = Mappers.sprite.get(slot);
        if (disabled) {
            slotSprite.setTexturePath("inventory_slot.png");
        } else {
            slotSprite.setTexturePath("inventory_slot_highlight.png");
        }
    }
}

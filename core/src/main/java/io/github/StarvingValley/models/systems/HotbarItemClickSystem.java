package io.github.StarvingValley.models.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import io.github.StarvingValley.models.Mappers;
import io.github.StarvingValley.models.components.ClickedComponent;
import io.github.StarvingValley.models.components.InventoryItemComponent;
import io.github.StarvingValley.models.components.PartOfHotbarComponent;
import io.github.StarvingValley.models.components.SelectedHotbarEntryComponent;
import io.github.StarvingValley.models.components.SpriteComponent;
import io.github.StarvingValley.models.entities.EntityFactoryRegistry;
import io.github.StarvingValley.utils.BuildUtils;
import io.github.StarvingValley.utils.InventoryUtils;

public class HotbarItemClickSystem extends EntitySystem {
    @Override
    public void update(float deltaTime) {
        if (InventoryUtils.isAnyInventoryOpen(getEngine())) {
            return;
        }

        ImmutableArray<Entity> clickedItems = getEngine().getEntitiesFor(
                Family.all(ClickedComponent.class, InventoryItemComponent.class, PartOfHotbarComponent.class).get());

        for (Entity item : clickedItems) {
            InventoryItemComponent itemData = Mappers.inventoryItem.get(item);

            Entity slot = InventoryUtils.getSlot(
                    getEngine(),
                    itemData.inventoryId,
                    itemData.slotX,
                    itemData.slotY);

            boolean wasSelected = Mappers.selectedHotbarItem.has(item);

            if (wasSelected) {
                item.remove(SelectedHotbarEntryComponent.class);
                slot.remove(SelectedHotbarEntryComponent.class);
                BuildUtils.disableBuildPreview(getEngine());
            } else {
                InventoryUtils.unselectSelectedHotbarItems(getEngine());

                item.add(new SelectedHotbarEntryComponent());
                slot.add(new SelectedHotbarEntryComponent());

                triggerBuildMode(item);
            }

            if (slot != null) {
                updateSlotTexture(slot, !wasSelected);
            }
        }
    }

    private void triggerBuildMode(Entity itemEntity) {
        InventoryItemComponent itemData = Mappers.inventoryItem.get(itemEntity);
        if (itemData == null)
            return;

        Entity item = EntityFactoryRegistry.create(itemData.type);
        if (!Mappers.buildable.has(item))
            return;

        BuildUtils.enableBuildPreview(
                BuildUtils.getBuildsTypeFromType(itemData.type),
                itemData.type,
                getEngine());
    }

    private void updateSlotTexture(Entity slot, boolean selected) {
        SpriteComponent sprite = Mappers.sprite.get(slot);
        if (sprite != null) {
            sprite.setTexturePath(selected ? "inventory_slot_highlight.png" : "inventory_slot.png");
        }
    }
}

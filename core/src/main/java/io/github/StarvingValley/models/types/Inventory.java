package io.github.StarvingValley.models.types;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.badlogic.gdx.math.GridPoint2;

public class Inventory {
  public int width = 8;
  public int height = 4;
  public List<InventorySlot> slots = new ArrayList<>();

  public Inventory() {
  }

  public Inventory(List<InventorySlot> slots, int width, int height) {
    this.slots = slots;
    this.width = width;
    this.height = height;
  }

  public List<InventorySlot> findSlotsOfType(PrefabType type) {
    return slots.stream().filter(slot -> slot.getType() == type).collect(Collectors.toList());
  }

  public boolean hasStackOfType(PrefabType type) {
    return slots.stream().anyMatch(slot -> slot.getType() == type);
  }

  public void addOrReplaceSlot(InventorySlot newSlot) {
    removeSlotAt(newSlot.x, newSlot.y);
    slots.add(newSlot);
  }

  public void removeSlotAt(int x, int y) {
    slots.removeIf(slot -> slot.x == x && slot.y == y);
  }

  public boolean addItem(PrefabType type, int quantity) {
    List<InventorySlot> slots = findSlotsOfType(type);
    if (!slots.isEmpty()) {
      slots.get(0).itemStack.quantity += quantity;
      return true;
    }

    GridPoint2 emptySlot = getFirstEmptySlot();
    if (emptySlot != null) {
      this.slots.add(new InventorySlot(type, quantity, emptySlot.x, emptySlot.y));
      return true;
    }

    return false;
  }

  public boolean removeItem(PrefabType type, int quantity) {
    List<InventorySlot> matching = findSlotsOfType(type);

    for (InventorySlot slot : matching) {
      if (slot.getQuantity() > quantity) {
        slot.itemStack.quantity -= quantity;
        return true;
      } else {
        quantity -= slot.getQuantity();
        this.slots.remove(slot);
        if (quantity == 0)
          return true;
      }
    }

    return quantity <= 0;
  }

  public InventorySlot getSlotAt(int x, int y) {
    for (InventorySlot slot : slots) {
      if (slot.x == x && slot.y == y) {
        return slot;
      }
    }
    return null;
  }

  public GridPoint2 getFirstEmptySlot() {
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        if (getSlotAt(x, y) == null) {
          return new GridPoint2(x, y);
        }
      }
    }

    return null;
  }

  // TODO: Remove, only for debugging
  public void printInventory() {
    System.out.println("Inventory:");
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        InventorySlot slot = getSlotAt(x, y);
        if (slot == null) {
          System.out.print("[ EMPTY ] ");
        } else {
          System.out.printf("[ %s(%d) ] ", slot.itemStack.type.name(), slot.getQuantity());
        }
      }
      System.out.println();
    }
  }
}

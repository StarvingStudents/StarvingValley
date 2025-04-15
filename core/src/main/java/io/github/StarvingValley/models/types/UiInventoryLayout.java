package io.github.StarvingValley.models.types;

public class UiInventoryLayout {
    public final float startX;
    public final float startY;
    public final float slotSizePx;
    public final int width;
    public final int height;

    public UiInventoryLayout(float startX, float startY, float slotSizePx, int width, int height) {
        this.startX = startX;
        this.startY = startY;
        this.slotSizePx = slotSizePx;
        this.width = width;
        this.height = height;
    }
}

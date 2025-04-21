package io.github.StarvingValley.models.types;

public enum WorldLayer {
    TERRAIN(0),
    SOIL(1),
    STRUCTURE(2),
    CROP(3),
    CHARACTER(3);

    private final int renderPriority;

    WorldLayer(int renderPriority) {
        this.renderPriority = renderPriority;
    }

    public int getRenderPriority() {
        return renderPriority;
    }
}

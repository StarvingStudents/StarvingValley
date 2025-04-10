package io.github.StarvingValley.models.components;

import com.badlogic.ashley.core.Component;

public class SyncDeletionRequestComponent implements Component {
    public String id;

    public SyncDeletionRequestComponent(String id) {
        this.id = id;
    }
}

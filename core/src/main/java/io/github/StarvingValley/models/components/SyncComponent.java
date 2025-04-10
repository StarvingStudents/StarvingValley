package io.github.StarvingValley.models.components;

import java.util.UUID;

import com.badlogic.ashley.core.Component;

public class SyncComponent implements Component {
    public String id;

    public SyncComponent() {
        setIdToUUID();
    }

    public String setIdToUUID() {
        id = UUID.randomUUID().toString();
        return id;
    }
}

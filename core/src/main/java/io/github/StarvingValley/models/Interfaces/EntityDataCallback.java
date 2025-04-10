package io.github.StarvingValley.models.Interfaces;

import java.util.Map;

import io.github.StarvingValley.models.dto.SyncEntity;

public interface EntityDataCallback {
    void onSuccess(Map<String, SyncEntity> data);
    void onFailure(String errorMessage);
}

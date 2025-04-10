package io.github.StarvingValley.models.Interfaces;

public interface PushCallback {
    void onSuccess();
    void onFailure(String error);
}

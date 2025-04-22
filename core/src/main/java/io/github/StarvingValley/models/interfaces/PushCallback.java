package io.github.StarvingValley.models.interfaces;

public interface PushCallback {
    void onSuccess();
    void onFailure(String error);
}

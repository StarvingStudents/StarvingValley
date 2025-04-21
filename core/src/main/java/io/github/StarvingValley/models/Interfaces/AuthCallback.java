package io.github.StarvingValley.models.interfaces;

public interface AuthCallback {
    void onSuccess();
    void onFailure(String errorMessage);
}

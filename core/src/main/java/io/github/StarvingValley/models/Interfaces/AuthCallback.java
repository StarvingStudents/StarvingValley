package io.github.StarvingValley.models.Interfaces;

public interface AuthCallback {
    void onSuccess();
    void onFailure(String errorMessage);
}

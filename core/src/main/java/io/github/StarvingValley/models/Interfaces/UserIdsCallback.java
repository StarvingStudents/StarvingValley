package io.github.StarvingValley.models.Interfaces;

import java.util.List;
public interface UserIdsCallback {
    void onSuccess(List<String> userIds);
    void onFailure(String error);
}
package io.github.StarvingValley.models.interfaces;

import java.util.List;
public interface UserIdsCallback {
    void onSuccess(List<String> userIds);
    void onFailure(String error);
}
package io.github.StarvingValley.models.Interfaces;

import java.util.List;
import java.util.Map;

public interface IFirebaseRepository {
    // Auth
    void signInWithEmail(String email, String password, AuthCallback callback);
    void signOut();
    boolean isSignedIn();
    String getCurrentUserId();
    void registerWithEmail(String email, String password, AuthCallback callback);
    void registerWithDeviceId(AuthCallback callback);
    void signInWithDeviceId(AuthCallback callback);
    void registerOrSignInWithDeviceId(AuthCallback callback);
    
    boolean pushEntities(Map<String, Object> entityMap, PushCallback callback);
    boolean getAllEntities(EntityDataCallback callback);
    boolean pushEntityDeletions(List<String> entityIds, PushCallback callback);

    boolean getAllUserIds(UserIdsCallback callback);
}

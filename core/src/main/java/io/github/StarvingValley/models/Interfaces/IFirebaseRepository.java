package io.github.StarvingValley.models.Interfaces;

public interface IFirebaseRepository {
    void SubmitUser(String username);

    // Auth
    void signInWithEmail(String email, String password, AuthCallback callback);
    void signOut();
    boolean isSignedIn();
    String getCurrentUserId();
    void registerWithEmail(String email, String password, AuthCallback callback);
}

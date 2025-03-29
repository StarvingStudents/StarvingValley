package io.github.StarvingValley.android;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.Executor;

import io.github.StarvingValley.models.Interfaces.AuthCallback;
import io.github.StarvingValley.models.Interfaces.IFirebaseRepository;
import io.github.StarvingValley.config.Config;

public class FirebaseRepository implements IFirebaseRepository {

    FirebaseDatabase _database;
    DatabaseReference _users;
    FirebaseAuth _auth;

    public FirebaseRepository() {
        _database = FirebaseDatabase.getInstance(Config.FIREBASE_DATABASE_URL);
        _users = _database.getReference("users");
        _auth = FirebaseAuth.getInstance();
    }

    @Override
    public void SubmitUser(String username) {
        _users.push().setValue(username);
    }

    @Override
    public void signInWithEmail(String email, String password, AuthCallback callback) {
        _auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    String errorMessage = task.getException() != null ? task.getException().getMessage() : "Unknown error";
                    callback.onFailure(errorMessage);
                }
            });
    }

    @Override
    public void signOut() {
        _auth.signOut();
    }

    @Override
    public boolean isSignedIn() {
        return _auth.getCurrentUser() != null;
    }

    @Override
    public String getCurrentUserId() {
        return _auth.getCurrentUser() != null ? _auth.getCurrentUser().getUid() : null;
    }

    @Override
    public void registerWithEmail(String email, String password, AuthCallback callback) {
        _auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    String errorMessage = task.getException() != null ? task.getException().getMessage() : "Unknown error";
                    callback.onFailure(errorMessage);
                }
            });
    }
}

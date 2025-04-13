package io.github.StarvingValley.android;

import com.badlogic.gdx.Gdx;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import io.github.StarvingValley.config.Config;
import io.github.StarvingValley.models.Interfaces.AuthCallback;
import io.github.StarvingValley.models.Interfaces.EntityDataCallback;
import io.github.StarvingValley.models.Interfaces.IFirebaseRepository;
import io.github.StarvingValley.models.Interfaces.PushCallback;
import io.github.StarvingValley.models.Interfaces.UserIdsCallback;
import io.github.StarvingValley.models.dto.SyncEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FirebaseRepository implements IFirebaseRepository {

  FirebaseDatabase _database;
  DatabaseReference _users;
  DatabaseReference _entities;
  FirebaseAuth _auth;

  public FirebaseRepository() {
    _database = FirebaseDatabase.getInstance(Config.FIREBASE_DATABASE_URL);
    _users = _database.getReference("userEntities");
    _auth = FirebaseAuth.getInstance();
  }

  public boolean initUserEntityReference() {
    if (_entities != null)
      return true;

    String userId = getCurrentUserId();
    if (userId == null)
      return false;

    _entities = _database.getReference("userEntities").child(getCurrentUserId());

    return true;
  }

  @Override
  public void signInWithEmail(String email, String password, AuthCallback callback) {
    _auth
        .signInWithEmailAndPassword(email, password)
        .addOnCompleteListener(
            task -> {
              if (task.isSuccessful()) {
                callback.onSuccess();
              } else {
                String errorMessage = task.getException() != null
                    ? task.getException().getMessage()
                    : "Unknown error";
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
    _auth
        .createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener(
            task -> {
              if (task.isSuccessful()) {
                callback.onSuccess();
              } else {
                String errorMessage = task.getException() != null
                    ? task.getException().getMessage()
                    : "Unknown error";
                callback.onFailure(errorMessage);
              }
            });
  }

  @Override
  public void registerWithDeviceId(AuthCallback callback) {
    String deviceId = DeviceIdManager.getOrCreateDeviceId();
    String email = getDeviceIdEmail(deviceId);
    String password = deviceId;

    registerWithEmail(email, password, callback);
  }

  @Override
  public void signInWithDeviceId(AuthCallback callback) {
    String deviceId = DeviceIdManager.getOrCreateDeviceId();
    String email = getDeviceIdEmail(deviceId);
    String password = deviceId;

    signInWithEmail(email, password, callback);
  }

  private String getDeviceIdEmail(String deviceId) {
    return deviceId + "@device.local";
  }

  @Override
  public void registerOrSignInWithDeviceId(AuthCallback callback) {
    registerWithDeviceId(
        new AuthCallback() {
          @Override
          public void onSuccess() {
            callback.onSuccess();
          }

          @Override
          public void onFailure(String errorMessage) {
            if (errorMessage == null || !errorMessage.contains("email address is already in use")) {
              System.out.println(errorMessage == null ? "Couldn't sign in" : errorMessage);
              callback.onFailure(errorMessage);
              return;
            }

            signInWithDeviceId(
                new AuthCallback() {
                  @Override
                  public void onSuccess() {
                    callback.onSuccess();
                  }

                  @Override
                  public void onFailure(String errorMessage) {
                    System.out.println("Couldn't sign in: " + errorMessage);
                    callback.onFailure(errorMessage);
                  }
                });
          }
        });
  }

  @Override
  public boolean pushEntities(Map<String, Object> entityMap, PushCallback callback) {
    if (!initUserEntityReference())
      return false;

    ExecutorService backgroundExecutor = Executors.newSingleThreadExecutor();

    _entities
        .updateChildren(entityMap)
        .addOnSuccessListener(
            backgroundExecutor,
            unused -> {
              Gdx.app.postRunnable(
                  () -> {
                    callback.onSuccess();
                  });
            })
        .addOnFailureListener(
            e -> {
              System.err.println("Failed to sync: " + e.getMessage());

              Gdx.app.postRunnable(
                  () -> {
                    callback.onFailure(e.getMessage());
                  });
            });

    return true;
  }

  @Override
  public boolean pushEntityDeletions(List<String> entityIds, PushCallback callback) {
    if (!initUserEntityReference())
      return false;

    if (entityIds == null || entityIds.isEmpty())
      return true;

    ExecutorService backgroundExecutor = Executors.newSingleThreadExecutor();

    Map<String, Object> deletions = new HashMap<>();
    for (String id : entityIds) {
      deletions.put(id, null);
    }

    _entities
        .updateChildren(deletions)
        .addOnSuccessListener(
            backgroundExecutor,
            unused -> {
              Gdx.app.postRunnable(
                  () -> {
                    if (callback != null) {
                      callback.onSuccess();
                    }
                  });
            })
        .addOnFailureListener(
            e -> {
              System.err.println("Failed to delete entities: " + e.getMessage());

              Gdx.app.postRunnable(
                  () -> {
                    if (callback != null) {
                      callback.onFailure(e.getMessage());
                    }
                  });
            });

    return true;
  }

  @Override
  public boolean getAllEntities(EntityDataCallback callback) {
    if (!initUserEntityReference())
      return false;

    _entities.addListenerForSingleValueEvent(
        new ValueEventListener() {
          @Override
          public void onDataChange(DataSnapshot snapshot) {
            Map<String, SyncEntity> result = new HashMap<>();
            for (DataSnapshot child : snapshot.getChildren()) {
              SyncEntity entity = child.getValue(SyncEntity.class);
              result.put(child.getKey(), entity);
            }
            callback.onSuccess(result);
          }

          @Override
          public void onCancelled(DatabaseError error) {
            callback.onFailure(error.getMessage());
          }
        });

    return true;
  }

    @Override
    public boolean getEntitiesForUser(String userId, EntityDataCallback callback) {
        if (userId == null || userId.isEmpty()) {
            callback.onFailure("Invalid user ID");
            return false;
        }

        DatabaseReference userEntityRef = _database.getReference("userEntities").child(userId);

        userEntityRef.addListenerForSingleValueEvent(
            new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    Map<String, SyncEntity> result = new HashMap<>();
                    for (DataSnapshot child : snapshot.getChildren()) {
                        SyncEntity entity = child.getValue(SyncEntity.class);
                        result.put(child.getKey(), entity);
                    }
                    callback.onSuccess(result);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    callback.onFailure(error.getMessage());
                }
            });

        return true;
    }

  @Override
  public boolean getAllUserIds(UserIdsCallback callback) {
    _users.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot snapshot) {
        List<String> userIds = new ArrayList<>();
        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
          userIds.add(childSnapshot.getKey());
        }
        callback.onSuccess(userIds);
      }

      @Override
      public void onCancelled(DatabaseError error) {
        callback.onFailure(error.getMessage());
      }
    });
    return true;
  }
}

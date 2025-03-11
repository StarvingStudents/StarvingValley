package io.github.StarvingValley.android;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import io.github.StarvingValley.models.Interfaces.IFirebaseRepository;
import io.github.StarvingValley.config.Config;

public class FirebaseRepository implements IFirebaseRepository {

    FirebaseDatabase _database;
    DatabaseReference _users;

    public FirebaseRepository() {
        _database = FirebaseDatabase.getInstance(Config.FIREBASE_DATABASE_URL);
        _users = _database.getReference("users");
    }

    @Override
    public void SubmitUser(String username) {
        _users.push().setValue(username);
    }
}

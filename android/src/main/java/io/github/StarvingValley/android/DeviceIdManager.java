package io.github.StarvingValley.android;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.UUID;

//Temporary dev helper until we have user-ids from signin/login screen
public class DeviceIdManager {
  private static final String PREFS_NAME = "app_prefs";
  private static final String KEY_DEVICE_GUID = "device_guid";

  public static Context context;

  public static String getOrCreateDeviceId() {
    SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    String id = prefs.getString(KEY_DEVICE_GUID, null);

    if (id == null) {
      id = UUID.randomUUID().toString();
      prefs.edit().putString(KEY_DEVICE_GUID, id).apply();
    }

    return id;
  }
}

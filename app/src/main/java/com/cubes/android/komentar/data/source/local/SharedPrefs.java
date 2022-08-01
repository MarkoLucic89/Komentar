package com.cubes.android.komentar.data.source.local;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefs {

    private final String NOTIFICATION = "prefs_notification";

    private static SharedPrefs instance;
    private SharedPreferences preferences;


    private SharedPrefs(Activity activity) {
        preferences = activity.getPreferences(Context.MODE_PRIVATE);
    }

    public synchronized static SharedPrefs getInstance(Activity activity) {

        if (instance == null) {
            instance = new SharedPrefs(activity);
        }

        return instance;
    }

    public boolean isNotificationOn() {
        return preferences.getBoolean(NOTIFICATION, false);
    }

    public void setNotificationStatus(boolean isOn) {
        preferences.edit().putBoolean(NOTIFICATION, isOn).apply();
    }
}


package com.cubes.android.komentar.data.source.local;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.cubes.android.komentar.data.model.NewsCommentVote;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SharedPrefs {

    private final String NOTIFICATION_KEY = "prefs_notification";
    private static final String COMMENTS_KEY = "list_key";


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
        return preferences.getBoolean(NOTIFICATION_KEY, false);
    }

    public void setNotificationStatus(boolean isOn) {
        preferences.edit().putBoolean(NOTIFICATION_KEY, isOn).apply();
    }

//    public void writeListInPref(List<NewsCommentVote> list) {
//        Gson gson = new Gson();
//        String jsonString = gson.toJson(list);
//
//        preferences.edit().putString(COMMENTS_KEY, jsonString);
//        preferences.edit().apply();
//    }
//
//    public List<NewsCommentVote> readListFromPref() {
//        String jsonString = preferences.getString(COMMENTS_KEY, "");
//
//        Gson gson = new Gson();
//        Type type = new TypeToken<ArrayList<NewsCommentVote>>() {
//        }.getType();
//        List<NewsCommentVote> list = gson.fromJson(jsonString, type);
//        return list;
//    }
}


package com.cubes.android.komentar.data.source.local;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.cubes.android.komentar.data.model.domain.NewsCommentVote;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SharedPrefs {

    private static final String LIST_KEY = "list_key";
    private static final String NOTIFICATION_KEY = "prefs_notification";

    public static boolean isNotificationOn(Activity activity) {

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(activity);

        return pref.getBoolean(NOTIFICATION_KEY, false);
    }

    public static void setNotificationStatus(Activity activity, boolean isOn) {

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(activity);

        pref.edit().putBoolean(NOTIFICATION_KEY, isOn).apply();
    }

    public static void writeListInPref(Activity activity, List<NewsCommentVote> list) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(list);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(LIST_KEY, jsonString);
        editor.apply();
    }

    public static List<NewsCommentVote> readListFromPref(Activity activity) {

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(activity);
        String jsonString = pref.getString(LIST_KEY, "");

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<NewsCommentVote>>() {
        }.getType();
        List<NewsCommentVote> list = gson.fromJson(jsonString, type);
        return list;
    }

}

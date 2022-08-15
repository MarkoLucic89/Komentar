package com.cubes.android.komentar.data.source.local;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.cubes.android.komentar.data.model.NewsCommentVote;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CommentPrefs {

    private static final String LIST_KEY = "list_key";

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

package com.example.oorstory.model;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferenceUtil {
    private static Context context;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private SharedPreferenceUtil(Context context) {
        preferences = context.getSharedPreferences("Application", Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    private static SharedPreferenceUtil instance;
    public static SharedPreferenceUtil getInstance(Context context) {
        if (instance == null)
            instance = new SharedPreferenceUtil(context);

        return instance;
    }

    public static final String userid = "userid";
    public static final String storyid = "storyid";

    public void setUserid(String id) {
        editor.putString(userid, id);
        editor.apply();
    }
    public String getUserid() {
        return preferences.getString(userid, "DEFAULT");
    }
    public void clearUserid() {
        editor.remove(userid);
        editor.apply();
    }

    public void setStoryid(String id) {
        editor.putString(storyid, id);
        editor.apply();
    }
    public String getStoryid() {
        return preferences.getString(storyid, "DEFAULT");
    }
    public void clearStoryid() {
        editor.remove(storyid);
        editor.apply();
    }
}
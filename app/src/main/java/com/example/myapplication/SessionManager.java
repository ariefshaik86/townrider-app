package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void createSession(int id, String name) {
        editor.putBoolean("isLoggedIn", true);
        editor.putInt("user_id", id);
        editor.putString("name", name);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return prefs.getBoolean("isLoggedIn", false);
    }

    public String getUserName() {
        return prefs.getString("name", "Guest User");
    }

    public void logout() {
        editor.clear();
        editor.apply();
    }
}
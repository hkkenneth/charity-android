package com.winginno.charitynow;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;

import android.util.Log;

public class SettingsStorage {
    private static final String PREFS_NAME = "SETTINGS";
    private static final String KEY = "settings";
    static final String TAG = "CharityNow";
    private SharedPreferences appSharedPrefs;
    private SharedPreferences.Editor prefsEditor;

    public SettingsStorage() {
        Context context = ApplicationContextProvider.getContext();

        this.appSharedPrefs = context.getSharedPreferences(PREFS_NAME, Activity.MODE_PRIVATE);
        this.prefsEditor = appSharedPrefs.edit();
    }

    public Settings get() {
        String jsonString = appSharedPrefs.getString(KEY, "");
        Log.i(TAG, "json get: " + jsonString);
        if (!jsonString.isEmpty()) {
            Gson gson = new Gson();
            try {
                Settings settings = gson.fromJson(jsonString, Settings.class);
                return settings;
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
        return Settings.getDefaultInstance();
    }

    public void set(Settings data) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(data);
        Log.i(TAG, "json set: " + jsonString);
        prefsEditor.putString(KEY, jsonString).commit();
    }

}

package com.winginno.charitynow;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class EventsStorage {
    private static final String PREFS_NAME = "EVENTS";
    private static final String KEY = "eventApi";
    private SharedPreferences appSharedPrefs;
    private SharedPreferences.Editor prefsEditor;

    public EventsStorage() {
        Context context = ApplicationContextProvider.getContext();

        this.appSharedPrefs = context.getSharedPreferences(PREFS_NAME, Activity.MODE_PRIVATE);
        this.prefsEditor = appSharedPrefs.edit();
    }

    public String get() {
        return appSharedPrefs.getString(KEY, "");
    }

    public void set(String data) {
        prefsEditor.putString(KEY, data).commit();
    }

}

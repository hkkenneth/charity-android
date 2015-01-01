package com.winginno.charitynow;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.Set;
import java.util.HashSet;

public class NotiHistoryStorage {
    private static final String PREFS_NAME = "HISTORY";
    private static final String KEY = "notiHistory";
    private SharedPreferences appSharedPrefs;
    private SharedPreferences.Editor prefsEditor;

    public NotiHistoryStorage() {
        Context context = ApplicationContextProvider.getContext();

        this.appSharedPrefs = context.getSharedPreferences(PREFS_NAME, Activity.MODE_PRIVATE);
        this.prefsEditor = appSharedPrefs.edit();
    }

    public boolean isInHistory(String id) {
        Set<String> set = appSharedPrefs.getStringSet(KEY, new HashSet<String>());
        return set.contains(id);
    }

    public void addToHistory(String id) {
        Set<String> set = (Set<String>) ((HashSet<String>)appSharedPrefs.getStringSet(KEY, new HashSet<String>())).clone();
        set.add(id);
        prefsEditor.putStringSet(KEY, set).commit();
    }

    public void clear() {
        prefsEditor.putStringSet(KEY, new HashSet<String>()).commit();
    }

}

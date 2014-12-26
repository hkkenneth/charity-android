package com.winginno.charitynow;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import android.util.Log;

public class GcmTokenStorage {

    private static final String PREFS_NAME = "GCM_TOKEN_STORAGE";
    private static final String KEY = "registrationId";
    private static final String VERSION_KEY = "appVersion";
    static final String TAG = "CharityNow";
    private SharedPreferences appSharedPrefs;
    private SharedPreferences.Editor prefsEditor;

    public GcmTokenStorage() {
        Context context = ApplicationContextProvider.getContext();

        this.appSharedPrefs = context.getSharedPreferences(PREFS_NAME, Activity.MODE_PRIVATE);
        this.prefsEditor = appSharedPrefs.edit();
    }

    /**
     * Gets the current registration ID for application on GCM service, if there is one.
     * <p>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     *         registration ID.
     */
    public String get() {
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = appSharedPrefs.getInt(VERSION_KEY, Integer.MIN_VALUE);
        if (registeredVersion == Utils.getAppVersion()) {
            return appSharedPrefs.getString(KEY, "");
        } else {
            Log.i(TAG, "App version changed.");
            return "";
        }
    }

    public String getIgnoreVersion() {
        return appSharedPrefs.getString(KEY, "");
    }

    /**
     * Stores the registration ID and the app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param data registration ID
     */
    public void set(String data) {
        prefsEditor.putString(KEY, data).putInt(VERSION_KEY, Utils.getAppVersion()).commit();
    }
}

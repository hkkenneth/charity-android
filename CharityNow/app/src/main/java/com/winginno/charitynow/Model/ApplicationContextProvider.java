package com.winginno.charitynow;

import android.app.Application;
import android.content.Context;

import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.analytics.GoogleAnalytics;

import java.util.HashMap;

import android.util.Log;

/**
 * Reference: http://www.myandroidsolutions.com/2013/04/27/android-get-application-context/
 */
public class ApplicationContextProvider extends Application {
 
    /**
     * Keeps a reference of the application context
     */
    private static Context sContext;
    private static HashMap<TrackerName, Tracker> sTrackers = new HashMap<TrackerName, Tracker>();

    static final String PROPERTY_ID_DEV = "UA-58092774-1";
    static final String PROPERTY_ID_PROD = "UA-58092774-2";

    static final String TAG = "CharityNow";

    public enum TrackerName {
        APP_TRACKER, // Tracker used only in this app.
    }
 
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "ApplicationContextProvider onCreate");
        sContext = getApplicationContext();
 
    }
 
    /**
     * Returns the application context
     *
     * @return application context
     */
    public static Context getContext() {
        return sContext;
    }
 
    synchronized static Tracker getTracker(TrackerName trackerId) {
        if (!sTrackers.containsKey(trackerId)) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(getContext());
            Tracker t = analytics.newTracker(PROPERTY_ID_DEV);
            sTrackers.put(trackerId, t);
        }
        return sTrackers.get(trackerId);
    }

}

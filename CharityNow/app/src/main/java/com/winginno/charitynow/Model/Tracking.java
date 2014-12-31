package com.winginno.charitynow;

import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.analytics.HitBuilders;

public class Tracking {

    public static void trackPageView(String screenName)
    {
        Tracker t = ApplicationContextProvider.getTracker(ApplicationContextProvider.TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName(screenName);

        // Send a screen view.
        t.send(new HitBuilders.AppViewBuilder().build());
    }

    public static void trackEvent(String category, String action, String label)
    {
        // Get tracker.
        Tracker t = ApplicationContextProvider.getTracker(ApplicationContextProvider.TrackerName.APP_TRACKER);

        t.send(new HitBuilders.EventBuilder()
            .setCategory(category)
            .setAction(action)
            .setLabel(label)
            .build());
    }

}

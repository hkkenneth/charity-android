package com.winginno.charitynow;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import android.util.Log;

public class EventsFactory {

    static final String TAG = "CharityNow";

    public static ArrayList<Event> getEvents()
    {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();

        EventsStorage eventsStorage = new EventsStorage();
        String jsonString = eventsStorage.get();
        try {
            EventsJsonWrapper eventsWrapper = gson.fromJson(jsonString, EventsJsonWrapper.class);
            return eventsWrapper.getEvents();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return new ArrayList<Event>();
    }
}

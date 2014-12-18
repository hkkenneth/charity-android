package com.winginno.charitynow;

import android.content.Context;
import android.content.Intent;
import android.app.PendingIntent;
import android.app.AlarmManager;
import android.util.Log;

import java.util.Calendar;

public class DataFetchTask {

    static final String TAG = "CharityNow";
    static final int TASK_FREQUENCY = 10000;
    static final int REQUEST_CODE = 19881988;

    private static Intent getIntent()
    {
        Context context = ApplicationContextProvider.getContext();
        return new Intent(context, DataFetchTaskReceiver.class);
    }

    public static void create()
    {
        Log.i(TAG, "DataFetchTask create()");
        Context context = ApplicationContextProvider.getContext();

        Intent intent = getIntent();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.MINUTE, 1);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), TASK_FREQUENCY, pendingIntent);
    }

    /**
     * Reference: http://stackoverflow.com/questions/4556670/how-to-check-if-alarmmamager-already-has-an-alarm-set
     */
    public static boolean isActive()
    {
        Log.i(TAG, "DataFetchTask isActive()");
        Context context = ApplicationContextProvider.getContext();
        return (PendingIntent.getBroadcast(context, REQUEST_CODE, getIntent(), PendingIntent.FLAG_NO_CREATE) != null);
    }

    public static void cancel()
    {
        Log.i(TAG, "DataFetchTask cancel()");
        Context context = ApplicationContextProvider.getContext();

        Intent intent = getIntent();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
            Log.i(TAG, "DataFetchTask cancel() alarm canceled");
        }
    }
}

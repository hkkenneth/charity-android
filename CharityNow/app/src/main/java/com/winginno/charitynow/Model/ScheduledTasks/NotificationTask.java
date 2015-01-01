package com.winginno.charitynow;

import android.content.Context;
import android.content.Intent;
import android.app.PendingIntent;
import android.app.AlarmManager;
import android.util.Log;

import java.util.Calendar;

public class NotificationTask {

    static final String TAG = "CharityNow";
    static final int TASK_FREQUENCY = 14400000;
    static final int REQUEST_CODE = 19881989;

    private static Intent getIntent()
    {
        Context context = ApplicationContextProvider.getContext();
        return new Intent(context, NotificationTaskReceiver.class);
    }

    public static void create()
    {
        Log.i(TAG, "NotificationTask create()");
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
        Log.i(TAG, "NotificationTask isActive()");
        Context context = ApplicationContextProvider.getContext();
        return (PendingIntent.getBroadcast(context, REQUEST_CODE, getIntent(), PendingIntent.FLAG_NO_CREATE) != null);
    }

    public static void cancel()
    {
        Log.i(TAG, "NotificationTask cancel()");
        Context context = ApplicationContextProvider.getContext();

        Intent intent = getIntent();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
            Log.i(TAG, "NotificationTask cancel() alarm canceled");
        }
    }
}

package com.winginno.charitynow.Model.ScheduledTasks;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.winginno.charitynow.DataFetchTask;
import com.winginno.charitynow.GcmIntentService;
import com.winginno.charitynow.R;

public class BootReceiver extends BroadcastReceiver {

    static final String TAG = "BootReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "BootReceiver onReceive() setAlarm");
        DataFetchTask.create();
    }

}

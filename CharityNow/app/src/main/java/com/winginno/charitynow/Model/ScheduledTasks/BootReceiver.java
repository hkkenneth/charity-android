package com.winginno.charitynow.Model.ScheduledTasks;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.winginno.charitynow.DataFetchTask;

public class BootReceiver extends BroadcastReceiver {

    static final String TAG = "BootReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "BootReceiver onReceive() setAlarm");
        DataFetchTask.create();
    }

}

/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.winginno.charitynow;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.net.Uri;

import java.util.Date;

/**
 * This {@code IntentService} does the actual handling of the GCM message.
 * {@code GcmBroadcastReceiver} (a {@code WakefulBroadcastReceiver}) holds a
 * partial wake lock for this service while the service does its work. When the
 * service is finished, it calls {@code completeWakefulIntent()} to release the
 * wake lock.
 */
public class GcmIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;

    public static final String PROPERTY_TITLE = "title";
    public static final String PROPERTY_DESCRIPTION = "description";
    public static final String PROPERTY_LINK = "link";
    public static final String PROPERTY_LOCAL_NOTI = "localNoti";
    public static final String PROPERTY_FORCE_CLEAR_EVENT_CACHE = "forceClearEventCache";


    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public GcmIntentService() {
        super("GcmIntentService");
    }
    static final String TAG = "CharityNow";

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "GcmIntentService.onHandleIntent");
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM will be
             * extended in the future with new message types, just ignore any message types you're
             * not interested in, or that you don't recognize.
             */
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                // sendNotification("Send error: " + extras.toString());
                Log.i(TAG, "Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                // sendNotification("Deleted messages on server: " + extras.toString());
                Log.i(TAG, "Deleted messages on server: " + extras.toString());
            // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                // This loop represents the service doing some work.
                // for (int i = 0; i < 5; i++) {
                //     Log.i(TAG, "Working... " + (i + 1)
                //             + "/5 @ " + SystemClock.elapsedRealtime());
                //     try {
                //         Thread.sleep(5000);
                //     } catch (InterruptedException e) {
                //     }
                // }
                // Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
                // Post notification of received message.
                boolean forceClearEventCache = Boolean.parseBoolean((String) extras.get(PROPERTY_FORCE_CLEAR_EVENT_CACHE));
                if (forceClearEventCache) {
                    EventsStorage eventsStorage = new EventsStorage();
                    eventsStorage.set("");
                    Log.i(TAG, "Received: cleared event cache");
                    return;
                }

                SettingsStorage settingsStorage = new SettingsStorage();
                Settings settings = settingsStorage.get();
                boolean callLocalNoti = Boolean.parseBoolean((String) extras.get(PROPERTY_LOCAL_NOTI));
                if (settings.isOtherNotiEnabled() || callLocalNoti) {
                    sendNotification(extras);
                }
                Log.i(TAG, "Received: " + extras.toString());
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(Bundle extras) {
        Log.i(TAG, "GcmIntentService.sendNotification");

        boolean callLocalNoti = Boolean.parseBoolean((String) extras.get(PROPERTY_LOCAL_NOTI));
        if (callLocalNoti) {
            Log.i(TAG, "sendNotification() trigger local noti instead");
            NotificationTaskReceiver notificationTaskReceiver = new NotificationTaskReceiver();
            notificationTaskReceiver.onReceive(null, null);
            return;
        }

        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent openUrlIntent = new Intent(Intent.ACTION_VIEW);
        openUrlIntent.setData(Uri.parse((String) extras.get(PROPERTY_LINK)));
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, openUrlIntent, 0);


        String title = (String) extras.get(PROPERTY_TITLE);
        String msg = (String) extras.get(PROPERTY_DESCRIPTION);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
            .setSmallIcon(R.drawable.ic_noti)
            .setContentTitle(title)
            .setContentText(msg)
            .setAutoCancel(true);

        Date now = new Date();
        int notiId = (int) (now.getTime() % 1000000);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(notiId, mBuilder.build());
    }
}

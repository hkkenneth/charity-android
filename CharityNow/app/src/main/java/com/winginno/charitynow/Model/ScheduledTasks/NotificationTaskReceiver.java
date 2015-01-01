package com.winginno.charitynow;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Date;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import java.util.concurrent.TimeUnit;

public class NotificationTaskReceiver extends BroadcastReceiver {

    static final String TAG = "CharityNow";
    static final int REQUEST_CODE = 19891989;
    static final int NOTIFICATION_WITHIN_HOURS = 18;

    @Override
    public void onReceive(Context arg0, Intent arg1) {
        Log.i(TAG, "NotificationTaskReceiver onReceive()");
        Context appContext = ApplicationContextProvider.getContext();

        NotificationManager mNotificationManager = (NotificationManager) appContext.getSystemService(Context.NOTIFICATION_SERVICE);
        EventsStorage eventsStorage = new EventsStorage();
        SettingsStorage settingsStorage = new SettingsStorage();
        Settings settings = settingsStorage.get();
        NotiHistoryStorage notiHistoryStorage = new NotiHistoryStorage();
        int i = 0;
        for (Event event : EventsFactory.getEvents()) {
            Date now = new Date();
            if ((event.getEndDate() != null) && (event.getEndDate().before(now))) {
                continue;
            }

            if (event.getEndDate().getTime() - now.getTime() > (TimeUnit.HOURS.toMillis(NOTIFICATION_WITHIN_HOURS))) {
                continue;
            }

            if (!(settings.isFlagSaleNotiHkEnabled() && event.isInHongKong()) &&
                !(settings.isFlagSaleNotiKowloonEnabled() && event.isInKowloon()) &&
                !(settings.isFlagSaleNotiNtEnabled() && event.isInNt())) {
                continue;
            }

            if (notiHistoryStorage.isInHistory(event.getId())) {
                continue;
            }

            Intent openMainActivityIntent = new Intent(appContext, MainActivity.class);
            openMainActivityIntent.putExtra("eventId", event.getId());

            String title = event.getStartDateString() + event.getRegion() + appContext.getString(R.string.noti_text_suffix);
            String orgName = event.getName();

            // adding i to request code is necessary to make sure these do not happen:
            // ref: http://stackoverflow.com/questions/3140072/android-keeps-caching-my-intents-extras-how-to-declare-a-pending-intent-that-ke
            // ref 2: http://stackoverflow.com/questions/6384524/android-getting-same-value-from-intent-extras
            PendingIntent contentIntent = PendingIntent.getActivity(appContext, REQUEST_CODE + i, openMainActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(appContext)
                .setSmallIcon(R.drawable.ic_noti)
                .setContentTitle(orgName)
                .setContentText(title)
                .setAutoCancel(true);

            mBuilder.setContentIntent(contentIntent);
            mNotificationManager.notify(GcmIntentService.NOTIFICATION_ID + i, mBuilder.build());

            notiHistoryStorage.addToHistory(event.getId());

            i++;
        }
    }

}

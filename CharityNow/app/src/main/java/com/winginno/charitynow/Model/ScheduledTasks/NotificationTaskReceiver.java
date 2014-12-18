package com.winginno.charitynow;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import java.text.SimpleDateFormat;
import java.text.ParsePosition;
import java.util.Locale;
import java.util.Date;

public class NotificationTaskReceiver extends BroadcastReceiver {

    static final String TAG = "CharityNow";
    static final int REQUEST_CODE = 19891989;

    @Override
    public void onReceive(Context arg0, Intent arg1) {
        Log.i(TAG, "NotificationTaskReceiver onReceive()");
        Context appContext = ApplicationContextProvider.getContext();

        NotificationManager mNotificationManager = (NotificationManager) appContext.getSystemService(Context.NOTIFICATION_SERVICE);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        EventsStorage eventsStorage = new EventsStorage();
        try {
            JSONObject data = new JSONObject(eventsStorage.get());

            JSONArray values = data.getJSONArray("od1");
            for(int i = 0 ; i < values.length(); i++) {
                JSONObject obj = values.getJSONObject(i);
                // Log.i(TAG, obj.getString("Headline"));
                String title = obj.getString("Headline");
                String notiUrl = obj.getString("Media_Caption");
                String startDate = obj.getString("Start_Date");
                ParsePosition pp = new ParsePosition(0);
                Date eventDate = sdf.parse(startDate, pp);
                // Log.i(TAG, new Integer(pp.getIndex()).toString());
                // Log.i(TAG, new Integer(pp.getErrorIndex()).toString());
                if ((eventDate != null) && (eventDate.before(new Date()))) {
                    continue;
                }

                Intent openUrlIntent = new Intent(Intent.ACTION_VIEW);
                openUrlIntent.setData(Uri.parse(notiUrl));
                PendingIntent contentIntent = PendingIntent.getActivity(appContext, REQUEST_CODE, openUrlIntent, 0);

                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(appContext)
                    .setSmallIcon(R.drawable.ic_stat_gcm)
                    .setContentTitle(title)
                    .setStyle(new NotificationCompat.BigTextStyle()
                    .bigText(title))
                    .setContentText(title)
                    .setAutoCancel(true);

                mBuilder.setContentIntent(contentIntent);
                mNotificationManager.notify(GcmIntentService.NOTIFICATION_ID + i, mBuilder.build());
                Log.i(TAG, "NotificationTaskReceiver created notification");
            }
        } catch (JSONException e) {
            Log.i(TAG, "json exception");
        }
    }

}

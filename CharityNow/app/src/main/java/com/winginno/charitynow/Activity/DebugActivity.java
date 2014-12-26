package com.winginno.charitynow;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;

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

import android.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

import java.util.ArrayList;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class DebugActivity extends ActionBarActivity {

    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    /**
     * Substitute you own sender ID here. This is the project number you got
     * from the API Console, as described in "Getting Started."
     */
    String SENDER_ID = "PLACE_HOLDER";

    /**
     * Tag used on log messages.
     */
    static final String TAG = "CharityNow";

    TextView mDisplay;
    GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();
    Context context;

    String regid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "on Create.");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDisplay = (TextView) findViewById(R.id.display);

        context = getApplicationContext();

        // Check device for Play Services APK.
        // if (checkPlayServices()) {
        //     // If this check succeeds, proceed with normal processing.
        //     // Otherwise, prompt user to get valid Play Services APK.
        //     gcm = GoogleCloudMessaging.getInstance(this);
        //     regid = getRegistrationId(context);

        //     if (regid.isEmpty()) {
        //         registerInBackground();
        //     }
        // } else {
        //     Log.i(TAG, "No valid Google Play Services APK found.");
        // }

        if (!DataFetchTask.isActive()) {
            DataFetchTask.create();
        }
        if (!NotificationTask.isActive()) {
            NotificationTask.create();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_debug, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(context, "settings", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.action_feedback) {
            Intent intent = new Intent(context, FeedbackActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_fetchApi) {
            Toast.makeText(context, "fetch api", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.action_reloadList) {
            Toast.makeText(context, "reload list", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // You need to do the Play Services APK check here too.
    @Override
    protected void onResume() {
        super.onResume();
        // checkPlayServices();
        if (!DataFetchTask.isActive()) {
            DataFetchTask.create();
        }
        if (!NotificationTask.isActive()) {
            NotificationTask.create();
        }
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                Log.i(TAG, "This device is not supported 1.");
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        Log.i(TAG, "This device is supported.");
        return true;
    }

    /**
     * Registers the application with GCM servers asynchronously.
     * <p>
     * Stores the registration ID and the app versionCode in the application's
     * shared preferences.
     */
    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            // This gets executed at background and return a string result for onPostExecute() to use
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    Log.i(TAG, "About to call gcm.register");
                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;
                    Log.i(TAG, msg);

                    // You should send the registration ID to your server over HTTP, so it
                    // can use GCM/HTTP or CCS to send messages to your app.
                    sendRegistrationIdToBackend();

                    // For this demo: we don't need to send it because the device will send
                    // upstream messages to a server that echo back the message using the
                    // 'from' address in the message.

                    // Persist the regID - no need to register again.
                    storeRegistrationId(context, regid);
                } catch (IOException ex) {
                    msg = "IO Error :" + ex.getMessage();
                    Log.e(TAG, msg);
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                mDisplay.append(msg + "\n");
            }
        }.execute(null, null, null);
    }

    // Send an upstream message.
    public void onClick(final View view) {

        if (view == findViewById(R.id.send)) {
            new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... params) {
                    String msg = "";
                    try {
                        Bundle data = new Bundle();
                        data.putString("my_message", "Hello World");
                        data.putString("my_action", "com.google.android.gcm.demo.app.ECHO_NOW");
                        String id = Integer.toString(msgId.incrementAndGet());
                        gcm.send(SENDER_ID + "@gcm.googleapis.com", id, data);
                        msg = "Sent message";
                    } catch (IOException ex) {
                        msg = "onClick IO Error :" + ex.getMessage();
                    }
                    return msg;
                }

                @Override
                protected void onPostExecute(String msg) {
                    mDisplay.append(msg + "\n");
                }
            }.execute(null, null, null);
        } else if (view == findViewById(R.id.clear)) {
            final ArrayList<String> list = new ArrayList<String>();

            for (Event e : EventsFactory.getEvents()) {
                if ((e.getStartDate() != null) && (e.getStartDate().before(new Date()))) {
                    continue;
                }
                Log.i(TAG, e.getName());
                Log.i(TAG, e.getMembership());

                list.add(e.getName());
            }

            final ListView listview = (ListView) findViewById(R.id.event_listview);

            final ArrayAdapter<Event> adapter = new EventViewAdapter(this, android.R.layout.simple_list_item_1, EventsFactory.getEvents());
            listview.setAdapter(adapter);

            final Context activityContext = context;
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

              @Override
              public void onItemClick(AdapterView<?> parent, final View view,
                  int position, long id) {

                Toast.makeText(activityContext, "hi", Toast.LENGTH_SHORT).show();
              }

            });

            listview.requestLayout();
        } else if (view == findViewById(R.id.btn_feedback_trigger)) {
            Intent intent = new Intent(context, FeedbackActivity.class);
            startActivity(intent);
        } else if (view == findViewById(R.id.btn_api_trigger)) {
            final Activity activity = this;

            RequestQueue queue = Volley.newRequestQueue(this);
            String url ="https://script.googleusercontent.com/a/macros/9gag.com/echo?user_content_key=5rB_V-GfLXa_VleYhJ17wJFhHzV7acDoGNlscBb4syuXE3rCLBGjRr4GTzZylfoFZxy1evj789I8hkXEF2MovbOq8QdZwM4wOJmA1Yb3SEsKFZqtv3DaNYcMrmhZHmUMi80zadyHLKAt-QELwJMLee8rPozBcotHtMyUQ50rEcTzW_eLHFbxIysVmuTfZaYEmUf1nKd3HdDN-fqX8Tw_b4mIOSv_1lOby1QSCbpxgpH5ispxt-K_YnyPKHFW471ddrx5_-y6IJwMC8qrsYZYetKv-L8akWNg7RE4Wg6mM2v8PXHli4Hd-rVpARzRnnDp&lib=M0klXLQOsKcIM4m86X8_CevqWUX6-vu4W";
            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    mDisplay.setText("Response: " + response.toString().substring(0,300));

                    NotificationManager mNotificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
                    try {
                        JSONArray values = response.getJSONArray("od1");
                        for(int i = 0 ; i < values.length(); i++) {
                            JSONObject obj = values.getJSONObject(i);
                            Log.i(TAG, obj.getString("Headline"));
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
                            PendingIntent contentIntent = PendingIntent.getActivity(activity, 0, openUrlIntent, 0);

                            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(activity)
                                .setSmallIcon(R.drawable.ic_noti)
                                .setContentTitle(title)
                                .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(title))
                                .setContentText(title)
                                .setAutoCancel(true);

                            mBuilder.setContentIntent(contentIntent);
                            mNotificationManager.notify(GcmIntentService.NOTIFICATION_ID + i, mBuilder.build());
                        }
                    } catch (JSONException e) {
                        Log.i(TAG, "json exception");
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    mDisplay.setText("That didn't work!");
                }
            });
            // Add the request to the RequestQueue.
            queue.add(jsObjRequest);
        } else if (view == findViewById(R.id.btn_api_test_trigger)) {
            RequestQueue queue = Volley.newRequestQueue(this);
            String url ="https://script.googleusercontent.com/a/macros/9gag.com/echo?user_content_key=5rB_V-GfLXa_VleYhJ17wJFhHzV7acDoGNlscBb4syuXE3rCLBGjRr4GTzZylfoFZxy1evj789I8hkXEF2MovbOq8QdZwM4wOJmA1Yb3SEsKFZqtv3DaNYcMrmhZHmUMi80zadyHLKAt-QELwJMLee8rPozBcotHtMyUQ50rEcTzW_eLHFbxIysVmuTfZaYEmUf1nKd3HdDN-fqX8Tw_b4mIOSv_1lOby1QSCbpxgpH5ispxt-K_YnyPKHFW471ddrx5_-y6IJwMC8qrsYZYetKv-L8akWNg7RE4Wg6mM2v8PXHli4Hd-rVpARzRnnDp&lib=M0klXLQOsKcIM4m86X8_CevqWUX6-vu4W";
            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    // Display the first 500 characters of the response string.
                    mDisplay.setText("Response is: "+ response.substring(0,500));
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    mDisplay.setText("That didn't work!");
                }
            });
            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        }
    }

    /**
     * Gets the current registration ID for application on GCM service, if there is one.
     * <p>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     *         registration ID.
     */
    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGcmPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        Log.i(TAG, "Current version is: " + Integer.toString(currentVersion));
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        Log.i(TAG, "Registration id is registrationId: " + registrationId);
        return registrationId;
    }

    /**
     * Stores the registration ID and the app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param regId registration ID
     */
    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGcmPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

    /**
     * @return Application's {@code SharedPreferences}.
     */
    private SharedPreferences getGcmPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the regID in your app is up to you.
        return getSharedPreferences(MainActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    /**
     * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP or CCS to send
     * messages to your app. Not needed for this demo since the device sends upstream messages
     * to a server that echoes back the message using the 'from' address in the message.
     */
    private void sendRegistrationIdToBackend() {
      // Your implementation here.
        Log.i(TAG, "sendRegistrationIdToBackend");
    }
}

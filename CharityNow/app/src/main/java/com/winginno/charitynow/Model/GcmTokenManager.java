package com.winginno.charitynow;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.Activity;

import android.os.AsyncTask;

import android.content.Context;

import android.util.Log;

import java.io.IOException;

public class GcmTokenManager {
    static final String TAG = "CharityNow";

    /**
     * Substitute you own sender ID here. This is the project number you got
     * from the API Console, as described in "Getting Started."
     */
    private final static String SENDER_ID = "PLACE_HOLDER";

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    static GoogleCloudMessaging gcm = null;


    public static void initialize(Activity activity)
    {
        // Check device for Play Services APK.
        if (checkPlayServices(activity)) {
            // If this check succeeds, proceed with normal processing.
            // Otherwise, prompt user to get valid Play Services APK.
            Context context = ApplicationContextProvider.getContext();
            gcm = GoogleCloudMessaging.getInstance(context);
            GcmTokenStorage gcmTokenStorage = new GcmTokenStorage();
            String regid = gcmTokenStorage.get();

            if (regid.isEmpty()) {
                registerInBackground();
            }
        } else {
            Log.i(TAG, "No valid Google Play Services APK found.");
        }
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    public static boolean checkPlayServices(Activity activity) {
        Context context = ApplicationContextProvider.getContext();
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                Log.i(TAG, "This device is not supported.");
                GooglePlayServicesUtil.getErrorDialog(resultCode, activity, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
            }
            return false;
        }
        Log.i(TAG, "This device is supported.");
        return true;
    }

    public static String getRegistrationId()
    {
        GcmTokenStorage gcmTokenStorage = new GcmTokenStorage();
        return gcmTokenStorage.get();
    }

    /**
     * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP or CCS to send
     * messages to your app. Not needed for this demo since the device sends upstream messages
     * to a server that echoes back the message using the 'from' address in the message.
     */
    public static void sendRegistrationIdToBackend() {
        // Your implementation here.
        Log.i(TAG, "sendRegistrationIdToBackend");
    }

    /**
     * Registers the application with GCM servers asynchronously.
     * <p>
     * Stores the registration ID and the app versionCode in the application's
     * shared preferences.
     */
    private static void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            // This gets executed at background and return a string result for onPostExecute() to use
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        Context context = ApplicationContextProvider.getContext();
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    Log.i(TAG, "About to call gcm.register");
                    String regid = gcm.register(SENDER_ID);
                    msg = regid;
                    Log.i(TAG, "Device registered, registration ID=" + regid);

                    // You should send the registration ID to your server over HTTP, so it
                    // can use GCM/HTTP or CCS to send messages to your app.
                    sendRegistrationIdToBackend();

                    // For this demo: we don't need to send it because the device will send
                    // upstream messages to a server that echo back the message using the
                    // 'from' address in the message.

                    // Persist the regID - no need to register again.
                    GcmTokenStorage gcmTokenStorage = new GcmTokenStorage();
                    gcmTokenStorage.set(regid);
                } catch (IOException ex) {
                    msg = "";
                    Log.e(TAG, "IO Error :" + ex.getMessage());
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                Log.i(TAG, " onPostExecute " + msg);
                if (!msg.isEmpty()) {
                    SettingsApi settingsApi = new SettingsApi();
                    SettingsStorage settingsStorage = new SettingsStorage();
                    settingsApi.save(msg, "", settingsStorage.get());
                }
            }
        }.execute(null, null, null);
    }
}

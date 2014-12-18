package com.winginno.charitynow;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
// import android.widget.Toast;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.JsonObjectRequest;

public class DataFetchTaskReceiver extends BroadcastReceiver {

    static final String TAG = "CharityNow";

    @Override
    public void onReceive(Context arg0, Intent arg1) {
        Log.i(TAG, "DataFetchTaskReceiver onReceive()");
        Context appContext = ApplicationContextProvider.getContext();

        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url ="https://script.googleusercontent.com/a/macros/9gag.com/echo?user_content_key=5rB_V-GfLXa_VleYhJ17wJFhHzV7acDoGNlscBb4syuXE3rCLBGjRr4GTzZylfoFZxy1evj789I8hkXEF2MovbOq8QdZwM4wOJmA1Yb3SEsKFZqtv3DaNYcMrmhZHmUMi80zadyHLKAt-QELwJMLee8rPozBcotHtMyUQ50rEcTzW_eLHFbxIysVmuTfZaYEmUf1nKd3HdDN-fqX8Tw_b4mIOSv_1lOby1QSCbpxgpH5ispxt-K_YnyPKHFW471ddrx5_-y6IJwMC8qrsYZYetKv-L8akWNg7RE4Wg6mM2v8PXHli4Hd-rVpARzRnnDp&lib=M0klXLQOsKcIM4m86X8_CevqWUX6-vu4W";
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "DataFetchTaskReceiver volley onResponse()");
                EventsStorage eventsStorage = new EventsStorage();
                eventsStorage.set(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "DataFetchTaskReceiver volley onErrorResponse()");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

}

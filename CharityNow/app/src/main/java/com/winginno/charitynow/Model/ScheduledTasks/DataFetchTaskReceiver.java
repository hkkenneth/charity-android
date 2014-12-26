package com.winginno.charitynow;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;

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

    private ArrayList<CallbackableActivityInterface> callbackObjects = new ArrayList<CallbackableActivityInterface>();

    public void addCallback(CallbackableActivityInterface callbackObject) {
        callbackObjects.add(callbackObject);
    }

    @Override
    public void onReceive(Context arg0, Intent arg1) {
        Log.i(TAG, "DataFetchTaskReceiver onReceive()");
        Context appContext = ApplicationContextProvider.getContext();

        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = "https://script.googleusercontent.com/macros/echo?user_content_key=bIWFEOUd4oSdepYQ5qw73rLRhfglQfdnm-wd7LX2Lv0Wsb--IkrqBdQ3nxWYKOP2PFYUGqKGupxcK40osoR36_Uq9flix-FbOJmA1Yb3SEsKFZqtv3DaNYcMrmhZHmUMWojr9NvTBuBLhyHCd5hHa3GaoauzAMbF001i6RBWVb-ahx4EiaaV5fSJzo-0WxE6LvjOUOM7LAmIGuV_AbfFrj0-L2Dqk3c86HUp6oO_MiG62ZT_A2P50INU58kCY7cDOw0k5H1fFWRzb20fTPaEy6Q03jIrKSwFLrKcaEvqS_2I8DPeI7MCag&lib=M0klXLQOsKcIM4m86X8_CevqWUX6-vu4W";

        final ArrayList<CallbackableActivityInterface> callbackObjs = callbackObjects;
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "DataFetchTaskReceiver volley onResponse()");
                EventsStorage eventsStorage = new EventsStorage();
                eventsStorage.set(response);

                for (CallbackableActivityInterface obj : callbackObjs) {
                    obj.callback(CallbackableActivityInterface.CALLBACK_TYPE_DATA_FETCH);
                }
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

package com.winginno.charitynow;

import android.content.Context;
import android.util.Log;

import java.util.Map;
import java.util.HashMap;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.StringRequest;

import com.google.gson.Gson;

public class SettingsApi {

    static final String TAG = "CharityNow";

    public void save(String regId, String prevRegId, Settings settings) {
        Log.i(TAG, "SettingsApi save()");
        Context appContext = ApplicationContextProvider.getContext();

        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = "http://charity-api.winginno.com:8080/settings";

        final Settings settingsPayload = settings;
        final String regIdPayload = regId;
        final String prevRegIdPayload = prevRegId;
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "SettingsApi volley onResponse() " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "SettingsApi volley onErrorResponse() " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() 
            {  
                Map<String, String> params = new HashMap<String, String>();
                params.put("regId", regIdPayload);
                params.put("prevRegId", prevRegIdPayload);
                Gson gson = new Gson();
                String jsonString = gson.toJson(settingsPayload);
                params.put("payload", jsonString);

                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

}

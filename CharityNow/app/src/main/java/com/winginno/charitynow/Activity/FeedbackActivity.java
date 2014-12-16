package com.winginno.charitynow;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.KeyEvent;
import android.view.Window;

import android.widget.Toast;

import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class FeedbackActivity extends Activity {

    /**
     * Tag used on log messages.
     */
    static final String TAG = "CharityNow";
    static final String FEEDBACK_FORM_URL = "https://docs.google.com/forms/d/1e8FZ4mwI_CROHOF6qRTv9Y0k8K_x76sPjeTmkR6GEX0/viewform";
    static final String FEEDBACK_FORM_URL_FINISH_SUFFIX = "formResponse";
    WebView myWebView;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "FeedbackActivity on Create.");
        // Let's display the progress in the activity title bar, like the
        // browser app does.
        getWindow().requestFeature(Window.FEATURE_PROGRESS);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        context = getApplicationContext();

        myWebView = (WebView) findViewById(R.id.feedback_webview);
        myWebView.setWebViewClient(new FeedbackWebViewClient());

        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        final Activity activity = this;
        myWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                // Activities and WebViews measure progress with different scales.
                // The progress meter will automatically disappear when we reach 100%
                activity.setProgress(progress * 1000);
            }
        });

        myWebView.loadUrl(FEEDBACK_FORM_URL);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && myWebView.canGoBack()) {
            myWebView.goBack();
            return true;
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }

    private class FeedbackWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            if (url.endsWith(FeedbackActivity.FEEDBACK_FORM_URL_FINISH_SUFFIX)) {
                // FIXME use the proper way to avoid creating many overlapping activities
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);

                Toast.makeText(context, R.string.feedback_thankyou, Toast.LENGTH_SHORT).show();
            }
        }
    }

}

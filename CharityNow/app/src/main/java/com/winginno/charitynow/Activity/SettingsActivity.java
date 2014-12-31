package com.winginno.charitynow;

import android.app.Activity;
import android.os.Bundle;

import android.content.Context;
import android.util.Log;
import android.view.View;

import android.widget.Toast;

import com.gc.materialdesign.views.CheckBox;
import com.gc.materialdesign.views.CheckBox.OnCheckListener;

public class SettingsActivity extends Activity {

    /**
     * Tag used on log messages.
     */
    static final String TAG = "CharityNow";
    Context context;
    Settings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "SettingsActivity on Create.");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        context = getApplicationContext();

        SettingsStorage settingsStorage = new SettingsStorage();
        settings = settingsStorage.get();

        final Settings appSettings = settings;

        // Other noti
        CheckBox checkbox = (CheckBox) findViewById(R.id.settings_option_other_noti);
        checkbox.setChecked(settings.isOtherNotiEnabled());
        checkbox.setOncheckListener(new OnCheckListener() {
            @Override
            public void onCheck(boolean isChecked) {
                appSettings.setOtherNotiEnabled(isChecked);
            }
        });

        // HK
        checkbox = (CheckBox) findViewById(R.id.settings_flag_sale_hk);
        checkbox.setChecked(settings.isFlagSaleNotiHkEnabled());
        checkbox.setOncheckListener(new OnCheckListener() {
            @Override
            public void onCheck(boolean isChecked) {
                appSettings.setFlagSaleNotiHkEnabled(isChecked);
            }
        });

        // Kowloon
        checkbox = (CheckBox) findViewById(R.id.settings_flag_sale_kowloon);
        checkbox.setChecked(settings.isFlagSaleNotiKowloonEnabled());
        checkbox.setOncheckListener(new OnCheckListener() {
            @Override
            public void onCheck(boolean isChecked) {
                appSettings.setFlagSaleNotiKowloonEnabled(isChecked);
            }
        });

        // NT
        checkbox = (CheckBox) findViewById(R.id.settings_flag_sale_nt);
        checkbox.setChecked(settings.isFlagSaleNotiNtEnabled());
        checkbox.setOncheckListener(new OnCheckListener() {
            @Override
            public void onCheck(boolean isChecked) {
                appSettings.setFlagSaleNotiNtEnabled(isChecked);
            }
        });
        Tracking.trackPageView("SettingsActivity");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "SettingsActivity on Resume.");
    }

    public void onClick(final View view) {
        if (view == findViewById(R.id.settings_action_save)) {
            SettingsStorage settingsStorage = new SettingsStorage();
            settingsStorage.set(settings);
            Toast.makeText(context, R.string.settings_text_saved, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

}

package com.winginno.charitynow;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.KeyEvent;
import android.widget.Toast;
import android.widget.TextView;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;

import java.util.Date;

import android.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity implements CallbackableActivityInterface {

    /**
     * Tag used on log messages.
     */
    static final String TAG = "CharityNow";

    Context context;
    Menu optionMenu;

    String regid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "on Create.");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();

        // GcmTokenManager.initialize(this);

        initListView();

        if (!DataFetchTask.isActive()) {
            DataFetchTask.create();
        }
        if (!NotificationTask.isActive()) {
            NotificationTask.create();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        optionMenu = menu;
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            Intent intent = new Intent(context, SettingsActivity.class);
            startActivity(intent);
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
            fillListView();
            return true;
        }
        if (id == R.id.action_clearEvents) {
            EventsStorage eventsStorage = new EventsStorage();
            eventsStorage.set("");
            return true;
        }
        if (id == R.id.action_fetchGcmToken) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // You need to do the Play Services APK check here too.
    @Override
    protected void onResume() {
        super.onResume();

        // GcmTokenManager.initialize(this);

        if (!DataFetchTask.isActive()) {
            DataFetchTask.create();
        }
        if (!NotificationTask.isActive()) {
            NotificationTask.create();
        }
    }

    public void callback(String type) {
        Log.i(TAG, " callback " + type);
        if (type == CallbackableActivityInterface.CALLBACK_TYPE_DATA_FETCH) {
            fillListView();
        }
    }

    // public boolean onKeyDown(int keyCode, KeyEvent event) {
    //     Log.i(TAG, " onKeyDown");
    //     if (event.getAction() == KeyEvent.ACTION_DOWN) {
    //         Log.i(TAG, "onKeyDown ACTION_DOWN");
    //         switch(keyCode) {
    //             case KeyEvent.KEYCODE_MENU:
    //                 Log.i(TAG, "onKeyDown KEYCODE_MENU.");
    //                 boolean result = optionMenu.performIdentifierAction(R.id.menu_main, 0);
    //                 if (result) {
    //                     Log.i(TAG, "onKeyDown performIdentifierAction ok.");
    //                 } else {
    //                     Log.i(TAG, "onKeyDown performIdentifierAction fail.");
    //                 }
    //                 return true;
    //         }
    //     }
    //     return false;
    // }

    private void initListView() {
        if (EventsFactory.getEvents().isEmpty()) {
            TextView textview = (TextView) findViewById(R.id.listview_placeholder);
            textview.setVisibility(View.VISIBLE);
            DataFetchTaskReceiver dataFetchTaskReceiver = new DataFetchTaskReceiver();
            dataFetchTaskReceiver.addCallback(this);
            dataFetchTaskReceiver.onReceive(null, null);
        } else {
            fillListView();
        }
    }

    private void fillListView() {
        TextView textview = (TextView) findViewById(R.id.listview_placeholder);
        ArrayList<Event> events = EventsFactory.getEvents();
        if (events.isEmpty()) {
            textview.setText(R.string.listview_text_placeholder_no_events);
            textview.setVisibility(View.VISIBLE);
        } else {
            textview.setVisibility(View.GONE);
        }

        ArrayList<ListItemInterface> listItems = new ArrayList<ListItemInterface>();
        ArrayList<Event> pastEvents = new ArrayList<Event>();
        ArrayList<Event> futureEvents = new ArrayList<Event>();

        for (Event e : events) {
            if (e.getEndDate() == null) {
                continue;
            }

            if (e.getEndDate().before(new Date())) {
                pastEvents.add(e);
            } else {
                futureEvents.add(e);
            }
        }
        if (!futureEvents.isEmpty()) {
            listItems.add(new SectionHeader(this.getString(R.string.listview_text_current)));
            listItems.addAll(futureEvents);
        }

        if (!pastEvents.isEmpty()) {
            listItems.add(new SectionHeader(this.getString(R.string.listview_text_past)));
            listItems.addAll(pastEvents);
        }

        final ListView listview = (ListView) findViewById(R.id.event_listview);

        final ArrayAdapter<ListItemInterface> adapter = new EventViewAdapter(this, android.R.layout.simple_list_item_1, listItems);
        listview.setAdapter(adapter);

        final Context activityContext = context;
        AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {

          @Override
          public void onItemClick(AdapterView<?> parent, final View view,
              int position, long id) {

            Toast.makeText(activityContext, String.valueOf(id), Toast.LENGTH_SHORT).show();
          }

        };

        listview.setOnItemClickListener(listener);
        listview.requestLayout();
    }

}

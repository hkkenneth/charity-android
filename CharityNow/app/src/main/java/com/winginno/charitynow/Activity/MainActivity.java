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
import java.util.Map;
import java.util.HashMap;

public class MainActivity extends ActionBarActivity implements CallbackableActivityInterface {

    /**
     * Tag used on log messages.
     */
    static final String TAG = "CharityNow";

    Context context;
    Menu optionMenu;
    Map<String, Integer> idToViewIndexMap;

    String regid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, " MainActivity on Create.");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();

        // GcmTokenManager.initialize(this);

        initListView();

        if (savedInstanceState != null) {
            String eventId = savedInstanceState.getString("eventId");
            Log.i(TAG, " MainActivity onCreate extra: " + eventId);
            if ((eventId != null) && (!eventId.isEmpty())) {
                scrollListViewToTaggedView(eventId);
            }
        }

        if (!DataFetchTask.isActive()) {
            DataFetchTask.create();
        }
        if (!NotificationTask.isActive()) {
            NotificationTask.create();
        }

        Tracking.trackPageView("MainActivity");
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
        Log.i(TAG, " MainActivity on Resume.");
        super.onResume();

        if ((getIntent() != null) && (getIntent().getExtras() != null)) {
            String eventId = getIntent().getExtras().getString("eventId");
            Log.i(TAG, " MainActivity onResume extra: " + eventId);
            if ((eventId != null) && (!eventId.isEmpty())) {
                scrollListViewToTaggedView(eventId);
            }
        }

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

    private void scrollListViewToIndex(int index) {
        final ListView listview = (ListView) findViewById(R.id.event_listview);
        listview.smoothScrollToPositionFromTop(index, 0, 300);
    }

    private void scrollListViewToTaggedView(String tag) {
        final ListView listview = (ListView) findViewById(R.id.event_listview);
        Integer index = idToViewIndexMap.get(tag);
        if (index != null) {
            View rowView = listview.getChildAt(index.intValue());
            int h1 = listview.getHeight();
            int h2 = 0;
            if (rowView != null) {
                h2 = rowView.getHeight();
            }
            // Log.i(TAG, "scrollListViewToTaggedView " + index.toString());
            listview.smoothScrollToPositionFromTop(index.intValue(), h1/2 - h2/2, 300);
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
        idToViewIndexMap = new HashMap<String, Integer>();

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
        int currentIndex = 0;
        if (!futureEvents.isEmpty()) {
            listItems.add(new SectionHeader(this.getString(R.string.listview_text_current)));
            listItems.addAll(futureEvents);
            currentIndex += 1;
            for (Event e : futureEvents) {
                idToViewIndexMap.put(e.getId(), new Integer(currentIndex));
                currentIndex += 1;
            }
        }

        if (!pastEvents.isEmpty()) {
            listItems.add(new SectionHeader(this.getString(R.string.listview_text_past)));
            listItems.addAll(pastEvents);
            currentIndex += 1;
            for (Event e : pastEvents) {
                idToViewIndexMap.put(e.getId(), new Integer(currentIndex));
                currentIndex += 1;
            }
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

package com.winginno.charitynow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

import java.text.SimpleDateFormat;
import java.util.Locale;

import com.squareup.picasso.Picasso;

public class EventViewAdapter extends ArrayAdapter<ListItemInterface> {
    private final Context context;
    private final List<ListItemInterface> values;

    public EventViewAdapter(Context context, int resource, List<ListItemInterface> values) {
      super(context, resource, values);

      this.context = context;
      this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ListItemInterface listItem = values.get(position);
        if (listItem.isSectionHeader()) {
            View rowView = inflater.inflate(R.layout.section_header, parent, false);
            TextView textView = (TextView) rowView.findViewById(R.id.section_header_title);

            textView.setText(listItem.getName());

            return rowView;
        } else {
            View rowView = inflater.inflate(R.layout.event_item, parent, false);
            TextView textViewDate = (TextView) rowView.findViewById(R.id.event_date);
            TextView textViewName = (TextView) rowView.findViewById(R.id.event_org_name);
            TextView textViewDescription = (TextView) rowView.findViewById(R.id.event_org_description);
            ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
            TextView textViewLocation = (TextView) rowView.findViewById(R.id.event_location);

            Event event = (Event) listItem;

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            textViewDate.setText(sdf.format(event.getStartDate()));
            textViewName.setText(event.getName());
            textViewDescription.setText(event.getDescription());
            textViewLocation.setText(event.getRegion());

            Picasso.with(context).load(event.getImageUrl()).into(imageView);

            return rowView;
        }
    }
} 

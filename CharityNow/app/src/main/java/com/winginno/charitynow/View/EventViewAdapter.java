package com.winginno.charitynow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

import com.squareup.picasso.Picasso;

public class EventViewAdapter extends ArrayAdapter<Event> {
    private final Context context;
    private final List<Event> values;

    public EventViewAdapter(Context context, int resource, List<Event> values) {
      super(context, resource, values);

      this.context = context;
      this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.event_item, parent, false);
        TextView textView1 = (TextView) rowView.findViewById(R.id.firstLine);
        TextView textView2 = (TextView) rowView.findViewById(R.id.secondLine);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        textView1.setText(values.get(position).getName());
        textView2.setText(values.get(position).getMembership());

        // if (position % 2 == 0) {
        //     imageView.setImageResource(R.drawable.ic_launcher);
        // } else {
        //     imageView.setImageResource(R.drawable.ic_noti);
        // }
        Picasso.with(context).load(values.get(position).getImageUrl()).into(imageView);

        return rowView;
    }
} 

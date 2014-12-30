package com.winginno.charitynow;

import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

import android.graphics.Paint;

import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.graphics.Color;
import android.graphics.Typeface;

import java.util.List;

import android.net.Uri;

import com.squareup.picasso.Picasso;

public class EventViewAdapter extends ArrayAdapter<ListItemInterface> {
    private final Context context;
    private final List<ListItemInterface> values;
    static final String TAG = "CharityNow";

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
            View buttonWeb = rowView.findViewById(R.id.listview_btn_web);
            View buttonRecruitWeb = rowView.findViewById(R.id.listview_btn_recruit_web);
            View buttonRecruitPhone = rowView.findViewById(R.id.listview_btn_recruit_phone);
            TextView textViewMembership = (TextView) rowView.findViewById(R.id.event_memberships);

            Event event = (Event) listItem;

            textViewDate.setText(event.getStartDateString());
            textViewName.setText(event.getName());
            textViewDescription.setText(event.getDescription());
            textViewLocation.setText(event.getRegion());
            textViewLocation.setPaintFlags(textViewLocation.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

            final Context appContext = context;

            if (!event.getRecruitPhone().isEmpty() && !event.isFinished()) {
                final Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + event.getRecruitPhone()));
                buttonRecruitPhone.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        appContext.startActivity(callIntent);
                    }
                });
                buttonRecruitPhone.setVisibility(View.VISIBLE);
            }

            if (!event.getRecruitWeb().isEmpty() && !event.isFinished()) {
                final Intent openUrlIntent = new Intent(Intent.ACTION_VIEW);
                openUrlIntent.setData(Uri.parse(event.getRecruitWeb()));
                buttonRecruitWeb.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        appContext.startActivity(openUrlIntent);
                    }
                });
                buttonRecruitWeb.setVisibility(View.VISIBLE);
            }

            if (!event.getWebsiteUrl().isEmpty()) {
                final Intent openUrlIntent = new Intent(Intent.ACTION_VIEW);
                openUrlIntent.setData(Uri.parse(event.getWebsiteUrl()));
                buttonWeb.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        appContext.startActivity(openUrlIntent);
                    }
                });
                buttonWeb.setVisibility(View.VISIBLE);
            }

            if (event.getMemberships().isEmpty()) {
                textViewMembership.setText(context.getString(R.string.listview_text_membership) +
                    context.getString(R.string.listview_text_membership_unknown));
            } else {
                String stringContent = context.getString(R.string.listview_text_membership);
                int startIndex = stringContent.length() + 1;
                for (String s : event.getMemberships()) {
                    stringContent += " ";
                    stringContent += s;
                }
                SpannableString styledString = new SpannableString(stringContent);
                int[] colors = new int[]{Color.BLUE, Color.RED};
                int i = 0;
                for (String s : event.getMemberships()) {
                    styledString.setSpan(new RoundedBackgroundSpan(colors[i % 2], Color.WHITE), startIndex, startIndex + s.length(), 0);
                    styledString.setSpan(new StyleSpan(Typeface.BOLD), startIndex, startIndex + s.length(), 0);
                    startIndex = startIndex + s.length() + 1;
                    i++;
                }
                textViewMembership.setText(styledString);
            }

            Picasso.with(context).load(event.getImageUrl()).into(imageView);

            rowView.setTag(event.getId());

            return rowView;
        }
    }
} 

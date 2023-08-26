package com.example.a310_rondayview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import java.text.SimpleDateFormat;
import java.util.List;

public class SwipeAdapter extends BaseAdapter {

    private Context context;
    private List<Event> events;
    // Views
    private TextView clubNameTextView;
    private TextView eventTitleTextView;
    private TextView locationTextView;
    private TextView timeTextView;
    private TextView eventDescriptionTextView;
    private ImageView eventImageView;

    private ImageView eventClubPFPImageView;

    public SwipeAdapter(Context context, List<Event> events) {
        this.context = context;
        this.events = events;
    }

    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public Object getItem(int i) {
        return events.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View currentView, ViewGroup parent) {
        View view;
        if (currentView == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_container, parent, false);
        } else {
            view = currentView;
        }
        Event event = events.get(i);
        // Find views within the included layout
        clubNameTextView = view.findViewById(R.id.clubNameTextView);
        eventTitleTextView = view.findViewById(R.id.eventTitleTextView);
        locationTextView = view.findViewById(R.id.locationTextView);
        timeTextView = view.findViewById(R.id.dateTimeTextView);
        eventDescriptionTextView = view.findViewById(R.id.eventDescriptionTextView);
        eventImageView = view.findViewById(R.id.eventImageView);
        eventClubPFPImageView = view.findViewById(R.id.profileImageView);

        if (i < events.size()) {
            if (clubNameTextView != null) {
                clubNameTextView.setText(event.getClubName());
            }
            eventTitleTextView.setText(event.getTitle());
            locationTextView.setText(event.getLocation());

            // Format the date and time as a single string
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("d' 'MMMM yyyy, hh:mm a");
            String dateTimeString = dateTimeFormat.format(event.getDateTime());

            // Set the formatted date and time in the UI
            timeTextView.setText(dateTimeString);
            eventDescriptionTextView.setText(event.getDescription());
            Glide.with(context).load(event.getImageURL()).into(eventImageView);
            Glide.with(context).load(event.getEventClubProfilePicture()).into(eventClubPFPImageView);
        }
        return view;
    }
}

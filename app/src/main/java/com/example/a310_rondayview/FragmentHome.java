package com.example.a310_rondayview;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FragmentHome extends Fragment {

    private List<Event> events = new ArrayList<>();
    private int currentEventIndex = 0;

    // Views
    private TextView clubNameTextView;
    private TextView eventTitleTextView;
    private TextView locationTextView;
    private TextView timeTextView;
    private TextView eventDescriptionTextView;
    private ImageView eventImageView;

    private ImageView eventClubPFPImageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        // Find the CardView that includes the event container layout
        View eventCardView = rootView.findViewById(R.id.eventCardView);

        // Find the included layout within the CardView
        View eventContainer = eventCardView.findViewById(R.id.event_container);

        // Find views within the included layout
        clubNameTextView = eventContainer.findViewById(R.id.clubNameTextView);
        eventTitleTextView = eventContainer.findViewById(R.id.eventTitleTextView);
        locationTextView = eventContainer.findViewById(R.id.locationTextView);
        timeTextView = eventContainer.findViewById(R.id.dateTimeTextView);
        eventDescriptionTextView = eventContainer.findViewById(R.id.eventDescriptionTextView);
        eventImageView = eventContainer.findViewById(R.id.eventImageView);
        eventClubPFPImageView = eventContainer.findViewById(R.id.profileImageView);

        // Fetch data from Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("events").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Event event = new Event(
                            document.getId(),
                            document.getString("clubName"),
                            document.getString("title"),
                            document.getString("description"),
                            document.getString("location"),
                            document.getTimestamp("dateTime").toDate(),
                            document.getString("imageURL"),
                            document.getString("eventClubProfilePicture")
                    );
                    events.add(event);
                }
                // Updating UI with the first event
                updateUI();
            }
        });

        // Set up button click listeners
        Button nopeButton = rootView.findViewById(R.id.nopeButton);
        Button interestedButton = rootView.findViewById(R.id.interestedButton);

        nopeButton.setOnClickListener(v -> nextEvent());
        interestedButton.setOnClickListener(v -> nextEvent());

        return rootView;
    }

    private void nextEvent() {
        if (currentEventIndex < events.size() - 1) {
            currentEventIndex++;
            updateUI();
        }
    }

    private void updateUI() {
        if (currentEventIndex < events.size()) {
            Event event = events.get(currentEventIndex);
            clubNameTextView.setText(event.clubName);
            eventTitleTextView.setText(event.title);
            locationTextView.setText(event.location);

            // Format the date and time as a single string
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("d' 'MMMM yyyy, hh:mm a");
            String dateTimeString = dateTimeFormat.format(event.dateTime);

            // Set the formatted date and time in the UI
            timeTextView.setText(dateTimeString);

            eventDescriptionTextView.setText(event.description);
            Glide.with(this).load(event.imageURL).into(eventImageView);
            Glide.with(this).load(event.eventClubProfilePicture).into(eventClubPFPImageView);
        }
    }
}



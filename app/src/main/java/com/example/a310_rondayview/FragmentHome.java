package com.example.a310_rondayview;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.a310_rondayview.data.event.EventsFirestoreManager;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class FragmentHome extends Fragment {

    private static final String TAG = "FragmentHome";
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

        // Get events from Firestore
        EventsFirestoreManager.getInstance().getAllEvents(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    events = task.getResult().toObjects(Event.class);
                }
                // Update UI with the first event
                updateUI();
            }
        });

        // Add a listener to the events Firestore collection to receive real time updates
        EventsFirestoreManager.getInstance().addEventsListener(new EventListener<QuerySnapshot>() {
           @Override
           public void onEvent(@Nullable QuerySnapshot snapshots,
                               @Nullable FirebaseFirestoreException e) {

               if (e != null) {
                   Log.w(TAG, "Listen failed.", e);
                   return;
               }

               for (DocumentChange dc : snapshots.getDocumentChanges()) {
                   switch (dc.getType()) {
                       case ADDED:
                           // if a document was added, add it to events list
                           Event event = dc.getDocument().toObject(Event.class);
                           events.add(event);
                           // update home page?
                           break;
                       case MODIFIED:
                           // TO DO (currently no way to modify events in app)
                           break;
                       case REMOVED:
                           // TO DO (currently no way to delete events in app)
                           break;
                   }
               }
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
            Glide.with(this).load(event.getImageURL()).into(eventImageView);
            Glide.with(this).load(event.getEventClubProfilePicture()).into(eventClubPFPImageView);
        }
    }
}



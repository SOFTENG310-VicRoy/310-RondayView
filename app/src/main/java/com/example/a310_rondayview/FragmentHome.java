package com.example.a310_rondayview;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.yalantis.library.Koloda;
import com.yalantis.library.KolodaListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class FragmentHome extends Fragment {
    private SwipeAdapter adapter;
    private List<Event> events = new ArrayList<>();
    private int currentEventIndex = 0;
    private LinearLayout buttonContainer;
    private LinearLayout emptyEventsLayout;
    Koloda koloda;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        // setting up views
        buttonContainer = rootView.findViewById(R.id.buttonsContainer);
        emptyEventsLayout = rootView.findViewById(R.id.emptyEventsLayout);
        // setting up koloda (for the card swipes) - READ MORE HERE: https://github.com/Yalantis/Koloda-Android
        koloda = rootView.findViewById(R.id.koloda);
        adapter = new SwipeAdapter(getContext(), events);
        koloda.setAdapter(adapter);

        /**
         * Koloda interface listener functions, don't need to use ALL
         */
        koloda.setKolodaListener(new KolodaListener() {
            @Override
            public void onNewTopCard(int i) {
            }

            @Override
            public void onCardDrag(int i, @NonNull View view, float v) {
            }

            @Override
            public void onCardSwipedLeft(int i) {
                handleSwipe(false, i);
            }

            @Override
            public void onCardSwipedRight(int i) {
               handleSwipe(true, i);
            }

            @Override
            public void onClickRight(int i) {
            }

            @Override
            public void onClickLeft(int i) {
            }

            //TODO DETAILS PAGE (A2) -> show details of event from dialog
            @Override
            public void onCardSingleTap(int i) {

            }

            @Override
            public void onCardDoubleTap(int i) {

            }

            @Override
            public void onCardLongPress(int i) {

            }

            @Override
            public void onEmptyDeck() { // events finished - need method of refreshing!
                if (currentEventIndex == events.size()-1) {
                    emptyEventsLayout.setVisibility(View.VISIBLE);
                    koloda.setVisibility(View.GONE);
                    buttonContainer.setVisibility(View.GONE);
                    currentEventIndex = 0;
                }
            }
        });

        // fetching event data
        fetchEventData();

        // Set up button click listeners
        Button nopeButton = rootView.findViewById(R.id.nopeButton);
        Button interestedButton = rootView.findViewById(R.id.interestedButton);
        Button refreshButton = rootView.findViewById(R.id.refreshButton);

        // NOT INTERESTED
        nopeButton.setOnClickListener(v -> {
            koloda.onButtonClick(false);
            handleSwipe(false, currentEventIndex);
        });

        // INTERESTED
        interestedButton.setOnClickListener(view -> {
            koloda.onButtonClick(true);
            handleSwipe(true, currentEventIndex);
        });

        // REFRESH PAGE
        refreshButton.setOnClickListener(view -> {
            emptyEventsLayout.setVisibility(View.GONE);
            koloda.setVisibility(View.VISIBLE);
            buttonContainer.setVisibility(View.VISIBLE);
            fetchEventData(); // fetch data again
            koloda.reloadAdapterData();
        });

        return rootView;
    }

    /**
     * Fetches the event data from the event collection in DB
     * COULD store this function in another class (DatabaseService class for SRP)
     */
    private void fetchEventData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("events").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                events.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Event event = document.toObject(Event.class);
                    events.add(event);
                }
                // Re-notify the adapter when the event data changed
                adapter.notifyDataSetChanged();
            } else {
                Log.e("Database error", "Fetching of events not working properly");
            }
        });
    }

    /**
     *
     * @param isInterested - if the user is interested or not
     * @param index - the particular event index
     */
    private void handleSwipe(boolean isInterested, int index) {
        currentEventIndex = index;
        if (isInterested) {
            FireBaseUserDataManager.getInstance().addInterestedEvent(events.get(currentEventIndex));
            FireBaseUserDataManager.getInstance().getInterestedEvents();
        }
        nextEvent();
        Toast.makeText(getContext(), isInterested ? "Interested" : "Not Interested", Toast.LENGTH_SHORT).show();
    }

    /**
     * Loads next event
     */
    private void nextEvent() {
        if (currentEventIndex < events.size() - 1) {
            currentEventIndex++;
            adapter.notifyDataSetChanged();
        } else { // if all events run out
           koloda.getKolodaListener().onEmptyDeck();
        }
    }

    public List<Event> getEvents() {
        return events;
    }

    public int getCurrentEventIndex() {
        return currentEventIndex;
    }
}



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
    Koloda koloda;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        // setting up koloda for card views
        koloda = rootView.findViewById(R.id.koloda);
        adapter = new SwipeAdapter(getContext(), events);
        koloda.setAdapter(adapter);

        koloda.setKolodaListener(new KolodaListener() {
            @Override
            public void onNewTopCard(int i) {
            }

            @Override
            public void onCardDrag(int i, @NonNull View view, float v) {
            }

            @Override
            public void onCardSwipedLeft(int i) {
                nextEvent(); // not interested
                Toast.makeText(getContext(), "Swiped Left - Not Interested", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCardSwipedRight(int i) {
                FireBaseUserDataManager.getInstance().addInterestedEvent(events.get(currentEventIndex));
                FireBaseUserDataManager.getInstance().getInterestedEvents();
                nextEvent();
                Toast.makeText(getContext(), "Swiped Right - Interested", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onClickRight(int i) {

            }

            @Override
            public void onClickLeft(int i) {

            }

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
            public void onEmptyDeck() {

            }
        });

        // fetching event data
        fetchEventData();

        // Set up button click listeners
        Button nopeButton = rootView.findViewById(R.id.nopeButton);
        Button interestedButton = rootView.findViewById(R.id.interestedButton);

        nopeButton.setOnClickListener(v -> {
            koloda.onButtonClick(false);
            nextEvent();
        });

        interestedButton.setOnClickListener(view -> {
            koloda.onButtonClick(true);
            FireBaseUserDataManager.getInstance().addInterestedEvent(events.get(currentEventIndex));
            FireBaseUserDataManager.getInstance().getInterestedEvents();
            nextEvent();
        });

        return rootView;
    }

    private void fetchEventData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("events").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                events.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Event event = document.toObject(Event.class);
                    events.add(event);
                }
                // changing the adapter
                adapter.notifyDataSetChanged();
            } else {
                Log.e("Database error", "Fetching of events not working properly");
            }
        });
    }

    private void nextEvent() {
        if (currentEventIndex < events.size() - 1) {
            currentEventIndex++;
            adapter.notifyDataSetChanged();
        }
    }
}



package com.example.a310_rondayview.data.event;

import android.util.Log;

import com.example.a310_rondayview.model.Event;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class DatabaseService {
    /**
     * Gets all current events irrespective of a user interacting with them
     * @return Arraylist of events
     */
    public CompletableFuture<ArrayList<Event>> getAllEvents(){
        CompletableFuture<ArrayList<Event>> futureEvents = new CompletableFuture<>();
        ArrayList<Event> allEvents = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("events").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Event event = document.toObject(Event.class);
                    allEvents.add(event);
                }
            } else {
                Log.e("Database error", "Fetching of events not working properly");
            }
            futureEvents.complete(allEvents);
        });
        return futureEvents;
    }

    /**
     * Gets all the events a user is disinterested in
     * @return Arraylist of events
     */
    private ArrayList<Event> fetchDisinterestedEvents() {
        ArrayList<Event> disinterestedEvents = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").get().addOnCompleteListener(task -> { // is this the correct collection path?
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Event event = document.toObject(Event.class);
                    disinterestedEvents.add(event);
                }
            } else {
                Log.e("Database error", "Fetching of disinterested events not working properly");
            }
        });
        return disinterestedEvents;
    }

}

package com.example.a310_rondayview.data.event;

import android.util.Log;

import com.example.a310_rondayview.data.user.FireBaseUserDataManager;
import com.example.a310_rondayview.model.Event;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
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
     * Only uninterested events or events they haven't seen should show
     * @return applicable events to show the user
     */
    public CompletableFuture<ArrayList<Event>> getApplicableEvents(){
        CompletableFuture<ArrayList<Event>> futureApplicableEvents = new CompletableFuture<>();
        FireBaseUserDataManager fireBaseUserDataManager = FireBaseUserDataManager.getInstance();
        fireBaseUserDataManager.getEvents(true);
        ArrayList<Event> applicableEvents = new ArrayList<>();
        List<Event> interestedEvents = fireBaseUserDataManager.getInterestedEvents();
        getAllEvents().thenAccept(events -> {
            for (Event event: events) {
                if(!interestedEvents.contains(event)){
                    applicableEvents.add(event);
                }
            }
            futureApplicableEvents.complete(applicableEvents);
        });
        return (futureApplicableEvents);
    }
}

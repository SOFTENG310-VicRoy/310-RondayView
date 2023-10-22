package com.example.a310_rondayview.data.event;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.a310_rondayview.data.user.FireBaseUserDataManager;
import com.example.a310_rondayview.model.Event;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class EventDatabaseService {
    private static final String DATABASE_ERROR_TAG = "Database error";

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
                Log.e(DATABASE_ERROR_TAG, "Fetching of events not working properly");
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
        FireBaseUserDataManager fireBaseUserDataManager = FireBaseUserDataManager.getInstance();

        CompletableFuture<ArrayList<Event>> futureApplicableEvents = new CompletableFuture<>();
        //Make sure the participated group names are correctly obtained before moving on
        FireBaseUserDataManager.getInstance().getParticipatedGroupNames(participatedGroups->{
            ArrayList<Event> applicableEvents = new ArrayList<>();
            fireBaseUserDataManager.getEvents(true).thenAccept(interestedEvents -> getAllEvents().thenAccept(events -> {
                Log.d("GET EVENT", "User is in group: "+participatedGroups);
                for (Event event: events) {
                    if(!interestedEvents.contains(event)){
                        String groupNameTag = event.getGroupNameTag();
                        if(groupNameTag==null||participatedGroups.contains(groupNameTag)||groupNameTag.equals("")){
                            Log.d("GET EVENT", "CURRENT EVENT GROUP IS FROM: "+event.getGroupNameTag());
                            applicableEvents.add(event);
                        }
                    }
                }
                futureApplicableEvents.complete(applicableEvents);
            }));
        });
        return (futureApplicableEvents);
    }

    /**
     * Gets a specific event by an ID
     * @param eventId
     * @return the event from the firebase db
     */
    public CompletableFuture<Event> getEventById(String eventId) {
        CompletableFuture<Event> futureEvent = new CompletableFuture<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference eventRef = db.collection("events").document(eventId);
        eventRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Event event = document.toObject(Event.class);
                    futureEvent.complete(event);
                } else {
                    Log.e(DATABASE_ERROR_TAG, "Event with ID " + eventId + " not found.");
                    futureEvent.complete(null); // Event not found
                }
            } else {
                Log.e(DATABASE_ERROR_TAG, "Error fetching event with ID " + eventId);
                futureEvent.completeExceptionally(task.getException());
            }
        });

        return futureEvent;
    }

}

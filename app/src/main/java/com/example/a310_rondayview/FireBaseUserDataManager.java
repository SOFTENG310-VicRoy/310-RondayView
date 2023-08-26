package com.example.a310_rondayview;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class FireBaseUserDataManager {

    private final FirebaseFirestore db;
    public List<Event> InterestedEvents = new ArrayList<>();
    public List<Event> DisinterestedEvents = new ArrayList<>();

    private static final String TAG = "FireBaseUserDataManager";
    private static final String USERSCOLLECTION = "users";
    private static final String INTERESTEDEVENTSCOLLECTION = "interestedEvents";
    private static final String DISINTERESTEDEVENTSCOLLECTION = "disinterestedEvents";
    private static final String SIGNINERROR = "No user is currently signed in.";


    private FireBaseUserDataManager() {
        db = FirebaseFirestore.getInstance();
        // On startup, populate the lists
        getInterestedEvents();
        getDisinterestedEvents();
    }

    private static final class InstanceHolder {
        static final FireBaseUserDataManager instance = new FireBaseUserDataManager();
    }

    public static FireBaseUserDataManager getInstance() {
        return InstanceHolder.instance;
    }

    public void getInterestedEvents() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        // Get the currently signed-in user
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        // Check if a user is signed in
        if (currentUser != null) {
            String uid = currentUser.getUid();

            // Use the UID to fetch all the users interested events and store it in a singleton list
            db.collection(USERSCOLLECTION)
                    .document(uid)
                    .collection(INTERESTEDEVENTSCOLLECTION)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            List<Event> events = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document != null && document.exists()) {
                                    Event event = new Event();

                                    // Assign the appropriate database values to the java object variables
                                    event.setEventId(document.getId());
                                    event.setClubName(document.getString("clubName"));
                                    event.setDateTime(document.getDate("dateTime"));
                                    event.setDescription(document.getString("description"));
                                    event.setEventClubProfilePicture(document.getString("eventClubProfilePicture"));
                                    event.setImageURL(document.getString("imageURL"));
                                    event.setLocation(document.getString("location"));
                                    event.setTitle(document.getString("title"));

                                    // Add the Event object to the list of events
                                    events.add(event);
                                }
                            }

                            // Assign the fetched list of Event objects to the appropriate class member variable
                            InterestedEvents = events;

                            Log.d(TAG, "Successfully fetched the interested events data: " + events.toString());
                        } else {
                            Log.e(TAG, "Error fetching interested events data: ", task.getException());
                        }
                    });
        } else {
            // Handle the case where no user is signed in
            Log.e(TAG, SIGNINERROR);
        }
    }

    public void getDisinterestedEvents() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        // Get the currently signed-in user
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        // Check if a user is signed in
        if (currentUser != null) {
            String uid = currentUser.getUid();

            // Use the UID to fetch all the users disinterested events and store it in a singleton list
            db.collection(USERSCOLLECTION)
                    .document(uid)
                    .collection(DISINTERESTEDEVENTSCOLLECTION)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            List<Event> events = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document != null && document.exists()) {
                                    Event event = new Event();

                                    // Assign the appropriate database values to the java object variables
                                    event.setEventId(document.getId());
                                    event.setClubName(document.getString("clubName"));
                                    event.setDateTime(document.getDate("dateTime"));
                                    event.setDescription(document.getString("description"));
                                    event.setEventClubProfilePicture(document.getString("eventClubProfilePicture"));
                                    event.setImageURL(document.getString("imageURL"));
                                    event.setLocation(document.getString("location"));
                                    event.setTitle(document.getString("title"));

                                    // Add the Event object to the list of events
                                    events.add(event);
                                }
                            }

                            // Assign the fetched list of Event objects to the appropriate class member variable
                            DisinterestedEvents = events;

                            Log.d(TAG, "Successfully fetched the disinterested events data: " + events.toString());
                        } else {
                            Log.e(TAG, "Error fetching disinterested events data: ", task.getException());
                        }
                    });
        } else {
            // Handle the case where no user is signed in
            Log.e(TAG, SIGNINERROR);
        }
    }

    public void addDisinterestedEvent(Event event) {
        // Get the current Firebase Auth instance
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        // Check if a user is signed in
        if (currentUser != null) {
            String uid = currentUser.getUid();

            // Use the UID to add the event to the users' Firestore collection
            db.collection(USERSCOLLECTION)
                    .document(uid)
                    .collection(DISINTERESTEDEVENTSCOLLECTION)
                    .document(event.getEventId())
                    .set(event);
        } else {
            Log.e(TAG, SIGNINERROR);
        }
    }

    public void addInterestedEvent(Event event) {
        // Get the current Firebase Auth instance
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        // Check if a user is signed in
        if (currentUser != null) {
            String uid = currentUser.getUid();

            // Use the UID to add the event to the users' Firestore collection
            db.collection(USERSCOLLECTION)
                    .document(uid)
                    .collection(INTERESTEDEVENTSCOLLECTION)
                    .document(event.getEventId())
                    .set(event);
        } else {
            Log.e(TAG, SIGNINERROR);
        }
    }

    public void removeInterestedEvent(Event event) {
        // Get the current Firebase Auth instance
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        // Check if a user is signed in
        if (currentUser != null) {
            String uid = currentUser.getUid();

            // Use the UID to remove the event from the users' Firestore collection
            db.collection(USERSCOLLECTION)
                    .document(uid)
                    .collection(INTERESTEDEVENTSCOLLECTION)
                    .document(event.getEventId())
                    .delete();
        } else {
            // Handle the case where no user is signed in
            Log.e(TAG, SIGNINERROR);
        }
    }
}


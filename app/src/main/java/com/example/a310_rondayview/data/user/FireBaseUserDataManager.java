package com.example.a310_rondayview.data.user;

import android.util.Log;

import com.example.a310_rondayview.model.Event;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FireBaseUserDataManager {
    private static final String TAG = "FireBaseUserDataManager";
    private static final String USERSCOLLECTION = "users";
    private static final String INTERESTEDEVENTSCOLLECTION = "interestedEvents";
    private static final String DISINTERESTEDEVENTSCOLLECTION = "disinterestedEvents";
    private static final String FRIEND_FIELD = "friends";
    private static final String SIGNINERROR = "No user is currently signed in.";
    private static final String FIND_FRIEND_ERROR = "Failed to find friend";
    private final FirebaseFirestore db;
    private List<Event> interestedEvents = new ArrayList<>();
    private List<Event> disinterestedEvents = new ArrayList<>();
    private List<String> friendEmails = new ArrayList<>();
    private List<Event> eventList = new ArrayList<>();

    private FireBaseUserDataManager() {
        db = FirebaseFirestore.getInstance();
        // On startup, populate the lists
        getEvents(true);
        getEvents(false);
    }

    private static final class InstanceHolder {
        static final FireBaseUserDataManager instance = new FireBaseUserDataManager();
    }

    public static FireBaseUserDataManager getInstance() {
        return InstanceHolder.instance;
    }

    public List<Event> getInterestedEvents() {
        return interestedEvents;
    }

    public List<Event> getDisinterestedEvents() {
        return disinterestedEvents;
    }

    public List<String> getFriendEmails() {
        return friendEmails;
    }

    public void getEvents(Boolean interested) {
        String collection;

        if(Boolean.TRUE.equals(interested)){
            collection = INTERESTEDEVENTSCOLLECTION;
        } else {
            collection = DISINTERESTEDEVENTSCOLLECTION;
        }

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        // Get the currently signed-in user
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        // Check if a user is signed in
        if (currentUser != null) {
            String uid = currentUser.getUid();

            // Use the UID to fetch all the users disinterested events and store it in a singleton list
            db.collection(USERSCOLLECTION)
                    .document(uid)
                    .collection(collection)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Assign the fetched list of Event objects to the appropriate class member variable
                            eventList = makeEventsList(task.getResult());

                            Log.d(TAG, "Successfully fetched the disinterested events data: " + disinterestedEvents.toString());
                        } else {
                            Log.e(TAG, "Error fetching disinterested events data: ", task.getException());
                        }
                    });
        } else {
            // Handle the case where no user is signed in
            Log.e(TAG, SIGNINERROR);
        }
        if(Boolean.TRUE.equals(interested)){
            interestedEvents = eventList;
        } else {
            disinterestedEvents = eventList;
        }
    }

    List<Event> makeEventsList(QuerySnapshot snapshot) {
        List<Event> events = new ArrayList<>();
        for (QueryDocumentSnapshot document : snapshot) {
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
        return events;
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

    public void getFriends(FriendCallback callback) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        // Get the currently signed-in user
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        // Check if a user is signed in
        if (currentUser != null) {
            String uid = currentUser.getUid();

            // Get a reference to the user's document
            DocumentReference userDocRef = db.collection(USERSCOLLECTION).document(uid);

            // Retrieve the "friends" array field from the user's document
            userDocRef.get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                List<String> friendIds = (List<String>) document.get(FRIEND_FIELD);

                                if (friendIds != null && !friendIds.isEmpty()) {
                                    friendEmails.clear();
                                    // Loop through friend IDs and fetch their email addresses
                                    for (String friendId : friendIds) {
                                        db.collection(USERSCOLLECTION)
                                                .document(friendId)
                                                .get()
                                                .addOnCompleteListener(friendTask -> {
                                                    if (friendTask.isSuccessful()) {
                                                        DocumentSnapshot friendDocument = friendTask.getResult();
                                                        if (friendDocument.exists()) {
                                                            String friendEmail = (String) friendDocument.get(FRIEND_FIELD);
                                                            if (friendEmail != null) {
                                                                // Add the friend's email to the list
                                                                friendEmails.add(friendEmail);
                                                            }
                                                        }
                                                    } else {
                                                        Log.e(TAG, "Error fetching friend data: ", friendTask.getException());
                                                    }

                                                    // Check if we have fetched all friend emails
                                                    if (friendEmails.size() == friendIds.size()) {
                                                        // Handle the list of friend emails here
                                                        Log.d(TAG, "Friend emails: " + friendEmails.toString());
                                                        callback.onSuccessfulFriendOperation();
                                                    }
                                                });
                                    }
                                } else {
                                    // Handle the case where the user has no friends
                                    Log.d(TAG, "User has no friends.");
                                }
                            } else {
                                Log.e(TAG, "User document does not exist.");
                            }
                        } else {
                            Log.e(TAG, "Error fetching user data: ", task.getException());
                        }
                    });
        } else {
            // Handle the case where no user is signed in
            Log.e(TAG, SIGNINERROR);
        }
    }
    public void addFriend(String friendEmail, FriendCallback callback) {
        // Get the current Firebase Auth instance
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        // Check if a user is signed in
        if (currentUser != null) {
            String uid = currentUser.getUid();

            // Get a reference to the users' collection
            CollectionReference usersCollectionRef = db.collection(USERSCOLLECTION);

            // Query for the friend's document by their email
            usersCollectionRef.whereEqualTo("email", friendEmail)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                // Assuming there's only one user with the given email, but you can handle multiple results if needed
                                DocumentSnapshot friendDocument = querySnapshot.getDocuments().get(0);
                                String friendUserId = friendDocument.getId();

                                // Get a reference to the current user's document
                                DocumentReference currentUserDocRef = usersCollectionRef.document(uid);

                                // Update the "friends" array field with the new friend's UID
                                currentUserDocRef.update(FRIEND_FIELD, FieldValue.arrayUnion(friendUserId))
                                        .addOnSuccessListener(aVoid -> {
                                            // Update was successful
                                            // Perform additional actions here if needed
                                            if (!friendEmails.contains(friendEmail)) {
                                                friendEmails.add(friendEmail);

                                            }
                                            callback.onSuccessfulFriendOperation();
                                        })
                                        .addOnFailureListener(e -> callback.onUnsuccessfulFriendOperation(new Exception("Failed to add friend")));
                            } else {
                                // Friend with the provided email does not exist
                                // Handle the case accordingly
                                callback.onUnsuccessfulFriendOperation(new Exception(FIND_FRIEND_ERROR));
                            }
                        } else {
                            // Handle the error
                            Log.e(TAG, "Error querying for friend: ", task.getException());
                            callback.onUnsuccessfulFriendOperation(new Exception(FIND_FRIEND_ERROR));
                        }
                    });
        }
    }
    public void removeFriend(String friendEmail, FriendCallback callback) {
        // Get the current Firebase Auth instance
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        // Check if a user is signed in
        if (currentUser != null) {
            String uid = currentUser.getUid();

            // Get a reference to the users' collection
            CollectionReference usersCollectionRef = db.collection(USERSCOLLECTION);

            // Query for the friend's document by their email
            usersCollectionRef.whereEqualTo("email", friendEmail)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                // Assuming there's only one user with the given email, but you can handle multiple results if needed
                                DocumentSnapshot friendDocument = querySnapshot.getDocuments().get(0);
                                String friendUserId = friendDocument.getId();

                                // Get a reference to the current user's document
                                DocumentReference currentUserDocRef = usersCollectionRef.document(uid);

                                // Update the "friends" array field with the  friend's UID
                                currentUserDocRef.update(FRIEND_FIELD, FieldValue.arrayRemove(friendUserId))
                                        .addOnSuccessListener(aVoid -> {
                                            // Update was successful
                                            // Perform additional actions here if needed
                                            friendEmails.remove(friendEmail);
                                            callback.onSuccessfulFriendOperation();
                                        })
                                        .addOnFailureListener(e -> callback.onUnsuccessfulFriendOperation(new Exception("Failed to remove friend")));
                            } else {
                                // Friend with the provided email does not exist
                                // Handle the case accordingly
                            }
                        } else {
                            // Handle the error
                            Log.e(TAG, "Error querying for friend: ", task.getException());
                            callback.onUnsuccessfulFriendOperation(new Exception(FIND_FRIEND_ERROR));

                        }
                    });
        }
    }
}


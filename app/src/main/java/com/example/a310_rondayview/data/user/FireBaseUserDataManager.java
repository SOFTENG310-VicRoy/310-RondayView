package com.example.a310_rondayview.data.user;

import android.util.Log;

import com.example.a310_rondayview.model.Event;
import com.example.a310_rondayview.model.User;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
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

    private List<User> friendsList = new ArrayList<>();
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
    public List<User> getFriendsList() {
        return friendsList;
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


    /**
     * This method gets the current user's friends data.
     * Current it gets the friends emails
     * @param callback
     */
    public void getFriends(FriendCallback callback) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser == null) {
            // Handle the case where no user is signed in
            Log.e(TAG, SIGNINERROR);
            return;
        }

        String uid = currentUser.getUid();
        DocumentReference userDocRef = db.collection(USERSCOLLECTION).document(uid);

        userDocRef.get()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        // Handle the error
                        Log.e(TAG, "Error fetching user data: ", task.getException());
                        return;
                    }
                    DocumentSnapshot document = task.getResult();
                    if (document == null || !document.exists()) {
                        // Handle the case where the user document does not exist
                        Log.e(TAG, "User document does not exist.");
                        return;
                    }
                    List<String> friendIds = (List<String>) document.get(FRIEND_FIELD);
                    if (friendIds == null || friendIds.isEmpty()) {
                        // Handle the case where the user has no friends
                        Log.d(TAG, "User has no friends.");
                        callback.onSuccessfulFriendOperation();
                        return;
                    }
                    friendsList.clear();
                    fetchFriendData(friendIds, callback);
                    // Add condition checks if we are getting something else...
                });
    }
    /**
     * This method takes the friends ids and get the friends' user document
     * @param friendIds
     * @param callback
     */
    private void fetchFriendData(List<String> friendIds, FriendCallback callback) {
        List<Task<DocumentSnapshot>> friendTasks = new ArrayList<>();
        for (String friendId : friendIds) {
            DocumentReference friendDocRef = db.collection(USERSCOLLECTION).document(friendId);
            Task<DocumentSnapshot> friendTask = friendDocRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot friendDocument = task.getResult();
                    try {
                        String friendEmail = friendDocument.getString("email");
                        User friend = new User(friendEmail);
                        friend.setUserId(friendId);
                        friendsList.add(friend);
                    } catch (Exception e) {
                        Log.e(TAG, "Error fetching friend data: ", task.getException());
                    }
                }
            });
            friendTasks.add(friendTask);
        }
        Tasks.whenAllSuccess(friendTasks)
                .addOnSuccessListener(results -> {
                    // Handle the list of friend emails here
                    Log.d(TAG, "Friend data: " + friendsList.toString());
                    callback.onSuccessfulFriendOperation();
                })
                // Handle the case where not all friends were fetched successfully
                .addOnFailureListener(e -> Log.e(TAG, "Error fetching friend data: ", e));
    }

    /**
     * This method adds a friend to the users array of friends in the db
     * @param friendEmail
     * @param callback
     */
    public void addFriend(String friendEmail, FriendCallback callback) {
        performFriendOperation(friendEmail, callback, true);
    }
    /**
     * This method removes a friend to the users array of friends in the db
     * @param friendEmail
     * @param callback
     */
    public void removeFriend(String friendEmail, FriendCallback callback) {
        performFriendOperation(friendEmail, callback, false);
    }

    /**
     * This method performs some operation on the friend array of the current user.
     * @param friendEmail
     * @param callback
     * @param addFriend
     */
    private void performFriendOperation(String friendEmail, FriendCallback callback, boolean addFriend) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser == null) {
            // Handle the case where no user is signed in
            Log.e(TAG, SIGNINERROR);
            return;
        }
        String uid = currentUser.getUid();
        CollectionReference usersCollectionRef = db.collection(USERSCOLLECTION);

        usersCollectionRef.whereEqualTo("email", friendEmail)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        DocumentSnapshot friendDocument = querySnapshot.getDocuments().get(0);
                        String friendUserId = friendDocument.getId();
                        User friend = new User(friendEmail);
                        friend.setUserId(friendUserId);
                        DocumentReference currentUserDocRef = usersCollectionRef.document(uid);

                        // Perform the friend operation based on the 'addFriend' parameter
                        if (addFriend) {
                            currentUserDocRef.update(FRIEND_FIELD, FieldValue.arrayUnion(friendUserId))
                                    .addOnSuccessListener(aVoid -> {
                                        if (!friendsList.contains(friend)) {
                                            friendsList.add(friend);
                                        }
                                        callback.onSuccessfulFriendOperation();
                                    })
                                    .addOnFailureListener(e -> callback.onUnsuccessfulFriendOperation(new Exception("Failed to add friend")));
                        } else {
                            currentUserDocRef.update(FRIEND_FIELD, FieldValue.arrayRemove(friendUserId))
                                    .addOnSuccessListener(aVoid -> {
                                        friendsList.remove(friend);
                                        callback.onSuccessfulFriendOperation();
                                    })
                                    .addOnFailureListener(e -> callback.onUnsuccessfulFriendOperation(new Exception("Failed to remove friend")));
                        }
                    } else {
                        // Friend with the provided email does not exist
                        callback.onUnsuccessfulFriendOperation(new Exception(FIND_FRIEND_ERROR));
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle the error
                    Log.e(TAG, "Error querying for friend: ", e);
                    callback.onUnsuccessfulFriendOperation(new Exception(FIND_FRIEND_ERROR));
                });
    }

    /**
     * This method takes a friend User object and sets its interested event list to the list found in db
     * @param friend
     * @param callback Callback to call after it is successful
     */
    public void getFriendsEvents(User friend, FriendCallback callback) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        // Get the currently signed-in user
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        // Check if a user is signed in
        if (currentUser != null) {
            // Use the UID to fetch all the users interested events and store it in a singleton list
            db.collection(USERSCOLLECTION)
                    .document(friend.getUserId())
                    .collection(INTERESTEDEVENTSCOLLECTION)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            friend.setInterestedEvents(makeEventsList(task.getResult()));
                            callback.onSuccessfulFriendOperation();
                            Log.d(TAG, "Successfully fetched the friends' events data: " + disinterestedEvents.toString());
                        } else {
                            Log.e(TAG, "Error fetching events data: ", task.getException());
                        }
                    });
        } else {
            // Handle the case where no user is signed in
            Log.e(TAG, SIGNINERROR);
        }
    }
}


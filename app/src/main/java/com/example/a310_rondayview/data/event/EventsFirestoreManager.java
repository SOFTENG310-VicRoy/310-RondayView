package com.example.a310_rondayview.data.event;

import static com.example.a310_rondayview.data.event.EventsFirestoreContract.EVENT_COLLECTION_NAME;

import com.example.a310_rondayview.Event;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class EventsFirestoreManager {

    private static EventsFirestoreManager eventsFirestoreManager;

    private CollectionReference eventsCollectionReference;
    private FirebaseFirestore firebaseFirestore;

    private EventsFirestoreManager (){
        firebaseFirestore = FirebaseFirestore.getInstance();
        eventsCollectionReference = firebaseFirestore.collection(EVENT_COLLECTION_NAME);
    }

    public static EventsFirestoreManager getInstance(){
        if(eventsFirestoreManager == null){
            eventsFirestoreManager = new EventsFirestoreManager();
        }
        return eventsFirestoreManager;
    }

    public void addEvent(Event event){
        eventsCollectionReference.add(event);
    }

    public void addEvent(Event event, OnCompleteListener<DocumentReference> onCompleteListener){
        eventsCollectionReference.add(event).addOnCompleteListener(onCompleteListener);
    }

    /**
     * Updates the event in the database with the new event
     * @param event
     */
    public void updateEvent(Event event){
        String documentId = event.getEventId();
        eventsCollectionReference.document(documentId).set(event);
    }
    public void deleteEvent(String documentId){
        eventsCollectionReference.document(documentId).delete();
    }
    public void getAllEvents(OnCompleteListener<QuerySnapshot> onCompleteListener){
        eventsCollectionReference.get().addOnCompleteListener(onCompleteListener);
    }

    /**
     * Adds a listener to the events Firestore collection to receive real time updates
     * @param eventListener the listener to add
     */
    public void addEventsListener(EventListener<QuerySnapshot> eventListener){
        eventsCollectionReference.addSnapshotListener(eventListener);
    }
}

package com.example.a310_rondayview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

//public class MainActivity extends AppCompatActivity {
//
//    private static final String TAG = "FirestoreTest";
//    FirebaseFirestore firestore;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        firestore = FirebaseFirestore.getInstance();
//
//        // Replace "yourDocumentId" with the actual document ID you want to retrieve
//        String documentId = "JrffZAxQzSWQSvnMMKYG";
//
//        retrieveAndPrintDocumentData(documentId);
//    }
//
//    private void retrieveAndPrintDocumentData(String documentId) {
//        firestore.collection("events").document(documentId).get()
//                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                    @Override
//                    public void onSuccess(DocumentSnapshot documentSnapshot) {
//                        if (documentSnapshot.exists()) {
//                            // Retrieve data from the document
//                            String eventName = documentSnapshot.getString("eventName");
//                            String eventDate = documentSnapshot.getString("eventDate");
//
//                            // Print the retrieved data to the console
//                            Log.d(TAG, "Event Name: " + eventName);
//                            Log.d(TAG, "Event Date: " + eventDate);
//                        } else {
//                            Log.d(TAG, "Document does not exist");
//                        }
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.e(TAG, "Error getting document", e);
//                    }
//                });
//    }
//}

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "FirestoreTest";
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firestore = FirebaseFirestore.getInstance();

        // Data to be added to the new document
        Map<String, Object> newEventData = new HashMap<>();
        newEventData.put("eventName", "New Event");
        newEventData.put("eventDate", "August 20");

        addNewEvent(newEventData);
    }

    private void addNewEvent(Map<String, Object> eventData) {
        firestore.collection("events").add(eventData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "New event added successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error adding new event", e);
                    }
                });
    }
}






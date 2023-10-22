package com.example.a310_rondayview.data.group;

import android.util.Log;

import com.example.a310_rondayview.model.Group;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class GroupDatabaseService {
    private static final String DATABASE_ERROR_TAG = "Database error";

    public CompletableFuture<List<Group>> getAllGroups(){
        CompletableFuture<List<Group>> futureEvents = new CompletableFuture<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        ArrayList<Group> allGroups = new ArrayList<>();
        db.collection("groups").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Group group = document.toObject(Group.class);
                    allGroups.add(group);
                }
            } else {
                Log.e(DATABASE_ERROR_TAG, "Fetching of groups not working properly");
            }
            futureEvents.complete(allGroups);
        });
        return futureEvents;
    }

    /**
     * Retieve a group based on the given group name
     * @param groupName
     * @return Group
     */
    public CompletableFuture<Group> getGroupByName(String groupName){
        CompletableFuture<Group> futureEvent = new CompletableFuture<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query fetchQuery = db.collection("groups").whereEqualTo("groupName", groupName);
        fetchQuery.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<DocumentSnapshot> document = task.getResult().getDocuments();
                if (!document.isEmpty()) {
                    Group group = document.get(0).toObject(Group.class);
                    futureEvent.complete(group);
                } else {
                    Log.e(DATABASE_ERROR_TAG, "Group with name " + groupName + " not found.");
                    futureEvent.complete(null); // Event not found
                }
            } else {
                Log.e(DATABASE_ERROR_TAG, "Error fetching group with name " + groupName);
                futureEvent.completeExceptionally(task.getException());
            }
        });

        return futureEvent;
    }

}

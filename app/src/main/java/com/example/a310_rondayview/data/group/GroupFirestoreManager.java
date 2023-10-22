package com.example.a310_rondayview.data.group;

import com.example.a310_rondayview.model.Event;
import com.example.a310_rondayview.model.Group;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class GroupFirestoreManager {
    private static GroupFirestoreManager groupFirestoreManager;
    private CollectionReference collectionReference;
    private FirebaseFirestore firebaseFirestore;

    private GroupFirestoreManager(){
        firebaseFirestore = FirebaseFirestore.getInstance();
        collectionReference = firebaseFirestore.collection("groups");
    }
    public static GroupFirestoreManager getInstance(){
        if(groupFirestoreManager==null){
            groupFirestoreManager = new GroupFirestoreManager();
        }
        return groupFirestoreManager;
    }

    public void addGroup(Group group, OnCompleteListener<DocumentReference> onCompleteListener){
        collectionReference.add(group).addOnCompleteListener(onCompleteListener);
    }
    public void updateGroup(Group group){
        String documentId = group.getGroupId();
        collectionReference.document(documentId).set(group);
    }



}

package com.example.a310_rondayview;


import com.google.firebase.Timestamp;

public class Event {
    String eventID;
    String clubName;
    String title;
    String description;
    String location;
    Timestamp dateTime; // firestore default timestamp
    String imageURL;

    public Event(String eventID, String clubName, String title, String description, String location, Timestamp dateTime, String imageURL) {
        this.eventID = eventID;
        this.clubName = clubName;
        this.title = title;
        this.description = description;
        this.location = location;
        this.dateTime = dateTime;
        this.imageURL = imageURL;
    }

    void updateDetails(){}
    void delete(){}


}

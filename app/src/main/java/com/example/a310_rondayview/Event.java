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
    String eventClubProfilePicture;

    public Event(String eventID, String clubName, String title, String description, String location, Timestamp dateTime, String imageURL, String eventClubProfilePicture) {
        this.eventID = eventID;
        this.clubName = clubName;
        this.title = title;
        this.description = description;
        this.location = location;
        this.dateTime = dateTime;
        this.imageURL = imageURL;
        this.eventClubProfilePicture = eventClubProfilePicture;
    }

    void updateDetails(){}
    void delete(){}


}

package com.example.a310_rondayview;

import com.google.firebase.firestore.DocumentId;

import java.util.Date;

public class Event {

    @DocumentId
    private String eventId;
    private String clubName;
    private String title;
    private String description;
    private String location;
    private Date dateTime;
    private String imageURL;
    private String eventClubProfilePicture;

    public Event() {
        // Default constructor for Firestore deserialization
    }

    public Event(String eventId, String clubName, String title, String description, String location, Date dateTime, String imageURL, String eventClubProfilePicture) {
        this.eventId = eventId;
        this.clubName = clubName;
        this.title = title;
        this.description = description;
        this.location = location;
        this.dateTime = dateTime;
        this.imageURL = imageURL;
        this.eventClubProfilePicture = eventClubProfilePicture;
    }

    public String getEventId() {
        return eventId;
    }

    public String getClubName() {
        return clubName;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getEventClubProfilePicture() {
        return eventClubProfilePicture;
    }
}

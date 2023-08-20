package com.example.a310_rondayview;
import com.google.firebase.Timestamp;

public class EventDTO {
    String eventID;
    public String clubName;
    public String title;
    public String description;
    public String location;
    public Timestamp dateTime;
    public String imageURL;
    public String eventClubProfilePicture;

    public Event toEvent() {
        return new Event(
                eventID,
                clubName,
                title,
                description,
                location,
                dateTime.toDate(),
                imageURL,
                eventClubProfilePicture
        );
    }
}



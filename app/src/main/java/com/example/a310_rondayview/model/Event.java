package com.example.a310_rondayview.model;

import com.google.firebase.firestore.DocumentId;

import java.util.Date;
import java.util.Objects;

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
    private int interestCount;

    public Event() {
        // Default constructor for Firestore deserialization
    }

    public Event(String clubName, String title, String description, String location, Date dateTime, String imageURL, String eventClubProfilePicture, int interestCount) {
        this.clubName = clubName;
        this.title = title;
        this.description = description;
        this.location = location;
        this.dateTime = dateTime;
        this.imageURL = imageURL;
        this.eventClubProfilePicture = eventClubProfilePicture;
        this.interestCount = interestCount;
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

    public int getInterestCount(){ return interestCount;}

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setInterestCount(int i){ this.interestCount = i;}

    public void incrementInterestCount(){this.interestCount++;}
    public void decrementInterestCount(){if(this.interestCount>0){this.interestCount--;}}
    public void setEventClubProfilePicture(String eventClubProfilePicture) {
        this.eventClubProfilePicture = eventClubProfilePicture;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }

        Event otherEvent = (Event) obj;

        return (eventId.equals(otherEvent.eventId) ||
                (description.equals(otherEvent.description) && title.equals(otherEvent.title) && clubName.equals(otherEvent.clubName)));
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId, title);
    }
}

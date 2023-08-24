package com.example.a310_rondayview.data.event;

public class EventsFirestoreContract {

    // Root collection name
    public static final String EVENT_COLLECTION_NAME = "events";

    // Document ID
    public static final String DOCUMENT_ID = "document_id";

    // Document field names
    public static final String EVENT_CLUB_NAME = "clubName";
    public static final String EVENT_DATE_TIME = "dateTime";
    public static final String EVENT_DESCRIPTION= "description";
    public static final String EVENT_CLUB_PROFILE_PICTURE = "eventClubProfilePicture";
    public static final String EVENT_IMAGE_URL = "imageURL";
    public static final String EVENT_LOCATION = "location";
    public static final String EVENT_TITLE = "title";

    // To prevent someone from accidentally instantiating the contract class, make the constructor private
    private EventsFirestoreContract() {}
}

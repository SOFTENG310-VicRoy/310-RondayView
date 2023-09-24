package com.example.a310_rondayview.model;

public class CurrentEvent {
    private static CurrentEvent instance;
    private Event currentEvent;

    private CurrentEvent() {
        // Private constructor to prevent instantiation
    }

    public static synchronized CurrentEvent getInstance() {
        if (instance == null) {
            instance = new CurrentEvent();
        }
        return instance;
    }

    public Event getCurrentEvent() {
        return currentEvent;
    }

    public void setCurrentEvent(Event event) {
        this.currentEvent = event;
    }
}
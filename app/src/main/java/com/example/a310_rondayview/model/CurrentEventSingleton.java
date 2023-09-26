package com.example.a310_rondayview.model;

public class CurrentEventSingleton {
    private static CurrentEventSingleton instance;
    private Event currentEvent;

    private CurrentEventSingleton() {
        // Private constructor to prevent instantiation
    }

    public static synchronized CurrentEventSingleton getInstance() {
        if (instance == null) {
            instance = new CurrentEventSingleton();
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
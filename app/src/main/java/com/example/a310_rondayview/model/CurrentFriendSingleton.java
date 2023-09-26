package com.example.a310_rondayview.model;

public class CurrentFriendSingleton {
    private static CurrentFriendSingleton instance;
    private User currentFriend;

    private CurrentFriendSingleton() {
        // Private constructor to prevent instantiation
    }

    public static synchronized CurrentFriendSingleton getInstance() {
        if (instance == null) {
            instance = new CurrentFriendSingleton();
        }
        return instance;
    }

    public User getCurrentFriend() {
        return currentFriend;
    }

    public void setCurrentFriend(User currentFriend) {
        this.currentFriend = currentFriend;
    }
}

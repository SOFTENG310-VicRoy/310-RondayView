package com.example.a310_rondayview.model;

import com.google.firebase.firestore.DocumentId;

import java.util.Objects;

public class User {
    @DocumentId
    private String userId;
    private String email;

    public User() {
    }
    public User (String email) {
        this.email = email;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId) && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, email);
    }
}

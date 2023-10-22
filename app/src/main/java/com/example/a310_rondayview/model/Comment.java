package com.example.a310_rondayview.model;

public class Comment {
    private String username;
    private String commentText;
    private float rating;
    public Comment() {
    }

    public Comment(String username, String commentText, float rating) {
        this.username = username;
        this.commentText = commentText;
        this.rating = rating;
    }

    public String getUsername() {
        return username;
    }

    public String getCommentText() {
        return commentText;
    }

    public float getRating() { return rating; }
}

package com.example.a310_rondayview.model;

public class Comment {
    private String username;
    private String commentText;
    public Comment() {
    }

    public Comment(String username, String commentText) {
        this.username = username;
        this.commentText = commentText;
    }

    public String getUsername() {
        return username;
    }

    public String getCommentText() {
        return commentText;
    }
}

package com.example.doqu;

public class Comment {

    private String username,description;
    int commentId;


    public Comment(int commentId,String username,String description){
        this.commentId = commentId;
        this.username = username;
        this.description = description;
    }

    public String getUsername() {
        return username;
    }

    public String getDescription() {
        return description;
    }

    public int getCommentId() {
        return commentId;
    }
}

package com.example.doqu.Post;

public class Post {
    private String username,desc,title;
    private int id,like;

    public Post(int id, String title, String username, String desc,int like){
        this.id = id;
        this.title = title;
        this.username = username;
        this.desc = desc;
        this.like = like;
    }

    public String getUsername() {
        return username;
    }

    public String getDesc() {
        return desc;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getLike() {
        return like;
    }

    @Override
    public String toString() {
        return "Title : " + title + ", Username : " + username + ", Description : " + desc;
    }
}

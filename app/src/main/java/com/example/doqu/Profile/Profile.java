package com.example.doqu.Profile;

public class Profile {

    private String name,username;

    public Profile(String name,String username){
        this.name = name;
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public String getUsername(){
        return username;
    }
}

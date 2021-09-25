package com.example.dansdistractor;

public class UserSchema {

    public String name, email, location;
    public int points, totalDistance;


    public UserSchema(){
    }

    public UserSchema(String name, String email) {
        this.name = name;
        this.email = email;
        this.points = 0;
        this.totalDistance = 0;

    }
}

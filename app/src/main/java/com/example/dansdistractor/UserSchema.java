package com.example.dansdistractor;

public class UserSchema {

    public String name, email, location;
    public int points, totaldistance, usertotalpins;

    public UserSchema() {
    }

    public UserSchema(String name, String email) {
        this.name = name;
        this.email = email;
        this.points = 0;
        this.totaldistance = 0;
        this.usertotalpins = 0;
        this.location = null;

    }
}

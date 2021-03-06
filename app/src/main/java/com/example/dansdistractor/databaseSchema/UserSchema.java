package com.example.dansdistractor.databaseSchema;

import java.util.ArrayList;

public class UserSchema {

    public String name, email, location;
    public int points, totaldistance, usertotalpins;
    public ArrayList<String> vouchers;
    public ArrayList<String> invalidVouchers;
    public ArrayList<UserHistorySchema> userhistories;

    public UserSchema() {
    }

    public UserSchema(String name, String email) {
        this.name = name;
        this.email = email;
        this.points = 0;
        this.totaldistance = 0;
        this.usertotalpins = 0;
        this.location = null;
        this.invalidVouchers = new ArrayList<>();
        this.vouchers = new ArrayList<>();
        this.userhistories = new ArrayList<UserHistorySchema>();
    }
}

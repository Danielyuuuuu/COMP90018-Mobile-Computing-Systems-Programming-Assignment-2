package com.example.dansdistractor.databaseSchema;

import com.google.firebase.Timestamp;

import java.util.Date;

public class MessageSchema {

    public String author, content, address;
    public double lat, lon;
    public Timestamp timestamp;

    public MessageSchema() {
    }

    public MessageSchema(String author, double lat, double lon, String content, String address) {
        this.author = author;
        this.lat = lat;
        this.lon = lon;
        this.content = content;
        this.timestamp = Timestamp.now();
        this.address = address;

    }
}

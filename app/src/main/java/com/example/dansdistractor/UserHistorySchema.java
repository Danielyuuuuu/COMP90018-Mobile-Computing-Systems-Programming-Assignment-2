package com.example.dansdistractor;

import java.sql.Time;
import java.util.Date;

public class UserHistorySchema {

    public Date date;
    public Time startTime, endTime;
    public int distance, steps, avgSpeed, pins, points;

    public UserHistorySchema() {
    }

    public UserHistorySchema(Date date, Time startTime) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = null;
        this.distance = 0;
        this.steps = 0;
        this.avgSpeed = 0;
        this.pins = 0;
        this.points = 0;
    }
}

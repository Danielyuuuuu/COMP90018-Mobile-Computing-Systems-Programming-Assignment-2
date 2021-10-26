package com.example.dansdistractor.utils;

import android.location.Location;

/**
 * @ClassName: MyLocation
 * @Description: Adaptor of android.location.Location class
 * @Author: wongchihaul
 * @CreateDate: 2021/10/26 11:48 下午
 */
public class MyLocation {
    private Location l;

    public MyLocation() {
    }

    public MyLocation(Location l) {
        this.l = l;
    }

    public Location getLocation() {
        return this.l;
    }
}

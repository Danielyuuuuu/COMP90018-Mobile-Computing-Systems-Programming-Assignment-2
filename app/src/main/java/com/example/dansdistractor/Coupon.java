package com.example.dansdistractor;/**
 * Created by wongchihaul on 2021/9/22
 */

import java.util.ArrayList;

/**
 *
 * @ClassName: Coupons
 * @Description:    //TODO
 * @Author: wongchihaul
 * @CreateDate: 2021/9/22 12:53 AM
 */
public class Coupon {
    // couponImage to store the resource id if coupon image
    private int couponImage;
    // couponName to store the string of coupon name
    private String couponName;

    public Coupon(int couponImage, String couponName) {
        this.couponImage = couponImage;
        this.couponName = couponName;
    }

    public int getCouponImage() {
        return couponImage;
    }

    public void setCouponImage(int couponImage) {
        this.couponImage = couponImage;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }
    
    public static ArrayList<Coupon> getCoupons() {

        ArrayList<Coupon> coupons = new ArrayList<>();
        coupons.add(new Coupon(R.drawable.apple, "Apple"));
        coupons.add(new Coupon(R.drawable.bananas, "Bananas"));
        coupons.add(new Coupon(R.drawable.cherry, "Cherry"));
        coupons.add(new Coupon(R.drawable.grapes, "Grapes"));
        coupons.add(new Coupon(R.drawable.lemon, "Lemon"));
        coupons.add(new Coupon(R.drawable.orange, "Orange"));
        coupons.add(new Coupon(R.drawable.melon, "Melon"));
        coupons.add(new Coupon(R.drawable.peach, "Peach"));
        coupons.add(new Coupon(R.drawable.pear, "Pear"));
        coupons.add(new Coupon(R.drawable.pomegranate, "Pomegranate"));
        coupons.add(new Coupon(R.drawable.strawberry, "Strawberry"));
        coupons.add(new Coupon(R.drawable.watermelon, "Watermelon"));
        return coupons;
        
    }
}

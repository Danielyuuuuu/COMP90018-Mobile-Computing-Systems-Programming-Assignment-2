package com.example.dansdistractor.vouchers;/**
 * Created by wongchihaul on 2021/9/22
 */

import com.example.dansdistractor.R;

import java.util.ArrayList;

/**
 * @ClassName: Coupons

 * @Author: wongchihaul
 * @CreateDate: 2021/9/22 12:53 AM
 */


public class Voucher {
    // image to store the resource id of voucher image
    private int image;
    // name to store the string of voucher name
    private String name;
    // description to store the description of voucher
    private String desc;


    public Voucher() {
    }

    public Voucher(int image, String name) {
        this.image = image;
        this.name = name;
    }

    public static ArrayList<Voucher> getVouchers() {

        ArrayList<Voucher> vouchers = new ArrayList<>();
        vouchers.add(new Voucher(R.drawable.apple, "Apple"));
        vouchers.add(new Voucher(R.drawable.bananas, "Bananas"));
        vouchers.add(new Voucher(R.drawable.cherry, "Cherry"));
        vouchers.add(new Voucher(R.drawable.grapes, "Grapes"));
        vouchers.add(new Voucher(R.drawable.lemon, "Lemon"));
        vouchers.add(new Voucher(R.drawable.orange, "Orange"));
        vouchers.add(new Voucher(R.drawable.melon, "Melon"));
        vouchers.add(new Voucher(R.drawable.peach, "Peach"));
        vouchers.add(new Voucher(R.drawable.pear, "Pear"));
        vouchers.add(new Voucher(R.drawable.pomegranate, "Pomegranate"));
        vouchers.add(new Voucher(R.drawable.strawberry, "Strawberry"));
        vouchers.add(new Voucher(R.drawable.watermelon, "Watermelon"));
        return vouchers;

    }

    public int getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public static final class VoucherBuilder {
        private Voucher voucher;

        private VoucherBuilder() {
            voucher = new Voucher();
        }

        public VoucherBuilder withImage(int image) {
            voucher.image = image;
            return this;
        }

        public VoucherBuilder withName(String name) {
            voucher.name = name;
            return this;
        }

        public VoucherBuilder withDesc(String desc) {
            voucher.desc = desc;
            return this;
        }

        public Voucher build() {
            return voucher;
        }
    }
}

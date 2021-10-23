package com.example.dansdistractor.vouchers;/**
 * Created by wongchihaul on 2021/9/22
 */

import java.util.Objects;

/**
 * @ClassName: Coupons

 * @Author: wongchihaul
 * @CreateDate: 2021/9/22 12:53 AM
 */


public class Voucher {
    // image to store the resource id of voucher image
    public String imageURI;
    // name to store the string of voucher name
    public String name;
    // description to store the description of voucher
    public String desc;


    public Voucher() {
    }

    public String getImageURI() {
        return imageURI;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Voucher voucher = (Voucher) o;
        return imageURI.equals(voucher.imageURI) && name.equals(voucher.name) && desc.equals(voucher.desc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(imageURI, name, desc);
    }

    public static final class VoucherBuilder {
        private Voucher voucher;

        public VoucherBuilder() {
            voucher = new Voucher();
        }

        public VoucherBuilder withImageURI(String imageURI) {
            voucher.imageURI = imageURI;
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

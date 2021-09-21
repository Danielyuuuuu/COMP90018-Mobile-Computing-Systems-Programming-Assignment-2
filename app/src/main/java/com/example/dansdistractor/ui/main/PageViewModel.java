package com.example.dansdistractor.ui.main;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.dansdistractor.Coupon;
import com.example.dansdistractor.HistoryListAdaptor;

public class PageViewModel extends ViewModel {

    private MutableLiveData<Integer> mIndex = new MutableLiveData<>();
    private LiveData<String> mText = Transformations.map(mIndex, new Function<Integer, String>() {
        @Override
        public String apply(Integer input) {
            return "Hello world from section: " + input;
        }
    });

    public void setIndex(int index) {
        mIndex.setValue(index);
    }

    public LiveData<String> getText() {
        return mText;
    }

    private LiveData<Coupon> mCoupon = Transformations.map(mIndex, new Function<Integer, Coupon>() {
        @Override
        public Coupon apply(Integer input) {
            return Coupon.getCoupons().get(input);
        }
    });


    public LiveData<Coupon> getmCoupon() {
        return mCoupon;
    }


}
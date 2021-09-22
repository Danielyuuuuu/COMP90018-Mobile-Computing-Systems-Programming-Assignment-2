package com.example.dansdistractor;/**
 * Created by wongchihaul on 2021/9/22
 */

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

/**
 *
 * @ClassName: Cricket
 * @Description:    //TODO
 * @Author: wongchihaul
 * @CreateDate: 2021/9/22 7:03 下午
 */
public class Cricket extends Fragment {
    public Cricket() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_cricket, container, false);
        TextView textView = rootView.findViewById(R.id.cricket_text);
        textView.setText("HELLO Coupons");
        return rootView;
    }
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        MyAdapter adapter = new MyAdapter(getChildFragmentManager());
//        ViewPager viewPager = view.findViewById(R.id.viewPager);
//        viewPager.setAdapter(adapter);
//    }
}

package com.example.dansdistractor;/**
 * Created by wongchihaul on 2021/9/22
 */

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import butterknife.ButterKnife;

/**
 *
 * @ClassName: HistoryFragment
 * @Description:    //TODO
 * @Author: wongchihaul
 * @CreateDate: 2021/9/22 1:50 AM
 */
public class HistoryFragment extends Fragment {

//    static int LINEAR = R.layout.fragment_layout_demo_linear;
//    static int RELATIVE = R.layout.fragment_layout_demo_relative;
//    static int RECYCLER = R.layout.fragment_layout_demo_recycler;

    static int LIST = R.layout.fragment_list_history;
    static String LAYOUT_TYPE = "type";

    private int layout = R.layout.fragment_list_history;
    private ListView linearView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (this.getArguments() != null)
            this.layout = getArguments().getInt(LAYOUT_TYPE);

        View view = inflater.inflate(layout, container, false);
        ButterKnife.bind(this, view);
        initializeList(view);

        return view;
    }

    // Recommended method to generate new LayoutDemoFragment
    // Instead of calling new LayoutDemoFragment() directly
    static Fragment newInstance(int layout) {
        Fragment fragment = new HistoryFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(LAYOUT_TYPE, layout);
        fragment.setArguments(bundle);

        return fragment;
    }

    // To bind ListView and RecyclerView to the corresponding layout
    private void initializeList(View view) {
        HistoryListAdaptor adapter = new HistoryListAdaptor(getActivity(), R.layout.list_history, Coupon.getCoupons());
        linearView = view.findViewById(R.id.history_list_view);
        linearView.setAdapter(adapter);
    }

}

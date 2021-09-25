package com.example.dansdistractor;/**
 * Created by wongchihaul on 2021/9/22
 */

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

/**
 * @ClassName: Cricket
 * @Description: //TODO
 * @Author: wongchihaul
 * @CreateDate: 2021/9/22 7:03 下午
 */
public class Cricket extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public Cricket() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list_history, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh_list);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        CouponListAdaptor adaptor = new CouponListAdaptor(requireActivity(), R.layout.coupon_list, Coupon.getCoupons());
        ListView listView = view.findViewById(R.id.history_list_view_bak);
        System.out.println(listView);
        listView.setAdapter(adaptor);

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Coupon coupon = (Coupon) adapterView.getItemAtPosition(i);
//                Toast.makeText(getContext(), coupon.getCouponName(), Toast.LENGTH_SHORT).show();
//            }
//        });
        return view;
    }


    @Override
    public void onRefresh() {
        Toast.makeText(getContext(), "Refresh", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 2000);
    }


//    // Recommended method to generate new LayoutDemoFragment
//    // Instead of calling new LayoutDemoFragment() directly
//    static Fragment newInstance(int layout) {
//        Fragment fragment = new HistoryFragment();
//        Bundle bundle = new Bundle();
//        bundle.putInt(LAYOUT_TYPE, layout);
//        fragment.setArguments(bundle);
//
//        return fragment;
//    }
//
//    // To bind ListView and RecyclerView to the corresponding layout
//    private void initializeList(View view) {
//        HistoryListAdaptor adapter = new HistoryListAdaptor(getActivity(), R.layout.list_history, Coupon.getCoupons());
//        linearView = view.findViewById(R.id.history_list_view);
//        linearView.setAdapter(adapter);
//    }
}

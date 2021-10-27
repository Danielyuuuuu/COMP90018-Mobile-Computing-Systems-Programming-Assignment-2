package com.example.dansdistractor.vouchers;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.dansdistractor.R;
import com.example.dansdistractor.utils.FetchUserData;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * @ClassName: VoucherTab
 * @Author: wongchihaul
 * @CreateDate: 2021/9/22 7:03 下午
 */
@RequiresApi(api = Build.VERSION_CODES.O)
public class VoucherTab extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private int status;

    public VoucherTab() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.history_recycler, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh_recycle);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        SharedPreferences sharedPref = getActivity().getSharedPreferences(FetchUserData.ALL_VOUCHERS, Activity.MODE_PRIVATE);
        String activeVouchers = sharedPref.getString(FetchUserData.ACTIVE_VOUCHERS, null);
        Gson gson = new Gson();
        ArrayList<Voucher> voucherList = gson.fromJson(activeVouchers, new TypeToken<ArrayList<Voucher>>() {
        }.getType());


        VoucherRecycleAdaptor adaptor = new VoucherRecycleAdaptor(voucherList, R.layout.voucher_card_flip, status);
        RecyclerView recyclerView = view.findViewById(R.id.demo_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adaptor);

        return view;
    }


    @Override
    public void onRefresh() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
                ViewGroup vg = requireActivity().findViewById(R.id.voucher_main_layout);
                vg.invalidate();
                Toast.makeText(getContext(), "Refresh Vouchers", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void setStatus(int status) {
        this.status = status;
    }
}

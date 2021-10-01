package com.example.dansdistractor.vouchers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.example.dansdistractor.R;

public class VoucherActivity extends AppCompatActivity implements  SwipeRefreshLayout.OnRefreshListener{
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher);


        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh_recycle);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        VoucherRecycleAdaptor adaptor = new VoucherRecycleAdaptor(Voucher.getVouchers(), R.layout.voucher_card);
        RecyclerView recyclerView = findViewById(R.id.demo_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adaptor);

    }

    @Override
    public void onRefresh() {
        Toast.makeText(this, "Refresh", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 2000);
    }
}
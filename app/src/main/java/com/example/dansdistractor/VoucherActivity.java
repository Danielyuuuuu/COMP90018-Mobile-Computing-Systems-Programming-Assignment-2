package com.example.dansdistractor;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.example.dansdistractor.vouchers.VoucherTabAdapter;
import com.google.android.material.tabs.TabLayout;

public class VoucherActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    TabLayout tabLayout;
    ViewPager viewPager;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher);


//        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh_recycle);
//        mSwipeRefreshLayout.setOnRefreshListener(this);
//
//        VoucherRecycleAdaptor adaptor = new VoucherRecycleAdaptor(Voucher.getVouchers(), R.layout.voucher_card);
//        RecyclerView recyclerView = findViewById(R.id.demo_recycler);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(adaptor);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        tabLayout.addTab(tabLayout.newTab().setText("Activate"));
        tabLayout.addTab(tabLayout.newTab().setText("Inactivate"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        final VoucherTabAdapter adapter = new VoucherTabAdapter(this, getSupportFragmentManager(),
                tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @Override
    public void onRefresh() {
        Toast.makeText(this, "VoucherActivity", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 2000);
    }
}
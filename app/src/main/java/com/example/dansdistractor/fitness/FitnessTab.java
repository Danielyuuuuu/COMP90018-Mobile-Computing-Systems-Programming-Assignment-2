package com.example.dansdistractor.fitness;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.dansdistractor.R;
import com.example.dansdistractor.utils.ChartStyle;

/**
 * @ClassName: FitnessTab
 * @Description: //TODO
 * @Author: wongchihaul
 * @CreateDate: 2021/9/22 7:00 下午
 */
public class FitnessTab extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    protected int TYPE;

    public FitnessTab() {
        // Set to WEEK Tab by default
        TYPE = ChartStyle.WEEK;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_recycler, container, false);

        mSwipeRefreshLayout = view.findViewById(R.id.swiperefresh_recycle);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        FitnessRecycleAdaptor adaptor = new FitnessRecycleAdaptor(Fitness.getFitness(), R.layout.fitness_card, TYPE);
        RecyclerView recyclerView = view.findViewById(R.id.demo_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adaptor);

        return view;
    }


    @Override
    public void onRefresh() {
        Toast.makeText(getContext(), "Refresh", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(() -> mSwipeRefreshLayout.setRefreshing(false), 2000);
    }
}

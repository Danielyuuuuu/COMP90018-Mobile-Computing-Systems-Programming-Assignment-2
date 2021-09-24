package com.example.dansdistractor;/**
 * Created by wongchihaul on 2021/9/22
 */

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 *
 * @ClassName: Football
 * @Description:    //TODO
 * @Author: wongchihaul
 * @CreateDate: 2021/9/22 7:00 下午
 */
public class Football extends Fragment {
    public Football() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_list_history, container, false);
//        ListView listView = view.findViewById(R.id.history_list_view_bak);
//
//        FitnessListAdaptor adaptor = new FitnessListAdaptor(requireActivity(), R.layout.fitness_list, Fitness.getFitness());
//        listView.setAdapter(adaptor);

        View view = inflater.inflate(R.layout.fitness_recycle, container, false);
        FitnessRecycleAdaptor adapter = new FitnessRecycleAdaptor(Fitness.getFitness(), R.layout.fitness_list);
        RecyclerView recyclerView = view.findViewById(R.id.demo_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);


        return view;
    }

}

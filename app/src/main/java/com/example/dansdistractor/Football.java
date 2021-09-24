package com.example.dansdistractor;/**
 * Created by wongchihaul on 2021/9/22
 */

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

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
        View view = inflater.inflate(R.layout.fragment_list_history, container, false);
        ListView listView = view.findViewById(R.id.history_list_view_bak);

        FitnessListAdaptor adaptor = new FitnessListAdaptor(requireActivity(), R.layout.fitness_list, Fitness.getFitness());
        listView.setAdapter(adaptor);

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//                Fitness fitness = (Fitness) adapterView.getItemAtPosition(i);
//                Toast.makeText(getContext(), fitness.getFitnessCategory(), Toast.LENGTH_SHORT).show();
//            }
//        });
        return view;
    }

}

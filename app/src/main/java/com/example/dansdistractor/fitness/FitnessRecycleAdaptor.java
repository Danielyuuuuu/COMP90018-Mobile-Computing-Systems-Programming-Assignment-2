package com.example.dansdistractor.fitness;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dansdistractor.R;
import com.example.dansdistractor.utils.ChartStyle;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: FitnessRecycleAdapter
 * @Description: //TODO
 * @Author: wongchihaul
 * @CreateDate: 2021/9/24 11:46 下午
 */
public class FitnessRecycleAdaptor extends RecyclerView.Adapter {

    private final int resourceId;
    private final List<Fitness> fitnessList;


    public FitnessRecycleAdaptor(List<Fitness> _fitnessList, int _resourceId) {
        resourceId = _resourceId;
        fitnessList = _fitnessList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resourceId, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyViewHolder mvh = (MyViewHolder) holder;
        onBindViewHolder(mvh, position);
    }


    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Fitness fitness = fitnessList.get(position);
        holder.icon.setImageResource(fitness.getIcon());
        holder.category.setText(fitness.getCategory());
        holder.number.setText(String.valueOf(fitness.getNumber()));
//        initLineChart(holder.chart);
        initBarChart(holder.chart);


    }


    @Override
    public int getItemCount() {
        return fitnessList.size();
    }

    private void initLineChart(LineChart chart) {
        List<Entry> entries = new ArrayList<>();
        entries.add(new Entry(1, 9));
        entries.add(new Entry(2, 3));
        entries.add(new Entry(3, 5));
        entries.add(new Entry(4, 15));
        entries.add(new Entry(5, 2));
        entries.add(new Entry(6, 4));
        entries.add(new Entry(7, 8));
        LineDataSet dataSet = new LineDataSet(entries, "Label"); // add entries to dataset
        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.invalidate(); // refresh

    }

    private void initBarChart(BarChart chart) {
        //Add data
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(1, 9));
        entries.add(new BarEntry(2, 3));
        entries.add(new BarEntry(3, 5));
        entries.add(new BarEntry(4, 15));
        entries.add(new BarEntry(5, 2));
        entries.add(new BarEntry(6, 4));
        entries.add(new BarEntry(7, 8));
        BarDataSet dataSet = new BarDataSet(entries, "Label"); // add entries to dataset
        BarData barData = new BarData(dataSet);
        chart.setData(barData);
        // set style
        ChartStyle.defaultBarChart(chart);
        // refresh
        chart.invalidate();

    }


    public static class MyViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        ImageView icon;
        TextView category;
        TextView number;
        //        LineChart chart;
        BarChart chart;

        public MyViewHolder(@NonNull View view) {
            super(view);
            icon = (ImageView) view.findViewById(R.id.fitness_icon);
            category = (TextView) view.findViewById(R.id.fitness_category);
            number = (TextView) view.findViewById(R.id.fitness_number);
//            chart = (LineChart) view.findViewById(R.id.line_chart);
            chart = (BarChart) view.findViewById(R.id.bar_chart);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), category.getText(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}

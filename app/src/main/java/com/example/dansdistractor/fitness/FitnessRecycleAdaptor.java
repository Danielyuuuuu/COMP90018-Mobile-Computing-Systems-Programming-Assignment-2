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
import com.example.dansdistractor.utils.DemoData;
import com.example.dansdistractor.utils.MonthData;
import com.example.dansdistractor.utils.WeekData;
import com.example.dansdistractor.utils.YearData;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: FitnessRecycleAdapter
 * @Description: Bridge between Fitness Tab and User's data.
 * @Author: wongchihaul
 * @CreateDate: 2021/9/24 11:46 下午
 */
public class FitnessRecycleAdaptor extends RecyclerView.Adapter {

    public static final int STEPS = 0;
    public static final int SPEED = 1;
    public static final int DISTANCE = 2;
    private int resourceId;
    private List<Fitness> fitnessList;
    private int TYPE;
    // User's data should be store in List<BarEntry>
    private List<BarEntry> userEntries;

    /**
     * This one doesn't specify user's data so use demo data instead.
     *
     * @param _fitnessList
     * @param _resourceId
     * @param _TYPE
     */
    public FitnessRecycleAdaptor(List<Fitness> _fitnessList, int _resourceId, int _TYPE) {
        resourceId = _resourceId;
        fitnessList = _fitnessList;
        TYPE = _TYPE;
    }

    /**
     * @param _fitnessList
     * @param _resourceId
     * @param _TYPE
     * @param _userEntries
     */
    public FitnessRecycleAdaptor(List<Fitness> _fitnessList, int _resourceId, int _TYPE, List<BarEntry> _userEntries) {
        resourceId = _resourceId;
        fitnessList = _fitnessList;
        TYPE = _TYPE;
        userEntries = _userEntries;
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
        DemoData demoData = new WeekData();
        switch (TYPE) {
            case ChartStyle.WEEK:
                break;
            case ChartStyle.MONTH:
                demoData = new MonthData();
                break;
            case ChartStyle.YEAR:
                demoData = new YearData();
                break;
        }
        onBindViewHolder(mvh, position, demoData);

    }

    /**
     * @param holder
     * @param position
     * @param demoData
     */
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position, DemoData demoData) {
        Fitness fitness = fitnessList.get(position);
        holder.icon.setImageResource(fitness.getIcon());
        holder.category.setText(fitness.getCategory());
        DecimalFormat df = new DecimalFormat("#0.00");
//        initLineChart(holder.chart);

        List<BarEntry> entries;

        if (userEntries != null) {

            entries = userEntries;
        } else {
            entries = demoData.demoBarStep();
        }
        switch (position) {
            case STEPS:
                //steps
                int steps = (int) DemoData.getAvg(demoData.demoBarStep());
                holder.number.setText("avg: " + steps);
                break;
            case SPEED:
                //speed
                String speed = df.format(DemoData.getAvg(demoData.demoBarSpeed()));
                holder.number.setText("avg: " + speed + "km/h");
                entries = demoData.demoBarSpeed();
                break;
            case DISTANCE:
                //distance
                String distance = df.format(DemoData.getAvg(demoData.demoBarDistance()));
                holder.number.setText("avg: " + distance + "km");
                entries = demoData.demoBarDistance();
                break;
        }


        initBarChart(holder.chart, entries, position);
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

    /**
     * @param chart
     * @param entries
     * @param category: steps, speed, distance
     */
    private void initBarChart(BarChart chart, List<BarEntry> entries, int category) {
        //Add data
        BarDataSet dataSet = new BarDataSet(entries, "Label"); // add entries to dataset
        BarData barData = new BarData(dataSet);
        chart.setData(barData);
        // set style
        if (category == STEPS) {
            ChartStyle.defaultBarChart(chart, 10000, TYPE);
        } else {
            ChartStyle.defaultBarChart(chart, TYPE);
        }

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
            icon = view.findViewById(R.id.fitness_icon);
            category = view.findViewById(R.id.fitness_category);
            number = view.findViewById(R.id.fitness_number);
//            chart = (LineChart) view.findViewById(R.id.line_chart);
            chart = view.findViewById(R.id.bar_chart);

            view.setOnClickListener(v -> Toast.makeText(v.getContext(), category.getText(), Toast.LENGTH_SHORT).show());

        }
    }
}

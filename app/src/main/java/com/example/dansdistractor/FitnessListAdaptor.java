package com.example.dansdistractor;/**
 * Created by wongchihaul on 2021/9/24
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: FitnessListAdaptor
 * @Description: //TODO
 * @Author: wongchihaul
 * @CreateDate: 2021/9/24 9:52 下午
 */
public class FitnessListAdaptor extends ArrayAdapter<Fitness> {

    private final int resourceId;

    public FitnessListAdaptor(@NonNull Context context, int textViewResourceId,
                              List<Fitness> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Fitness fitness = getItem(position);
        View view = convertView;
        FitnessListAdaptor.ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.icon = (ImageView) view.findViewById(R.id.fitness_icon);
            viewHolder.category = (TextView) view.findViewById(R.id.fitness_category);
            viewHolder.number = (TextView) view.findViewById(R.id.fitness_number);
            viewHolder.chart = (LineChart) view.findViewById(R.id.line_chart);
            view.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) view.getTag();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),
                        ((ViewHolder) view.getTag()).category.getText() + "\n" + ((ViewHolder) view.getTag()).number.getText()
                        , Toast.LENGTH_SHORT).show();
            }
        });

        viewHolder.icon.setImageResource(fitness.getFitnessIcon());
        viewHolder.category.setText(fitness.getFitnessCategory());
        viewHolder.number.setText(String.valueOf(fitness.getFitnessNumber()));
        initChart(viewHolder.chart);

        return view;
    }


    private void initChart(LineChart chart) {
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

    static class ViewHolder {
        ImageView icon;
        TextView category;
        TextView number;
        LineChart chart;
    }
}

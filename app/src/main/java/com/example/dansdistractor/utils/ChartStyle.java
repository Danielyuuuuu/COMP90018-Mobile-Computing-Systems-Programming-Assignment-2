package com.example.dansdistractor.utils;

import android.graphics.Color;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.formatter.ValueFormatter;

/**
 * @ClassName: ChartStyle
 * @Description: //TODO
 * @Author: wongchihaul
 * @CreateDate: 2021/9/25 10:11 下午
 */
public class ChartStyle {

    public final static String[] weeks = new String[]{"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};


    public static void defaultBarChart(BarChart chart) {
        // default goal is 3
        defaultBarChart(chart, 3);
    }

    public static void defaultBarChart(BarChart chart, long goal) {
        Description description = new Description();//描述信息
        description.setEnabled(false);//是否可用
        chart.setDescription(description);//不然会显示默认的 Description。
        chart.setTouchEnabled(true); // 设置是否可以触摸
        chart.setDragEnabled(true);// 是否可以拖拽
        chart.setScaleEnabled(false);// 是否可以缩放

        //x轴配置
        XAxis xAxis = chart.getXAxis();
        xAxis.setEnabled(true);//是否可用
        xAxis.setDrawLabels(true);//是否显示数值
        xAxis.setDrawAxisLine(true);//是否显示坐标线
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//X轴文字显示位置

        ValueFormatter formatter = new ValueFormatter() {

            @Override
            public String getFormattedValue(float value) {
                return weeks[(int) (value - 1)];
            }
        };

        xAxis.setValueFormatter(formatter);

        //左y轴配置
        YAxis lyAxis = chart.getAxisLeft();
        lyAxis.setEnabled(true);//是否可用
        lyAxis.setDrawLabels(false);//是否显示数值
        lyAxis.setDrawAxisLine(false); // 不绘制坐标轴线
        lyAxis.setDrawGridLines(false); // 不绘制网格线

        //限制线
        //用户的goal，需要从预设中获取
        LimitLine ll = new LimitLine(goal, "Goal: " + (int) goal);
        ll.setLineColor(Color.RED);
        ll.setLineWidth(1f);
        ll.setTextColor(Color.RED);
        ll.setTextSize(8f);
        lyAxis.addLimitLine(ll);

        //右Y轴
        YAxis ryAxis = chart.getAxisRight();
        ryAxis.setEnabled(false);


        //标签配置
        Legend legend = chart.getLegend();
        legend.setEnabled(false);//是否可用

    }
}

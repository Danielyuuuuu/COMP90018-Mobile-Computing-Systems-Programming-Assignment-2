package com.example.dansdistractor.utils;

import android.graphics.Color;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DecimalFormat;
import java.util.Calendar;

/**
 * @ClassName: ChartStyle

 * @Author: wongchihaul
 * @CreateDate: 2021/9/25 10:11 下午
 */
public class ChartStyle {

    // One week
    public final static String[] ONE_WEEK = new String[]{"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
    public final static Integer[] ONE_MONTH = getOneMonth();
    // One year
    public final static String[] ONE_YEAR = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sept", "Oct", "Nov", "Dec"};
    public final static int WEEK = 0;
    public final static int MONTH = 1;
    public final static int YEAR = 2;

    // One month
    public static Integer[] getOneMonth() {
        // how many days in this month
        int daysOfMonth = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);
        Integer[] oneMonth = new Integer[daysOfMonth];
        for (int i = 0; i < daysOfMonth; i++) {
            oneMonth[i] = i + 1;
        }
        return oneMonth;
    }

    public static void defaultBarChart(BarChart chart, int TYPE) {
        defaultBarChart(chart, 0, 0, TYPE);
    }

    /**
     * @param chart
     * @param goalSteps    8000 by default
     * @param goalDistance kilometers, 4.8km by default
     * @param TYPE         Weekly, monthly and yearly have their own chart styles
     */
    public static void defaultBarChart(BarChart chart, long goalSteps, double goalDistance, int TYPE) {
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
        xAxis.setDrawGridLines(false);//不绘制网格线

        ValueFormatter formatter = new ValueFormatter() {

            @Override
            public String getFormattedValue(float value) {
                switch (TYPE) {
                    case WEEK:
                        return ONE_WEEK[(int) (value - 1)];
                    case MONTH:
                        return String.valueOf(ONE_MONTH[(int) (value - 1)]);
                    case YEAR:
                        return ONE_YEAR[(int) (value - 1)];
                    default:
                        return null;
                }
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

        // it means we are in STEPS chart
        if (goalSteps > 0) {
            LimitLine ll = new LimitLine(goalSteps, "Goal: " + new DecimalFormat("0,000").format(goalSteps));
            ll.setLineColor(Color.RED);
            ll.setLineWidth(1f);
            ll.setTextColor(Color.RED);
            ll.setTextSize(8f);
            if (goalSteps > lyAxis.mAxisMaximum) {
                lyAxis.setAxisMaximum((int) (goalSteps * 1.2));
            }
            lyAxis.addLimitLine(ll);
            if (TYPE == YEAR) {
                lyAxis.removeLimitLine(ll);
            }
        }

        // it means we are in DISTANCE chart
        if (goalDistance > 0) {
            LimitLine ll = new LimitLine((float) goalDistance, "Goal: " + new DecimalFormat("0.00").format(goalDistance));
            ll.setLineColor(Color.RED);
            ll.setLineWidth(1f);
            ll.setTextColor(Color.RED);
            ll.setTextSize(8f);
            if (goalDistance > lyAxis.mAxisMaximum) {
                lyAxis.setAxisMaximum((float) (goalDistance * 1.2));
            }
            lyAxis.addLimitLine(ll);
            if (TYPE == YEAR) {
                lyAxis.removeLimitLine(ll);
            }
        }


        //右Y轴
        YAxis ryAxis = chart.getAxisRight();
        ryAxis.setEnabled(false);


        //标签配置
        Legend legend = chart.getLegend();
        legend.setEnabled(false);//是否可用

    }
}

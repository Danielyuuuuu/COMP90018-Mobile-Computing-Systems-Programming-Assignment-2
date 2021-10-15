package com.example.dansdistractor.utils;

import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: WeekData

 * @Author: wongchihaul
 * @CreateDate: 2021/10/2 12:14 上午
 */
public class WeekData implements DemoData {

    // Fitness Steps
    public List<BarEntry> demoBarStep() {
        List<BarEntry> entries = new ArrayList<>();
        for (int i = 1; i <= ChartStyle.ONE_WEEK.length; i++) {
            entries.add(new BarEntry(i, DemoData.nextRandomInt(1000, 20000)));
        }
        return entries;
    }

    // Fitness Speed

    public List<BarEntry> demoBarSpeed() {
        List<BarEntry> entries = new ArrayList<>();
        for (int i = 1; i <= ChartStyle.ONE_WEEK.length; i++) {
            entries.add(new BarEntry(i, DemoData.nextRandomFloat(4, 5)));
        }
        return entries;
    }

    // Fitness Distance

    public List<BarEntry> demoBarDistance() {
        List<BarEntry> entries = new ArrayList<>();
        for (int i = 1; i <= ChartStyle.ONE_WEEK.length; i++) {
            entries.add(new BarEntry(i, DemoData.nextRandomFloat(4, 15)));
        }
        return entries;
    }
}

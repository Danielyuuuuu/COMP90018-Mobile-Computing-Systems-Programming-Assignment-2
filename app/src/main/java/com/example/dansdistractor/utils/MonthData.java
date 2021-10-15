package com.example.dansdistractor.utils;

import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: MonthData

 * @Author: wongchihaul
 * @CreateDate: 2021/10/2 12:17 上午
 */
public class MonthData implements DemoData {

    public List<BarEntry> demoBarStep() {
        List<BarEntry> entries = new ArrayList<>();
        for (int i = 1; i <= ChartStyle.ONE_MONTH.length; i++) {
            entries.add(new BarEntry(i, DemoData.nextRandomInt(1000, 20000)));
        }
        return entries;
    }

    public List<BarEntry> demoBarSpeed() {
        List<BarEntry> entries = new ArrayList<>();
        for (int i = 1; i <= ChartStyle.ONE_MONTH.length; i++) {
            entries.add(new BarEntry(i, DemoData.nextRandomFloat(4, 5)));
        }
        return entries;
    }

    public List<BarEntry> demoBarDistance() {
        List<BarEntry> entries = new ArrayList<>();
        for (int i = 1; i <= ChartStyle.ONE_MONTH.length; i++) {
            entries.add(new BarEntry(i, DemoData.nextRandomFloat(4, 15)));
        }
        return entries;
    }
}

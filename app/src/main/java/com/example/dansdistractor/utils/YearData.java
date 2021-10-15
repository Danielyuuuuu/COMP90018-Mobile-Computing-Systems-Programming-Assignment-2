package com.example.dansdistractor.utils;

import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: YearData

 * @Author: wongchihaul
 * @CreateDate: 2021/10/2 12:17 上午
 */
public class YearData implements DemoData {

    public List<BarEntry> demoBarStep() {
        List<BarEntry> entries = new ArrayList<>();
        for (int i = 1; i <= ChartStyle.ONE_YEAR.length; i++) {
            entries.add(new BarEntry(i, DemoData.nextRandomInt(20000, 500000)));
        }
        return entries;
    }


    public List<BarEntry> demoBarSpeed() {
        List<BarEntry> entries = new ArrayList<>();
        for (int i = 1; i <= ChartStyle.ONE_YEAR.length; i++) {
            entries.add(new BarEntry(i, DemoData.nextRandomFloat(4, 5)));
        }
        return entries;
    }


    public List<BarEntry> demoBarDistance() {
        List<BarEntry> entries = new ArrayList<>();
        for (int i = 1; i <= ChartStyle.ONE_YEAR.length; i++) {
            entries.add(new BarEntry(i, DemoData.nextRandomFloat(110, 450)));
        }
        return entries;
    }
}

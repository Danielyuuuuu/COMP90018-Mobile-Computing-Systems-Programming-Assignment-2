package com.example.dansdistractor.utils;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import com.example.dansdistractor.databaseSchema.UserHistorySchema;
import com.github.mikephil.charting.data.BarEntry;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @ClassName: YearData
 * @Author: wongchihaul
 * @CreateDate: 2021/10/2 12:17 上午
 */
@RequiresApi(api = Build.VERSION_CODES.O)
public class YearData implements DemoData {

    HashMap<Integer, ArrayList<UserHistorySchema>> historyMap;

    public YearData(FragmentActivity activity) {
        SharedPreferences sharedPref = activity.getSharedPreferences(FetchUserData.ALL_HISTORY, Activity.MODE_PRIVATE);
        String weeklyHistory = sharedPref.getString(FetchUserData.YEARLY_HISTORY, null);
        Gson gson = new Gson();
        historyMap = gson.fromJson(weeklyHistory, new TypeToken<HashMap<Integer, ArrayList<UserHistorySchema>>>() {
        }.getType());
    }

    public List<BarEntry> demoBarStep() {
        List<BarEntry> entries = new ArrayList<>();
        for (int i = 1; i <= ChartStyle.ONE_YEAR.length; i++) {
            int totalSteps = 0;
            if (historyMap.get(i) != null) {
                totalSteps = historyMap.get(i).stream().mapToInt(history -> history.steps).sum();
            } else {
                // uncomment to use demo data to fill blank
                // totalSteps = DemoData.nextRandomInt(20000, 500000);
            }
            entries.add(new BarEntry(i, totalSteps));
        }
        return entries;
    }


    public List<BarEntry> demoBarSpeed() {
        List<BarEntry> entries = new ArrayList<>();
        for (int i = 1; i <= ChartStyle.ONE_YEAR.length; i++) {
            double avgSpeed = 0;
            double totalDistance = 0;
            long totalElapsedTime = 0;
            ArrayList<UserHistorySchema> DoWUserHistory = historyMap.get(i);
            if (DoWUserHistory != null) {
                // meters to kilometers
                totalDistance = DoWUserHistory
                        .stream()
                        .mapToDouble(history -> history.distance)
                        .sum() / 1000;
                // milliseconds to  hour
                totalElapsedTime = DoWUserHistory
                        .stream()
                        .mapToLong(history -> (history.endDateTime.getTime() - history.startDateTime.getTime()))
                        .sum() / (1000 * 3600);
                avgSpeed = totalElapsedTime == 0 ? 0 : totalDistance / totalElapsedTime;
            } else {
                // uncomment to use demo data to fill blank
                // avgSpeed = DemoData.nextRandomFloat(4, 5);
            }

            entries.add(new BarEntry(i, (float) avgSpeed));
        }
        return entries;
    }


    public List<BarEntry> demoBarDistance() {
        List<BarEntry> entries = new ArrayList<>();
        for (int i = 1; i <= ChartStyle.ONE_YEAR.length; i++) {
            double totalDistance = 0;
            ArrayList<UserHistorySchema> DoWUserHistory = historyMap.get(i);
            if (DoWUserHistory != null) {
                // meters to kilometers
                totalDistance = DoWUserHistory
                        .stream()
                        .mapToDouble(history -> history.distance)
                        .sum() / 1000;
            } else {
                // uncomment to use demo data to fill blank
                // totalDistance = DemoData.nextRandomFloat(110, 450);
            }
            entries.add(new BarEntry(i, (float) totalDistance));
        }
        return entries;
    }
}

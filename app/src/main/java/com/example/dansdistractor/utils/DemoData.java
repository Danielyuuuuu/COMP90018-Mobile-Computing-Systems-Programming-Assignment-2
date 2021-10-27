package com.example.dansdistractor.utils;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.github.mikephil.charting.data.BarEntry;

import java.util.List;
import java.util.Random;

/**
 * @ClassName: DemoData
 * @Description: This class provides static data for development
 * @Author: wongchihaul
 * @CreateDate: 2021/10/1 11:11 下午
 */
@RequiresApi(api = Build.VERSION_CODES.N)
public interface DemoData {
    /**
     * Generate random int/double in [start, end]
     *
     * @param start
     * @param end
     * @return
     */

    static int nextRandomInt(int start, int end) {
        return new Random().nextInt(end) % (end - start + 1) + start;
    }

    static float nextRandomFloat(int start, int end) {
        return start + new Random().nextFloat() * (end - start);
    }


    static float getAvg(List<BarEntry> entries) {
        return (float) entries.stream().mapToDouble(BarEntry::getY).average().getAsDouble();
    }

    List<BarEntry> demoBarStep();

    List<BarEntry> demoBarSpeed();

    List<BarEntry> demoBarDistance();

}

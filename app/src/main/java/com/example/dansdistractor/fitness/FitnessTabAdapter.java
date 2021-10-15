package com.example.dansdistractor.fitness;

import android.content.Context;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.jetbrains.annotations.NotNull;

/**
 * @ClassName: TabAdapter
 * @Author: wongchihaul
 * @CreateDate: 2021/9/22 6:58 下午
 */
public class FitnessTabAdapter extends FragmentPagerAdapter {
    Context context;
    int totalTabs;

    public FitnessTabAdapter(Context c, FragmentManager fm, int totalTabs) {
        super(fm);
        context = c;
        this.totalTabs = totalTabs;
    }

    public FitnessTabAdapter(FragmentManager fm) {
        super(fm);
    }

    @NotNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new Weekly();
            case 1:
                return new Monthly();
            case 2:
                return new Yearly();
            default:
                Toast.makeText(context.getApplicationContext(), "You've found a no man's land", Toast.LENGTH_SHORT).show();
                return new Monthly();
        }
    }
    @Override
    public int getCount() {
        return 3;
    }
}

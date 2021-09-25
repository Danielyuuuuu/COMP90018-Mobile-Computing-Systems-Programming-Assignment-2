package com.example.dansdistractor;/**
 * Created by wongchihaul on 2021/9/22
 */

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.jetbrains.annotations.NotNull;

/**
 * @ClassName: TabAdapter
 * @Description: //TODO
 * @Author: wongchihaul
 * @CreateDate: 2021/9/22 6:58 下午
 */
public class TabAdapter extends FragmentPagerAdapter {
    Context context;
    int totalTabs;

    public TabAdapter(Context c, FragmentManager fm, int totalTabs) {
        super(fm);
        context = c;
        this.totalTabs = totalTabs;
    }

    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @NotNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FitnessTab();
            case 1:
                return new VoucherTab();
            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return 2;
    }
}

package com.example.dansdistractor;/**
 * Created by wongchihaul on 2021/9/22
 */

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @ClassName: MyAdapter
 * @Description:    //TODO
 * @Author: wongchihaul
 * @CreateDate: 2021/9/22 6:58 下午
 */
public class MyAdapter extends FragmentPagerAdapter {
    Context context;
    int totalTabs;
    public MyAdapter(Context c, FragmentManager fm, int totalTabs) {
        super(fm);
        context = c;
        this.totalTabs = totalTabs;
    }

    public  MyAdapter(FragmentManager fm){
        super(fm);
    }

    @NotNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new Football();
            case 1:
                return new Cricket();
            case 2:
                return new NBA();
            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return 3;
    }
}

package com.example.dansdistractor.vouchers;

import android.content.Context;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.jetbrains.annotations.NotNull;

/**
 * @ClassName: TabAdapter
 * @Author: wongchihaul
 * @CreateDate: 2021/9/22 6:58 PM
 */
public class VoucherTabAdapter extends FragmentPagerAdapter {
    Context context;
    int totalTabs;

    public VoucherTabAdapter(Context c, FragmentManager fm, int totalTabs) {
        super(fm);
        context = c;
        this.totalTabs = totalTabs;
    }

    public VoucherTabAdapter(FragmentManager fm) {
        super(fm);
    }

    @NotNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ValidVoucher();
            case 1:
                return new INValidVoucher();
            default:
                Toast.makeText(context.getApplicationContext(), "You've found a no man's land", Toast.LENGTH_SHORT).show();
                return new ValidVoucher();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}

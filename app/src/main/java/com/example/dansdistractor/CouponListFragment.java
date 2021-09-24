package com.example.dansdistractor;
/**
 * Created by wongchihaul on 2021/9/24
 */

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.fragment.app.Fragment;

/**
 * @ClassName: CouponListFragment
 * @Description: //TODO
 * @Author: wongchihaul
 * @CreateDate: 2021/9/24 12:00 下午
 */
public class CouponListFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        CouponListAdaptor adaptor = new CouponListAdaptor(getActivity(), R.layout.coupon_list, Coupon.getCoupons());
        View view = inflater.inflate(R.layout.coupon_list, container, false);
        ListView list = (ListView) view.findViewById(R.id.history_list_view_bak);
        list.setAdapter(adaptor);
        return view;
    }

}

package com.example.dansdistractor;/**
 * Created by wongchihaul on 2021/9/22
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 * @ClassName: HistoryListAdaptor
 * @Description:    //TODO
 * @Author: wongchihaul
 * @CreateDate: 2021/9/22 12:53 AM
 */
public class HistoryListAdaptor extends ArrayAdapter<Coupon> {
    static class ViewHolder {
        @BindView(R.id.coupons_image)
        ImageView image;
        @BindView(R.id.coupons_text)
        TextView text;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    private int resourceId;
    public HistoryListAdaptor(@NonNull Context context, int resource, List<Coupon> coupons) {
        super(context, resource, coupons);
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Coupon coupon = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        //TODO: 点击之后会弹出coupon的详细信息，例如优惠券内容、商家信息、有效期等
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), ((ViewHolder) view.getTag()).text.getText(), Toast.LENGTH_SHORT).show();
            }
        });

        viewHolder.image.setImageResource(coupon.getCouponImage());
        viewHolder.text.setText(coupon.getCouponName());

        return view;
    }
}

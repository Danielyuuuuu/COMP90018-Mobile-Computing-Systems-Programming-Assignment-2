package com.example.dansdistractor;
/**
 * Created by wongchihaul on 2021/9/24
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

/**
 * @ClassName: CouponListAdaptor
 * @Description: //TODO
 * @Author: wongchihaul
 * @CreateDate: 2021/9/24 11:52 上午
 */
public class CouponListAdaptor extends ArrayAdapter<Coupon> {

    private final int resourceId;

    public CouponListAdaptor(@NonNull Context context, int textViewResourceId,
                             List<Coupon> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Coupon coupon = getItem(position);
        View view = convertView;
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.text1st = (TextView) view.findViewById(R.id.coupons_1st_line);
            viewHolder.text2nd = (TextView) view.findViewById(R.id.coupons_2nd_line);
            viewHolder.image = (ImageView) view.findViewById(R.id.coupons_icon);
            view.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) view.getTag();
        //TODO: 点击之后会弹出coupon的详细信息，例如优惠券内容、商家信息、有效期等
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),
                        ((ViewHolder) view.getTag()).text1st.getText() + "\n" + ((ViewHolder) view.getTag()).text2nd.getText()
                        , Toast.LENGTH_SHORT).show();
            }
        });

        viewHolder.image.setImageResource(coupon.getCouponImage());
        viewHolder.text1st.setText(coupon.getCouponName());
        viewHolder.text2nd.setText("Here is description of the coupon");

        return view;
    }

    static class ViewHolder {
        ImageView image;
        TextView text1st;
        TextView text2nd;
    }
}

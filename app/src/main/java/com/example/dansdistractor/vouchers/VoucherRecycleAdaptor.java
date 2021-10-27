package com.example.dansdistractor.vouchers;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dansdistractor.R;
import com.squareup.picasso.Picasso;
import com.wajahatkarim3.easyflipview.EasyFlipView;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: VoucherRecycleAdaptor
 * @Author: wongchihaul
 * @CreateDate: 2021/9/25 6:47 下午
 */
public class VoucherRecycleAdaptor extends RecyclerView.Adapter {

    private final int resourceId;
    private final List<Voucher> voucherList;
    public static final int ACTIVE = 10;
    public static final int INACTIVE = 11;
    private final int status;

    public VoucherRecycleAdaptor(ArrayList<Voucher> _voucherList, int _resourceId, int _status) {
        resourceId = _resourceId;
        voucherList = _voucherList;
        status = _status;

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resourceId, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyViewHolder mvh = (MyViewHolder) holder;
        onBindViewHolder(mvh, position);
    }


    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);


        Voucher voucher = voucherList.get(position);

        //download and show icon via URL link
        Picasso.get().load(voucher.getImageURI()).fit().centerCrop().into(holder.icon);
        Picasso.get().load(voucher.getImageURI()).fit().centerCrop().into(holder.icon_back);

        //set vendor name

        holder.title.setText(voucher.getName());

        // grey out inactive voucher
        if (status == INACTIVE) {
            holder.icon.setColorFilter(filter);
            holder.icon_back.setColorFilter(filter);
        }

        holder.description_back.setText(voucher.getDesc() == null ? "This is an " + voucher.getName() : voucher.getDesc());
    }


    @Override
    public int getItemCount() {
        return voucherList.size();
    }


    public class MyViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        ImageView icon;
        TextView title;
        TextView description;

        ImageView icon_back;
        TextView title_back;
        TextView description_back;

        EasyFlipView myEasyFlipView;

        public MyViewHolder(@NonNull View view) {
            super(view);
            icon = (ImageView) view.findViewById(R.id.voucher_icon);
            title = (TextView) view.findViewById(R.id.voucher_name);
            description = (TextView) view.findViewById(R.id.voucher_description);

            // --->just for demo
            icon_back = (ImageView) view.findViewById(R.id.voucher_icon_back);
//            title_back = (TextView) view.findViewById(R.id.voucher_name_back);
            description_back = (TextView) view.findViewById(R.id.voucher_description_back);
            // <--- just for demo

            myEasyFlipView = view.findViewById(R.id.easyFlipView);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myEasyFlipView.flipTheView();
                }
            });
        }
    }


}

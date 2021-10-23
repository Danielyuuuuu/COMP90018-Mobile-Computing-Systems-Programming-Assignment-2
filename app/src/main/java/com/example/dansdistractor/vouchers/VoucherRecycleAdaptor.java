package com.example.dansdistractor.vouchers;

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

    public VoucherRecycleAdaptor(ArrayList<Voucher> _voucherList, int _resourceId) {
        resourceId = _resourceId;
        voucherList = _voucherList;
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
        Voucher voucher = voucherList.get(position);
        Picasso.get().load(voucher.getImageURI()).into(holder.icon);
        holder.title.setText(voucher.getName());
//        holder.description.setText(voucher.getDesc() == null ? "Need More Details" : voucher.getDesc());

        // ---> just for demo
        Picasso.get().load(voucher.getImageURI()).into(holder.icon_back);
//        holder.title_back.setText(voucher.getName());
        holder.description_back.setText(voucher.getDesc() == null ? "This is an " + voucher.getName() : voucher.getDesc());
        // <--- just for demo
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
            title_back = (TextView) view.findViewById(R.id.voucher_name_back);
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

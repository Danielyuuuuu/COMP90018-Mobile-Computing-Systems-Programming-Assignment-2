package com.example.dansdistractor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * @ClassName: VoucherRecycleAdaptor
 * @Description: //TODO
 * @Author: wongchihaul
 * @CreateDate: 2021/9/25 6:47 下午
 */
public class VoucherRecycleAdaptor extends RecyclerView.Adapter {

    private final int resourceId;
    private final List<Voucher> voucherList;

    public VoucherRecycleAdaptor(List<Voucher> _voucherList, int _resourceId) {
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
        holder.icon.setImageResource(voucher.getImage());
        holder.title.setText(voucher.getName());
        holder.description.setText(voucher.getDesc() == null ? "Need More Details" : voucher.getDesc());
    }


    @Override
    public int getItemCount() {
        return voucherList.size();
    }


    public static class MyViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        ImageView icon;
        TextView title;
        TextView description;

        public MyViewHolder(@NonNull View view) {
            super(view);
            icon = (ImageView) view.findViewById(R.id.voucher_icon);
            title = (TextView) view.findViewById(R.id.voucher_name);
            description = (TextView) view.findViewById(R.id.voucher_description);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), title.getText(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


}

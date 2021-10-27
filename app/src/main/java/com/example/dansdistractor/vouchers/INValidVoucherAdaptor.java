package com.example.dansdistractor.vouchers;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dansdistractor.R;
import com.example.dansdistractor.utils.FetchUserData;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.wajahatkarim3.easyflipview.EasyFlipView;

import java.util.ArrayList;

/**
 * @ClassName: INValidVoucherAdaptor
 * @Description:
 * @Author: wongchihaul
 * @CreateDate: 2021/10/27 8:22 下午
 */
public class INValidVoucherAdaptor extends RecyclerView.Adapter {

    private final int resourceId;
    private final Fragment tab;
    private ArrayList<Voucher> voucherList;

    public INValidVoucherAdaptor(int _resourceId, Fragment _tab) {
        resourceId = _resourceId;
        tab = _tab;

        // get data from shared preferences
        voucherList = new ArrayList<>();
        Gson gson = new Gson();
        SharedPreferences sharedPref = tab.requireActivity().getSharedPreferences(FetchUserData.ALL_VOUCHERS, Activity.MODE_PRIVATE);
        String INValidVouchers = sharedPref.getString(FetchUserData.LOCAL_VERIFIED_VOUCHERS, "");
        Log.d("INActive", INValidVouchers);
        voucherList = gson.fromJson(INValidVouchers, new TypeToken<ArrayList<Voucher>>() {
        }.getType());
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resourceId, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view, tab);
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
        holder.icon.setColorFilter(filter);
        holder.icon_back.setColorFilter(filter);

        holder.description_back.setText(voucher.getDesc() == null ? "This is an " + voucher.getName() : voucher.getDesc());
    }


    @Override
    public int getItemCount() {
        return voucherList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView title;
        Button verifyButton;
        ImageView icon_back;
        TextView description_back;

        EasyFlipView myEasyFlipView;

        public MyViewHolder(@NonNull View view, Fragment tab) {
            super(view);
            icon = (ImageView) view.findViewById(R.id.voucher_icon);
            title = (TextView) view.findViewById(R.id.voucher_name);
            icon_back = (ImageView) view.findViewById(R.id.voucher_icon_back);
            description_back = (TextView) view.findViewById(R.id.voucher_description_back);
            verifyButton = (Button) view.findViewById(R.id.verify_button);

            verifyButton.setText("VERIFIED");
            verifyButton.setOnClickListener(button -> {
                Toast.makeText(verifyButton.getContext(), title.getText() + " is verified", Toast.LENGTH_SHORT).show();
            });

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


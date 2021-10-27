package com.example.dansdistractor.vouchers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dansdistractor.R;

/**
 * @ClassName: Activate
 * @Description:
 * @Author: wongchihaul
 * @CreateDate: 2021/10/15 11:44 下午
 */
public class ValidVoucher extends VoucherTab {
    public ValidVoucher() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.history_recycler, container, false);
        ValidVoucherAdaptor adaptor = new ValidVoucherAdaptor(R.layout.voucher_card_flip, this);
        RecyclerView recyclerView = view.findViewById(R.id.demo_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adaptor);
        return view;
    }
}

package com.example.dansdistractor.vouchers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dansdistractor.R;

/**
 * @ClassName: Inactivate
 * @Description:
 * @Author: wongchihaul
 * @CreateDate: 2021/10/15 11:44 PM
 */
public class INValidVoucher extends Fragment {
    public INValidVoucher() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.history_recycler, container, false);
        INValidVoucherAdaptor adaptor = new INValidVoucherAdaptor(R.layout.voucher_card_flip, this);
        RecyclerView recyclerView = view.findViewById(R.id.demo_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adaptor);
        return view;
    }
}

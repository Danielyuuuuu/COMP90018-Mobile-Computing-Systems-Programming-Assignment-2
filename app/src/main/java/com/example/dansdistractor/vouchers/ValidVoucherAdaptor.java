package com.example.dansdistractor.vouchers;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.ColorMatrix;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.wajahatkarim3.easyflipview.EasyFlipView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

/**
 * @ClassName: VoucherRecycleAdaptor
 * @Author: wongchihaul
 * @CreateDate: 2021/9/25 6:47 PM
 */
public class ValidVoucherAdaptor extends RecyclerView.Adapter {

    private final int resourceId;
    private final Fragment tab;
    private ArrayList<Voucher> voucherList;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String userID = user.getUid();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference userRef = db.collection("Users").document(userID);


    public ValidVoucherAdaptor(int _resourceId, Fragment _tab) {
        resourceId = _resourceId;
        tab = _tab;

        // get data from shared preferences
        voucherList = new ArrayList<>();
        Gson gson = new Gson();

        SharedPreferences sharedPref = tab.requireActivity().getSharedPreferences(FetchUserData.ALL_VOUCHERS, Activity.MODE_PRIVATE);
        String activeVouchers = sharedPref.getString(FetchUserData.LOCAL_ACTIVE_VOUCHERS, "");
        voucherList = gson.fromJson(activeVouchers, new TypeToken<ArrayList<Voucher>>() {
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


        Voucher voucher = voucherList.get(position);

        //download and show icon via URL link
        Picasso.get().load(voucher.getImageURI()).fit().centerCrop().into(holder.icon);
        Picasso.get().load(voucher.getImageURI()).fit().centerCrop().into(holder.icon_back);

        //set vendor name
        holder.title.setText(voucher.getName());

        holder.description_back.setText(voucher.getDesc() == null ? "This is an " + voucher.getName() : voucher.getDesc());
    }


    @Override
    public int getItemCount() {
        return voucherList.size();
    }


    public class MyViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
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
            verifyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    SharedPreferences sharedPref = verifyButton.getContext().getSharedPreferences(FetchUserData.ALL_VOUCHERS, Activity.MODE_PRIVATE);
                    Gson gson = new Gson();
                    SharedPreferences.Editor editor = sharedPref.edit();

                    // remove clicked voucher and commit
                    String VALID_VOUCHERS = sharedPref.getString(FetchUserData.LOCAL_ACTIVE_VOUCHERS, null);
                    ArrayList<Voucher> validVoucherList = gson.fromJson(VALID_VOUCHERS, new TypeToken<ArrayList<Voucher>>() {
                    }.getType());
                    Iterator<Voucher> iter = validVoucherList.iterator();
                    Voucher verifiedVoucher = null;
                    while (iter.hasNext()) {
                        Voucher voucher = iter.next();
                        if (voucher.getName().equals(title.getText())) {
                            verifiedVoucher = voucher;
                            iter.remove();
                            break;
                        }
                    }
                    editor.remove(FetchUserData.LOCAL_ACTIVE_VOUCHERS).apply();
                    editor.putString(FetchUserData.LOCAL_ACTIVE_VOUCHERS, gson.toJson(validVoucherList)).apply();
                    ArrayList<String> validVoucherIDs = new ArrayList<>();
                    validVoucherList.forEach(v -> validVoucherIDs.add(v.name));
                    userRef.update(
                            "vouchers", validVoucherIDs
                    );


                    // add verified voucher to inactive voucher list and commit
                    String INVALID_VOUCHERS = sharedPref.getString(FetchUserData.LOCAL_VERIFIED_VOUCHERS, null);
                    ArrayList<Voucher> invalidVoucherList = gson.fromJson(INVALID_VOUCHERS, new TypeToken<ArrayList<Voucher>>() {
                    }.getType());
                    if (invalidVoucherList == null) {
                        invalidVoucherList = new ArrayList<>();
                    }
                    if (verifiedVoucher != null) {
                        invalidVoucherList.add(verifiedVoucher);
                    }
                    invalidVoucherList.sort(Comparator.comparing(iv -> iv.name));
                    editor.remove(FetchUserData.LOCAL_VERIFIED_VOUCHERS).apply();
                    editor.putString(FetchUserData.LOCAL_VERIFIED_VOUCHERS, gson.toJson(invalidVoucherList)).apply();

                    ArrayList<String> invalidVoucherIDs = new ArrayList<>();
                    invalidVoucherList.forEach(v -> invalidVoucherIDs.add(v.name));
                    userRef.update(
                            "invalidVouchers", invalidVoucherIDs
                    );
                    Toast.makeText(verifyButton.getContext(), "Verifying " + title.getText() + ". Please exit and re-enter this page to take effect", Toast.LENGTH_SHORT).show();
                }
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

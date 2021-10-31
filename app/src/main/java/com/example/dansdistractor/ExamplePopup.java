package com.example.dansdistractor;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.dansdistractor.databaseSchema.UserSchema;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class ExamplePopup extends AppCompatDialogFragment {
    TextView voucherName;
    ImageView voucherImage;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String userID = user.getUid();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference docRefVoucher;
    private List<String> l = new ArrayList<>();
    TextView desc;

    private MyApplication myApplication;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Random rand = new Random();
        int x = rand.nextInt(3);
        if(x == 1){
            db.collection("Vouchers")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {

                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    l.add(document.getId());
                                }
                                int randomNum = ThreadLocalRandom.current().nextInt(0, l.size());
                                docRefVoucher = db.collection("Vouchers").document(l.get(randomNum));

                                myApplication = (MyApplication) getActivity().getApplicationContext();
                                String newVoucher = l.get(randomNum);
                                myApplication.getMyVouchers().add(String.valueOf(newVoucher));

                                docRefVoucher.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {
                                                voucherName.setText(String.valueOf(document.getData().get("name")));

                                                Log.i("myvouchers", myApplication.getMyVouchers().toString());
                                                Picasso.get().load(String.valueOf(document.getData().get("imageURI"))).fit().centerCrop().into(voucherImage);

                                                db.collection("Users").document(userID)
                                                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onSuccess(@NonNull DocumentSnapshot documentSnapshot) {
                                                        UserSchema userProfile = documentSnapshot.toObject(UserSchema.class);

                                                        if (userProfile != null) {
                                                            ArrayList<String> userVouchers = userProfile.vouchers;

                                                            userVouchers.add(newVoucher);

                                                            db.collection("Users").document(userID)
                                                                    .update("vouchers", userVouchers);
                                                        }

                                                    }
                                                });

                                            }
                                        }
                                    }
                                });
                            }
                        }
                    });
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.layout_popup,null);
            voucherName = view.findViewById(R.id.textView_vouchergot);
            voucherImage = view.findViewById(R.id.voucher_image);


            builder.setView(view)
                    .setTitle("Congratulations!")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
            return builder.create();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.layout_popup,null);
            voucherName = view.findViewById(R.id.textView_vouchergot);
            desc=view.findViewById(R.id.textView_win);
            desc.setText("You almost win a voucher!");
            voucherImage = view.findViewById(R.id.voucher_image);
            Picasso.get().load("https://novaumc.org/wp-content/uploads/2021/04/almost-there-1024x1024.png").fit().centerCrop().into(voucherImage);
            builder.setView(view)
                    .setTitle("Almost...")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
            return builder.create();
        }


    }
}

package com.example.dansdistractor.utils;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dansdistractor.databaseSchema.UserHistorySchema;
import com.example.dansdistractor.databaseSchema.UserSchema;
import com.example.dansdistractor.vouchers.Voucher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * @ClassName: FetchUserDataFromFirebase
 * @Description: Fetch user's data from firebase to avoid data missing caused by asynchronous data fetching.
 * @Author: wongchihaul
 * @CreateDate: 2021/10/26 6:08 下午
 */
@RequiresApi(api = Build.VERSION_CODES.O)
public class FetchUserData {

    // for all data
    private FirebaseUser user;
    private String userID;
    private FirebaseFirestore db;
    private DocumentReference userRef;
    private AppCompatActivity activity;

    //for user's vouchers
    private CollectionReference allVouchersRef;
    private ArrayList<String> userVoucherIDs;
    private ArrayList<Voucher> userVouchers;

    // for user's fitness history
    private CollectionReference userHistoryRef;
    private HashMap<Integer, ArrayList<UserHistorySchema>> weeklyUserHistory;
    private HashMap<Integer, ArrayList<UserHistorySchema>> monthlyUserHistory;
    private HashMap<Integer, ArrayList<UserHistorySchema>> yearlyUserHistory;
    private ArrayList<UserHistorySchema> currentUserHistory;
    private WeekFields weekFields;
    private Calendar c;


    public FetchUserData(AppCompatActivity _activity) {
        // for all data
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        db = FirebaseFirestore.getInstance();
        userRef = db.collection("Users").document(userID);
        activity = _activity;
        fetchUserVouchers();

        //for user's vouchers
        allVouchersRef = db.collection("Vouchers");
        userVoucherIDs = new ArrayList<>();
        userVouchers = new ArrayList<>();

        // for user's fitness history
        userHistoryRef = db.collection("UserHistory");
        weeklyUserHistory = new HashMap<>();
        monthlyUserHistory = new HashMap<>();
        yearlyUserHistory = new HashMap<>();
        currentUserHistory = new ArrayList<>();
        weekFields = WeekFields.of(Locale.getDefault());
        c = Calendar.getInstance();
    }

    /**
     * Fetch vouchers data
     */
    public void Vouchers() {
        SharedPreferences sharedPref = activity.getSharedPreferences("Vouchers", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        // the id of voucher owned by user
        allVouchersRef
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot document : task.getResult()) {
                            String voucherID = document.getId();
                            System.out.println(voucherID);
                            if (userVoucherIDs.contains(voucherID)) {
                                userVouchers.add(document.toObject(Voucher.class));
                            }
                        }
                        Gson gson = new Gson();
                        editor.putString("ActiveVouchers", gson.toJson(userVouchers));
                        editor.apply();
                    }

                });

    }

    /**
     * Fetch fitness data
     */
    public void Fitness() {
        SharedPreferences sharedPref = activity.getSharedPreferences("Fitness", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        LocalDateTime ldt = LocalDateTime.now();
        int thisYear = ldt.getYear();
        int thisMonth = ldt.getMonth().getValue();
        int thisWeek = getWeekOfYear(ldt);

        userHistoryRef
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        task.getResult().forEach(document -> {
                            // find current user's history and group by year
                            if (userID.equals(document.getId())) {
                                UserHistorySchema history = document.toObject(UserHistorySchema.class);
                                Date date = history.endDateTime;
                                // only this year's date will be showed
                                if (date.getYear() == thisYear) {
                                    updateUserHistory(yearlyUserHistory, date.getMonth(), history);

                                    if (date.getMonth() == thisMonth) {
                                        updateUserHistory(monthlyUserHistory, getDayOfMonth(date), history);
                                    }
                                    if (getWeekOfYear(date) == thisWeek) {
                                        updateUserHistory(weeklyUserHistory, getDayOfMonth(date), history);
                                    }
                                }
                            }
                        });
                        Gson gson = new Gson();
                        editor.putString("YearlyHistory", gson.toJson(yearlyUserHistory));
                        editor.putString("MonthlyHistory", gson.toJson(monthlyUserHistory));
                        editor.putString("WeeklyHistory", gson.toJson(weeklyUserHistory));
                        editor.apply();
                    }
                });
    }

    /**
     * Fetch all data from user.
     */
    private void fetchUserVouchers() {
        userRef
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    UserSchema userProfile = documentSnapshot.toObject(UserSchema.class);
                    if (userProfile != null) {
                        userVoucherIDs = userProfile.vouchers;
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(activity, "Unable to obtain user's vouchers", Toast.LENGTH_LONG).show());
    }


    // Helper functions start
    private int getWeekOfYear(LocalDateTime date) {
        int weekNumber = date.get(weekFields.weekOfWeekBasedYear());
        return weekNumber;
    }

    private int getWeekOfYear(Date date) {
        c.setTime(date);
        return c.get(Calendar.WEEK_OF_YEAR);
    }

    private int getDayOfMonth(Date date) {
        c.setTime(date);
        return c.get(Calendar.DAY_OF_MONTH);
    }

    private void updateUserHistory(HashMap<Integer, ArrayList<UserHistorySchema>> historyMap, int timeGroup, UserHistorySchema history) {
        ArrayList<UserHistorySchema> groupedByTimeUserHistory = historyMap.get(timeGroup);
        if (groupedByTimeUserHistory != null) {
            groupedByTimeUserHistory.add(history);
        } else {
            groupedByTimeUserHistory = new ArrayList<>();
            groupedByTimeUserHistory.add(history);
            historyMap.put(timeGroup, groupedByTimeUserHistory);

        }
    }
    // Helper functions end
}

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
 * @CreateDate: 2021/10/26 6:08 PM
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
    private ArrayList<String> userInValidVoucherIDs;
    private ArrayList<Voucher> userInvalidVouchers;

    // for user's fitness history
    private CollectionReference userHistoryRef;
    private HashMap<Integer, ArrayList<UserHistorySchema>> weeklyUserHistory;
    private HashMap<Integer, ArrayList<UserHistorySchema>> monthlyUserHistory;
    private HashMap<Integer, ArrayList<UserHistorySchema>> yearlyUserHistory;
    private ArrayList<UserHistorySchema> currentUserHistory;
    private WeekFields weekFields;
    private Calendar c;

    // for shared preferences
    public static final String ALL_VOUCHERS = String.valueOf("ALL_VOUCHERS".hashCode());
    public static final String REMOTE_ACTIVE_VOUCHERS = String.valueOf("REMOTE_ACTIVE_VOUCHERS".hashCode());
    public static final String REMOTE_VERIFIED_VOUCHERS = String.valueOf("REMOTE_VERIFIED_VOUCHERS".hashCode());
    public static final String LOCAL_ACTIVE_VOUCHERS = String.valueOf("LOCAL_ACTIVE_VOUCHERS".hashCode());
    public static final String LOCAL_VERIFIED_VOUCHERS = String.valueOf("LOCAL_VERIFIED_VOUCHERS".hashCode());
    public static final String ALL_HISTORY = String.valueOf("ALL_HISTORY".hashCode());
    public static final String YEARLY_HISTORY = String.valueOf("YEARLY_HISTORY".hashCode());
    public static final String MONTHLY_HISTORY = String.valueOf("MONTHLY_HISTORY".hashCode());
    public static final String WEEKLY_HISTORY = String.valueOf("WEEKLY_HISTORY".hashCode());


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
        userInValidVoucherIDs = new ArrayList<>();
        userInvalidVouchers = new ArrayList<>();

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
        SharedPreferences sharedPref = activity.getSharedPreferences(ALL_VOUCHERS, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        //reset cache
        editor.remove(REMOTE_ACTIVE_VOUCHERS).remove(REMOTE_VERIFIED_VOUCHERS).apply();
        // the id of voucher owned by user
        allVouchersRef
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        HashMap<String, Voucher> allVouchers = new HashMap<>();
                        task.getResult().forEach(document -> allVouchers.put(document.getId(), document.toObject(Voucher.class)));
                        for (String id : userVoucherIDs) {
                            userVouchers.add(allVouchers.get(id));
                        }
                        for (String id : userInValidVoucherIDs) {
                            userInvalidVouchers.add(allVouchers.get(id));
                        }
                        Gson gson = new Gson();

                        if (!sharedPref.contains(REMOTE_ACTIVE_VOUCHERS)) {
                            editor.putString(REMOTE_ACTIVE_VOUCHERS, gson.toJson(userVouchers));
                            editor.putString(LOCAL_ACTIVE_VOUCHERS, gson.toJson(userVouchers));
                        }
                        if (!sharedPref.contains(REMOTE_VERIFIED_VOUCHERS)) {
                            editor.putString(REMOTE_VERIFIED_VOUCHERS, gson.toJson(userInvalidVouchers));
                            editor.putString(LOCAL_VERIFIED_VOUCHERS, gson.toJson(userInvalidVouchers));
                        }
                        editor.apply();
                    }

                });

    }

    /**
     * Fetch fitness data
     */
    public void Fitness() {
        SharedPreferences sharedPref = activity.getSharedPreferences(ALL_HISTORY, Activity.MODE_PRIVATE);
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
                            if (userID.equals(document.get("user", String.class))) {
                                UserHistorySchema history = document.toObject(UserHistorySchema.class);
                                Date date = history.endDateTime;
                                c.setTime(date);

                                // only this year's date will be showed
                                if (c.get(Calendar.YEAR) == thisYear) {

                                    // don't know why c.get(Calendar.MONTH) return 9 (where date is "Oct 26, 2021")
                                    // hence I plus 1 to it
                                    updateUserHistory(yearlyUserHistory, c.get(Calendar.MONTH) + 1, history);

                                    if (c.get(Calendar.MONTH) + 1 == thisMonth) {
                                        updateUserHistory(monthlyUserHistory, c.get(Calendar.DAY_OF_MONTH), history);
                                    }

                                    if (c.get(Calendar.WEEK_OF_YEAR) == thisWeek) {
                                        // Calendar.DAY_OF_WEEK starts from Sunday, i.e., Sunday returns 1 and Saturday returns 7
                                        // transform it to a start-from-Monday week, i.e., Monday return 1 and Sunday returns 7
                                        int dayOfWeek = (c.get(Calendar.DAY_OF_WEEK) - 1) % 7 == 0 ? 7 : c.get(Calendar.DAY_OF_WEEK) - 1;
                                        updateUserHistory(weeklyUserHistory, dayOfWeek, history);
                                    }
                                }
                            }
                        });
                        Gson gson = new Gson();
                        editor.putString(YEARLY_HISTORY, gson.toJson(yearlyUserHistory));
                        editor.putString(MONTHLY_HISTORY, gson.toJson(monthlyUserHistory));
                        editor.putString(WEEKLY_HISTORY, gson.toJson(weeklyUserHistory));

                        editor.apply();
                    }
                });
    }

    /**
     * Fetch all vouchers from user.
     */
    private void fetchUserVouchers() {
        userRef
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    UserSchema userProfile = documentSnapshot.toObject(UserSchema.class);
                    if (userProfile != null) {
                        userVoucherIDs = userProfile.vouchers == null ? new ArrayList<>() : userProfile.vouchers;
                        userInValidVoucherIDs = userProfile.invalidVouchers == null ? new ArrayList<>() : userProfile.invalidVouchers;
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(activity, "Unable to obtain user's vouchers", Toast.LENGTH_LONG).show());
    }


    // Helper functions start
    private int getWeekOfYear(LocalDateTime date) {
        int weekNumber = date.get(weekFields.weekOfWeekBasedYear());
        return weekNumber;
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

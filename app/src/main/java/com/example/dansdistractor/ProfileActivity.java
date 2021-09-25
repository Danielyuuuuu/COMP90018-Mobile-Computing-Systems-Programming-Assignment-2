package com.example.dansdistractor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{

    private RelativeLayout logout;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        logout = (RelativeLayout) findViewById(R.id.logout);
        logout.setOnClickListener(this);


        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        final TextView username = (TextView) findViewById(R.id.username);
        final TextView useremail = (TextView) findViewById(R.id.useremail);
        final TextView userpoints = (TextView) findViewById(R.id.userpoints);
        final TextView usertotaldistance = (TextView) findViewById(R.id.usertotaldistance);
        final TextView usertotalpins = (TextView) findViewById(R.id.usertotalpins);


        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserSchema userProfile = snapshot.getValue(UserSchema.class);

                if (userProfile != null) {
                    String nameUserProfile = userProfile.name;
                    String emailUserProfile = userProfile.email;
                    int userpointsUserProfile = userProfile.points;
                    int usertotaldistanceUserProfile = userProfile.totaldistance;
                    int usertotalpinsUserProfile = userProfile.usertotalpins;


                    username.setText(nameUserProfile);
                    useremail.setText(emailUserProfile);
                    userpoints.setText(String.valueOf(userpointsUserProfile));
                    usertotaldistance.setText(String.valueOf(usertotaldistanceUserProfile));
                    usertotalpins.setText(String.valueOf(usertotalpinsUserProfile));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "Unable to obtain user info", Toast.LENGTH_LONG).show();
            }
        });


    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.logout:
                userLogout();
                break;
        }

    }

    private void userLogout() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(ProfileActivity.this, MainActivity.class));
    }


}

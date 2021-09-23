package com.example.dansdistractor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

    private Button logout;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        logout = (Button) findViewById(R.id.logout);
        logout.setOnClickListener(this);


        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        final TextView name = (TextView) findViewById(R.id.name);
        final TextView email = (TextView) findViewById(R.id.email);
        final TextView age = (TextView) findViewById(R.id.age);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserSchema userProfile = snapshot.getValue(UserSchema.class);

                if (userProfile != null) {
                    String nameUserProfile = userProfile.name;
                    String emailUserProfile = userProfile.email;
                    String ageUserProfile = userProfile.age;

                    name.setText(nameUserProfile);
                    email.setText(emailUserProfile);
                    age.setText(ageUserProfile);
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

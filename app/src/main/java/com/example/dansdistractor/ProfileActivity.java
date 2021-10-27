package com.example.dansdistractor;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dansdistractor.databaseSchema.UserSchema;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

@RequiresApi(api = Build.VERSION_CODES.O)
public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout logout;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String userID = user.getUid();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference docRefCurrentUser = db.collection("Users").document(userID);
    private ImageView closeProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        logout = (RelativeLayout) findViewById(R.id.logout);
        logout.setOnClickListener(this);

        closeProfile = findViewById(R.id.closeProfile);
        closeProfile.setOnClickListener(this);

        final TextView username = (TextView) findViewById(R.id.username);
        final TextView useremail = (TextView) findViewById(R.id.useremail);
        final TextView userpoints = (TextView) findViewById(R.id.userpoints);
        final TextView usertotaldistance = (TextView) findViewById(R.id.usertotaldistance);
        final TextView usertotalpins = (TextView) findViewById(R.id.usertotalpins);

        docRefCurrentUser.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(@NonNull DocumentSnapshot documentSnapshot) {
                UserSchema userProfile = documentSnapshot.toObject(UserSchema.class);

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
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
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
            case R.id.closeProfile:
                startActivity(new Intent(ProfileActivity.this, NavigationActivity.class));
                break;
        }

    }

    private void userLogout() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(ProfileActivity.this, Login.class));
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, NavigationActivity.class));
    }
}

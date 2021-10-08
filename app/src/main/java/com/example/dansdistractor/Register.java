package com.example.dansdistractor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class Register extends AppCompatActivity implements View.OnClickListener {

    private TextView register;
    private ImageView logo;
    private EditText name, email, password;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        logo = (ImageView) findViewById(R.id.logo);
        logo.setOnClickListener(this);

        register = (TextView) findViewById(R.id.register);
        register.setOnClickListener(this);

        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.logo:
                startActivity(new Intent(this, Login.class));
                break;
            case R.id.register:
                registerUser();
                break;
        }
    }

    private void registerUser() {

        //get all the input value form register form
        String emailInput = email.getText().toString().trim();
        String passwordInput = password.getText().toString().trim();
        String nameInput = name.getText().toString().trim();

        //equal to true when the register form contains error(s)
        boolean incompleteForm = false;

        //Validate if the register form contains error or not
        if (passwordInput.isEmpty()) {
            password.setError("Password is required!");
            password.requestFocus();
            incompleteForm = true;
        }else if (passwordInput.length() < 6) {
            password.setError("Password should have a size of 6 or greater");
            password.requestFocus();
            incompleteForm = true;
        }

        if (emailInput.isEmpty()) {
            email.setError("Email is required!");
            email.requestFocus();
            incompleteForm = true;
        }else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            email.setError("Email is not valid");
            email.requestFocus();
            incompleteForm = true;
        }

        if (nameInput.isEmpty()) {
            name.setError("Full name is required!");
            name.requestFocus();
            incompleteForm = true;
        }

        //terminate the register process when the form is incomplete
        if(incompleteForm) return;

        //start registering
        //Toast.makeText(Register.this, "register start", Toast.LENGTH_LONG).show();

        //set loading icon to visible
        progressBar.setVisibility(View.VISIBLE);

        //connect to firebase authentication modules for register a new user
        mAuth.createUserWithEmailAndPassword(emailInput, passwordInput)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            UserSchema user = new UserSchema(nameInput, emailInput);

                            db.collection("Users").document(mAuth.getCurrentUser().getUid())
                                    .set(user)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(Register.this, "User has been registered successfully!", Toast.LENGTH_LONG).show();
                                            progressBar.setVisibility(View.GONE);

                                            //redirect to profile
                                            startActivity(new Intent(Register.this, ProfileActivity.class));
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(Register.this, "Failed to register! Please try again!", Toast.LENGTH_LONG).show();
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    });
                        } else {
                            Toast.makeText(Register.this, "Failed to register! Please try again!", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}
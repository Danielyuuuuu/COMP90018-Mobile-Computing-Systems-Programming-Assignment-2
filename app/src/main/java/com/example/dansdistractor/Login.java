package com.example.dansdistractor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity implements View.OnClickListener{

    private TextView register;
    private EditText email, password;
    private Button login;

    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(this);

        register = (TextView) findViewById(R.id.register);
        register.setOnClickListener(this);

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.register:
                startActivity(new Intent(this, Register.class));
                break;
            case R.id.login:
                userLogin();
                break;
        }
    }

    //check if user is login in or not on the start of the program
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            startActivity(new Intent(Login.this, ProfileActivity.class));
        }
    }

    //perform login functionality
    private void userLogin() {
        String emailInput = email.getText().toString().trim();
        String passwordInput = password.getText().toString().trim();

        //equal to true when the login form contains error(s)
        boolean incompleteForm = false;

        //Validate if the login form contains error or not
        if (passwordInput.isEmpty()) {
            password.setError("Password is required!");
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

        //terminate the login process when the form is incomplete
        if(incompleteForm) return;

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(emailInput, passwordInput).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //redirect to user profile
                    progressBar.setVisibility(View.GONE);
                    startActivity(new Intent(Login.this, ProfileActivity.class));
                }else{
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(Login.this, "Failed to login! Unrecognised username or password.", Toast.LENGTH_LONG).show();
                }
            }
        });


    }





}
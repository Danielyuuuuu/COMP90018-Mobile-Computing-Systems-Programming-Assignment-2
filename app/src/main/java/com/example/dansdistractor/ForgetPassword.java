package com.example.dansdistractor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword extends AppCompatActivity implements View.OnClickListener{

    private ImageView logo;
    private EditText email;
    private FirebaseAuth mAuth;
    private Button resetpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);


        mAuth = FirebaseAuth.getInstance();

        logo = (ImageView) findViewById(R.id.logo);
        logo.setOnClickListener(this);

        email = (EditText) findViewById(R.id.email);

        resetpassword = (Button) findViewById(R.id.resetpassword);
        resetpassword.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.logo:
                startActivity(new Intent(this, Login.class));
                break;
            case R.id.resetpassword:
                forgetpassword();
                break;
        }
    }

    private void forgetpassword() {

        //get all the input value form register form
        String emailInput = email.getText().toString().trim();

        //equal to true when the register form contains error(s)
        boolean incompleteForm = false;

        if (emailInput.isEmpty()) {
            email.setError("Email is required!");
            email.requestFocus();
            incompleteForm = true;
        }else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            email.setError("Email is not valid");
            email.requestFocus();
            incompleteForm = true;
        }

        //terminate the register process when the form is incomplete
        if(incompleteForm) return;

        FirebaseAuth.getInstance().sendPasswordResetEmail(emailInput)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ForgetPassword.this, "Password reset email sent", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(ForgetPassword.this, "Email not found", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
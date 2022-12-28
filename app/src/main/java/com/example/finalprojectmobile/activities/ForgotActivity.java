package com.example.finalprojectmobile.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.finalprojectmobile.R;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotActivity extends AppCompatActivity {
    EditText emailField;
    Button submitButton;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        emailField = findViewById(R.id.et_email);
        submitButton = findViewById(R.id.submit_btn);

        mAuth = FirebaseAuth.getInstance();

        submitButton.setOnClickListener(v -> {
            String email = emailField.getText().toString();
            if(email.length() == 0){
                Toast.makeText(this, "Email cannot be empty.", Toast.LENGTH_SHORT).show();
            }else{
                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(task -> {
                            if(!task.isSuccessful()){
                                Toast.makeText(this, "Fail to send reset link.", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(this, "Password reset link sent!", Toast.LENGTH_SHORT).show();
                            }
                        });
            }

        });

    }
}
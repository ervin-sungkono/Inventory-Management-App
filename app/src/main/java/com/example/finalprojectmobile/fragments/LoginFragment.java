package com.example.finalprojectmobile.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.finalprojectmobile.R;
import com.example.finalprojectmobile.activities.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginFragment extends Fragment {
    FirebaseAuth mAuth;
    FirebaseUser user;
    EditText emailField, passwordField;
    Button loginButton;

    public LoginFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        emailField = view.findViewById(R.id.et_email);
        passwordField = view.findViewById(R.id.et_password);
        loginButton = view.findViewById(R.id.login_btn);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if(user != null){
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        }

        loginButton.setOnClickListener(v -> {
            String email = emailField.getText().toString();
            String password = passwordField.getText().toString();

            if (email.length() == 0 || password.length() == 0) {
                Toast.makeText(getActivity(), "Email and password cannot be empty!", Toast.LENGTH_SHORT).show();
            }else if (!email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")){
                Toast.makeText(getActivity(), "Invalid email address", Toast.LENGTH_SHORT).show();
            }else {
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(getActivity(), task -> {
                    if(!task.isSuccessful()){
                        Toast.makeText(getActivity(), "Email or password is incorrect.", Toast.LENGTH_SHORT).show();
                    }else{
                        startActivity(new Intent(getActivity(), MainActivity.class));
                        getActivity().finish();
                    }
                });
            }
        });

        return view;
    }
}
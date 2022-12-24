package com.example.finalprojectmobile.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.finalprojectmobile.R;
import com.example.finalprojectmobile.activities.MainActivity;
import com.example.finalprojectmobile.database.UserDB;
import com.example.finalprojectmobile.models.User;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterFragment extends Fragment {
    FirebaseAuth mAuth;
    EditText emailField, usernameField, passwordField, confPassField;
    Button registerButton;
    UserDB userDB;

    public RegisterFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        emailField = view.findViewById(R.id.et_email);
        usernameField = view.findViewById(R.id.et_username);
        passwordField = view.findViewById(R.id.et_password);
        confPassField = view.findViewById(R.id.et_conf_password);
        registerButton = view.findViewById(R.id.register_btn);

        userDB = new UserDB(getActivity());
        mAuth = FirebaseAuth.getInstance();

        registerButton.setOnClickListener(v -> {
            String email = emailField.getText().toString();
            String username = usernameField.getText().toString();
            String password = passwordField.getText().toString();
            String confPass = confPassField.getText().toString();

            if(email.length() == 0 || username.length() == 0 || password.length() == 0){
                Toast.makeText(getActivity(), "All credentials must be filled.", Toast.LENGTH_SHORT).show();
            }else if (!email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")){
                Toast.makeText(getActivity(), "Invalid email address.", Toast.LENGTH_SHORT).show();
            }else if (username.length() < 5){
                Toast.makeText(getActivity(), "Username must be at least 5 characters.", Toast.LENGTH_SHORT).show();
            }else if (password.length() < 7){
                Toast.makeText(getActivity(), "Password must be at least 7 characters.", Toast.LENGTH_SHORT).show();
            }else if (!password.equals(confPass)){
                Toast.makeText(getActivity(), "Password does not match confirm password.", Toast.LENGTH_SHORT).show();
            }else{
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(getActivity(), task -> {
                    if(!task.isSuccessful()){
                        Toast.makeText(getActivity(), "Register failed.", Toast.LENGTH_SHORT).show();
                    }else{
                        mAuth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(getActivity(), task2 -> {
                                    if(!task2.isSuccessful()){
                                        Toast.makeText(getActivity(), "Login failed.", Toast.LENGTH_SHORT).show();
                                    }else{
                                        String id = mAuth.getCurrentUser().getUid();
                                        userDB.insertUser(new User(id, username, email, null));
                                        startActivity(new Intent(getActivity(), MainActivity.class));
                                        getActivity().finish();
                                    }
                                });
                    }
                });
            }
        });

        return view;
    }
}
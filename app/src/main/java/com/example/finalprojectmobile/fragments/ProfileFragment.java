package com.example.finalprojectmobile.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.finalprojectmobile.ImageHelper;
import com.example.finalprojectmobile.R;
import com.example.finalprojectmobile.activities.EditProfileActivity;
import com.example.finalprojectmobile.database.UserDB;
import com.example.finalprojectmobile.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment {
    FirebaseUser currentUser;
    UserDB userDB;
    TextView usernameText, emailText;
    ImageView profilePic;
    Button editProfile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        userDB = new UserDB(getActivity());

        usernameText = view.findViewById(R.id.tv_username);
        emailText = view.findViewById(R.id.tv_email);
        profilePic = view.findViewById(R.id.iv_profile_pic);
        editProfile = view.findViewById(R.id.edit_profile_btn);

        User user = userDB.findUser(currentUser.getUid());
        if(user != null){
            usernameText.setText(user.getUsername());
            emailText.setText(user.getEmail());
            ImageHelper.setImageViewWithByteArray(profilePic, user.getProfilePic());
        }

        editProfile.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), EditProfileActivity.class));
        });

        return view;
    }
}
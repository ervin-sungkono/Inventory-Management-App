package com.example.finalprojectmobile.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.finalprojectmobile.ImageHelper;
import com.example.finalprojectmobile.R;
import com.example.finalprojectmobile.database.UserDB;
import com.example.finalprojectmobile.models.User;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;

public class EditProfileActivity extends AppCompatActivity {
    EditText usernameField, emailField, passwordField;
    ImageView profilePic;
    Button changePicture, submitProfile;
    private byte[] profilePicImage;
    FirebaseAuth mAuth;
    UserDB userDB;
    User user;

    ActivityResultLauncher<Intent> activityResultLaunch = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if(data != null){
                        Uri resultImage = data.getData();
                        profilePic.setImageURI(resultImage);
                        profilePicImage = ImageHelper.getBytesArrayFromURI(resultImage, this);
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mAuth = FirebaseAuth.getInstance();
        userDB = new UserDB(this);

        usernameField = findViewById(R.id.et_username);
        emailField = findViewById(R.id.et_email);
        passwordField = findViewById(R.id.et_password);
        profilePic = findViewById(R.id.iv_profile_pic);
        changePicture = findViewById(R.id.change_pic_btn);
        submitProfile = findViewById(R.id.submit_profile_btn);

        user = userDB.findUser(mAuth.getCurrentUser().getUid());

        if(user != null){
            usernameField.setText(user.getUsername());
            emailField.setText(user.getEmail());
            profilePicImage = user.getProfilePic();
            ImageHelper.setImageViewWithByteArray(profilePic, profilePicImage);
        }

        changePicture.setOnClickListener(v -> {
            Intent iGallery = new Intent(Intent.ACTION_PICK);
            iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResultLaunch.launch(iGallery);
        });

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setTitle("Save your profile changes?")
                .setCancelable(false)
                .setPositiveButton("Save", (dialog, i) -> {
                    editProfile();
                }).setNegativeButton("Cancel", (dialog, i) -> {
                    dialog.cancel();
                });
        AlertDialog alertDialog = alertDialogBuilder.create();

        submitProfile.setOnClickListener(v -> {
            alertDialog.show();
        });
    }



    public void editProfile(){
        String newUsername = usernameField.getText().toString();
        String newEmail = emailField.getText().toString();
        String password = passwordField.getText().toString();
        byte[] newProfilePicImage = profilePicImage;

        if(!newEmail.equals(user.getEmail())){
            if(password.length() > 0){
                AuthCredential credential = EmailAuthProvider
                        .getCredential(mAuth.getCurrentUser().getEmail(), password);
                mAuth.getCurrentUser().reauthenticate(credential)
                        .addOnCompleteListener(task -> {
                            if(!task.isSuccessful()){
                                Toast.makeText(this, "Invalid credentials given", Toast.LENGTH_SHORT).show();
                            }else{
                                mAuth.getCurrentUser().updateEmail(newEmail)
                                        .addOnCompleteListener(task2 -> {
                                            if(!task2.isSuccessful()){
                                                Toast.makeText(this, "Fail to update email", Toast.LENGTH_SHORT).show();
                                            }else{
                                                executeUpdate(user.getId(), newUsername, newEmail, newProfilePicImage);
                                            }
                                        });
                            }
                        });
            }else{
                Toast.makeText(this, "Please fill in current password to re-authenticate.", Toast.LENGTH_SHORT).show();
            }
        }else{
            executeUpdate(user.getId(), newUsername, newEmail, newProfilePicImage);
        }
    }

    public void executeUpdate(String userId, String username, String email, byte[] profilePic){
        if(userDB.updateUser(new User(userId, username, email,profilePic)) > 0){
            Toast.makeText(this, "Profile updated!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(EditProfileActivity.this, MainActivity.class));
            finish();
        }else{
            Toast.makeText(this, "Update Failed", Toast.LENGTH_SHORT).show();
        }
    }
}
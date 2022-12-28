package com.example.finalprojectmobile.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalprojectmobile.ImageHelper;
import com.example.finalprojectmobile.R;
import com.example.finalprojectmobile.activities.ForgotActivity;
import com.example.finalprojectmobile.activities.MainActivity;
import com.example.finalprojectmobile.database.UserDB;
import com.example.finalprojectmobile.models.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


public class LoginFragment extends Fragment {
    FirebaseAuth mAuth;
    FirebaseUser user;
    EditText emailField, passwordField;
    Button loginButton, loginGoogleButton;
    TextView forgotPassLink;
    GoogleSignInClient googleSignInClient;
    UserDB userDB;

    ActivityResultLauncher<Intent> activityResultLaunch = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    Task<GoogleSignInAccount> signInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
                    if(signInAccountTask.isSuccessful()){
                        try{
                            GoogleSignInAccount googleSignInAccount = signInAccountTask.getResult(ApiException.class);
                            if(googleSignInAccount != null){
                                AuthCredential authCredential = GoogleAuthProvider
                                        .getCredential(googleSignInAccount.getIdToken(),null);
                                mAuth.signInWithCredential(authCredential)
                                        .addOnCompleteListener(getActivity(), task -> {
                                            if(!task.isSuccessful()){
                                                Toast.makeText(getActivity(), "Login with Google failed.", Toast.LENGTH_SHORT).show();
                                            }else{
                                                String id = mAuth.getCurrentUser().getUid();
                                                String username = googleSignInAccount.getDisplayName();
                                                String email = googleSignInAccount.getEmail();
                                                byte[] profilePic = ImageHelper.getBytesArrayFromURI(googleSignInAccount.getPhotoUrl(), getActivity());
                                                userDB.insertUser(new User(id, username, email, profilePic));
                                                startActivity(new Intent(getActivity(), MainActivity.class));
                                                getActivity().finish();
                                            }
                                        });
                            }
                        }catch (ApiException e){
                            e.printStackTrace();
                        }
                    }
                }
            });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        emailField = view.findViewById(R.id.et_email);
        passwordField = view.findViewById(R.id.et_password);
        loginButton = view.findViewById(R.id.login_btn);
        loginGoogleButton = view.findViewById(R.id.login_google_btn);
        forgotPassLink = view.findViewById(R.id.forgot_password_link);

        userDB = new UserDB(getActivity());

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if(user != null){
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        }

        forgotPassLink.setOnClickListener(v-> {
            startActivity(new Intent(getActivity(), ForgotActivity.class));
        });

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

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestProfile()
                .build();
        googleSignInClient= GoogleSignIn.getClient(getActivity(), googleSignInOptions);
        loginGoogleButton.setOnClickListener(v -> {
            Intent intent = googleSignInClient.getSignInIntent();
            activityResultLaunch.launch(intent);
        });

        return view;
    }
}
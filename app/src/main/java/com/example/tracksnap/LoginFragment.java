package com.example.tracksnap;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;


public class LoginFragment extends Fragment {
    private EditText loginUsername, loginPassword;
    private Button loginButton;
    private CheckBox rememberMeButton;
    private SharedPreferences sharedPreferences;


    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);

        loginUsername = view.findViewById(R.id.login_username_edit);
        loginPassword = view.findViewById(R.id.login_password_edit);
        loginButton = view.findViewById(R.id.login_complete_btn);
        rememberMeButton = view.findViewById(R.id.rememberMeCheck);

        // Initialize SharedPreferences
        sharedPreferences = requireActivity().getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);

        // Check if Remember Me was previously checked and fill the username and password fields
        if (sharedPreferences.getBoolean("rememberMe", false)) {
            loginUsername.setText(sharedPreferences.getString("username", ""));
            loginPassword.setText(sharedPreferences.getString("password", ""));
            rememberMeButton.setChecked(true);
        }

        // Login Button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateUsername() || !validatePassword()) {

                } else {
                    // Check if Remember Me is checked and save the credentials
                    if (rememberMeButton.isChecked()) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("username", loginUsername.getText().toString().trim());
                        editor.putString("password", loginPassword.getText().toString().trim());
                        editor.putBoolean("rememberMe", true);
                        editor.apply();
                    } else {
                        // If Remember Me is not checked, clear the saved credentials
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.remove("username");
                        editor.remove("password");
                        editor.remove("rememberMe");
                        editor.apply();
                    }

                    checkUser();
                }
            }
        });

        return view;
    }

    public Boolean validateUsername () {
        // Takes the user input value from the login edittext
        String val = loginUsername.getText().toString();
        if (val.isEmpty()) {
            loginUsername.setError("Username cannot be empty");
            return false;
        } else {
            loginUsername.setError(null);
            return true;
        }
    }

    public Boolean validatePassword () {
        // Takes the user input value from the login edittext
        String val = loginPassword.getText().toString();
        if (val.isEmpty()) {
            loginPassword.setError("Password cannot be empty");
            return false;
        } else {
            loginPassword.setError(null);
            return true;
        }
    }

    public void checkUser () {
        String userUsername = loginUsername.getText().toString().trim();
        String userPassword = loginPassword.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("username").equalTo(userUsername);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // The user exists
                if (snapshot.exists()) {
                    loginUsername.setError(null);
                    String passwordFromDB = snapshot.child(userUsername).child("password").getValue(String.class);

                    // Compares the password in the database with the user password that's provided at the time of login
                    if (passwordFromDB.equals(userPassword)) {
                        loginPassword.setError(null);

                        // Passing in the user's username to make it easier when displaying their profile in the profile fragment
                        LoginFragmentDirections.ActionLoginFragmentToProfileFragment action = LoginFragmentDirections.actionLoginFragmentToProfileFragment(userUsername);

                        Navigation.findNavController(view).navigate(action);
                    } else {
                        loginPassword.setError("Invalid Password");
                        loginPassword.requestFocus();
                    }
                } else {
                    loginUsername.setError("User doesn't exist");
                    loginPassword.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
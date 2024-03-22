package com.example.tracksnap;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.compose.ui.graphics.vector.VectorProperty;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URI;
import java.util.UUID;

public class SettingsFragment extends Fragment {

    private static final String PREF_NAME = "MyAppPreferences";
    private static final String DARK_MODE_KEY = "darkMode";
    private static final int PICK_IMAGE_REQUEST = 1;
    public Uri imageUri;
    private Switch darkModeSwitch;
    private TextView versionText;
    private Button editProfilePic;
    private TextView aboutText;
    private Button logoutBtn;
    ImageView profilePic;
    private String currentUsername = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        darkModeSwitch = view.findViewById(R.id.darkModeSwitch);
        versionText = view.findViewById(R.id.versionText);
        aboutText = view.findViewById(R.id.aboutText);
        logoutBtn = view.findViewById(R.id.logoutBtn); // Initialize logout button
        editProfilePic = view.findViewById(R.id.editPicButton);
        profilePic = view.findViewById(R.id.profilePicEdit);

        currentUsername = SettingsFragmentArgs.fromBundle(requireArguments()).getUsername();


        editProfilePic.setOnClickListener(v -> openFileChooser());

        // Set the initial state of the dark mode switch
        darkModeSwitch.setChecked(isDarkModeEnabled());


        // Set up an OnCheckedChangeListener for the dark mode switch
        darkModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Handle dark mode switch changes
                updateDarkModeSetting(isChecked);
                // You can also update the app theme or settings based on the isChecked value
            }
        });

        // Set the app version dynamically (you might want to get this from the app)
        versionText.setText("App Version: 1.0.0");

        // Set click listener for logout button
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Log out the user
                FirebaseAuth.getInstance().signOut();
                // Navigate back to the home fragment
                Navigation.findNavController(v).navigate(R.id.action_settingsFragment_to_homeFragment);
                // Finish the current activity (optional)
                //requireActivity().finish(); // Finish the current activity
            }
        });

        return view;
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK
                && data != null && data.getData() != null) {
            imageUri = data.getData();
            profilePic.setImageURI(imageUri);
            uploadFile();
        }
    }


    private void uploadFile() {
        if (imageUri != null) {
            ProgressDialog progressDialog = new ProgressDialog(requireContext());
            progressDialog.setMessage("Uploading");
            progressDialog.show();

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(currentUsername).child("profilePictures");

            ref.setValue(imageUri.toString())
                    .addOnSuccessListener(aVoid -> {
                        progressDialog.dismiss();
                        Toast.makeText(requireContext(), "Upload successful", Toast.LENGTH_LONG).show();
                    })
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(requireContext(), "Upload failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        e.printStackTrace(); // Print stack trace for debugging
                    });
        } else {
            Toast.makeText(requireContext(), "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isDarkModeEnabled() {
        SharedPreferences preferences = requireActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        // Retrieve the dark mode state, defaulting to false if not found
        return preferences.getBoolean(DARK_MODE_KEY, false);
    }

    private void updateDarkModeSetting(boolean isChecked) {
        SharedPreferences preferences = requireActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        // Save the dark mode state
        preferences.edit().putBoolean(DARK_MODE_KEY, isChecked).apply();
    }
}

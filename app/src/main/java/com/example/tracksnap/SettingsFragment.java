package com.example.tracksnap;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.tracksnap.R;

public class SettingsFragment extends Fragment {

    private static final String PREF_NAME = "MyAppPreferences";
    private static final String DARK_MODE_KEY = "darkMode";

    Switch darkModeSwitch;
    TextView versionText;
    TextView aboutText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        darkModeSwitch = view.findViewById(R.id.darkModeSwitch);
        versionText = view.findViewById(R.id.versionText);
        aboutText = view.findViewById(R.id.aboutText);

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

        return view;
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

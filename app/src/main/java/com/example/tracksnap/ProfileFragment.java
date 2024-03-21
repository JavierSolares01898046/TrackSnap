package com.example.tracksnap;

import android.content.SharedPreferences;
import android.os.Bundle;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ProfileFragment extends Fragment {
    private TextView usernameTxtView;

    private ImageButton setting_btn;
    private TextView bioTxtView;
    private Button moviesWatchedBtn;
    private Button watchlistBtn;
    private Button reviewsBtn;
    private Button friendsBtn;
    private Button homemovie_btn;
    private ImageView profile_pic;
    private String username = "";
    private String otherUsername = "";
    DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        usernameTxtView = view.findViewById(R.id.username_txtview);
        bioTxtView = view.findViewById(R.id.user_bio_txtview);
        moviesWatchedBtn = view.findViewById(R.id.moviesWatched_btn);
        watchlistBtn = view.findViewById(R.id.watchlist_btn);
        reviewsBtn = view.findViewById(R.id.reviews_btn);
        friendsBtn = view.findViewById(R.id.friends_btn);
        homemovie_btn = view.findViewById(R.id.home_movie_btn);
        setting_btn= view.findViewById(R.id.settingsBtn);

        // Obtaining the user's username
        username = ProfileFragmentArgs.fromBundle(requireArguments()).getUsername();
        Toast.makeText(requireContext(), "Current Username: " + username, Toast.LENGTH_SHORT).show();

        profile_pic = view.findViewById(R.id.profilePic);



//        if (otherUsername.equals(username)) {
//            // Enable the Friends button
//            friendsBtn.setEnabled(true);
//        } else {
//            // Disable the Friends button
//            friendsBtn.setEnabled(false);
//        }


        // Displays username at the top of the profile page
        usernameTxtView.setText(username);

        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(username);

        // Retrieving the user's information from the database
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    HelperClass user = snapshot.getValue(HelperClass.class);
                    if (user != null) {
                        bioTxtView.setText(user.getBio());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Buttons to navigate to each part of the user's profile page
        moviesWatchedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_moviesWatchedFragment);
            }
        });

        watchlistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_watchlistFragment);
            }
        });

        reviewsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_reviewsFragment);
            }
        });

        friendsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileFragmentDirections.ActionProfileFragmentToFriendsFragment action = ProfileFragmentDirections.actionProfileFragmentToFriendsFragment(username);
                Navigation.findNavController(view).navigate(action);
            }
        });

        homemovie_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileFragmentDirections.ActionProfileFragmentToMovieHomeFragment action = ProfileFragmentDirections.actionProfileFragmentToMovieHomeFragment(username);
                Navigation.findNavController(view).navigate(action);
            }
        });

        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileFragmentDirections.ActionProfileFragmentToSettingsFragment action = ProfileFragmentDirections.actionProfileFragmentToSettingsFragment(username);
                Navigation.findNavController(view).navigate(action);
            }
        });


        loadProfilePictureFromSharedPreferences();

        return view;
    }


//    public void onDataChange(@NonNull DataSnapshot snapshot) {
//        if (snapshot.exists()) {
//            HelperClass user = snapshot.getValue(HelperClass.class);
//            if (user != null) {
//                bioTxtView.setText(user.getBio());
//                String profilePicUrl = user.getProfilePicUrl();
//                if (profilePicUrl != null && !profilePicUrl.isEmpty()) {
//                    // Load the profile picture into the ImageView using Glide or Picasso
//                    Glide.with(requireContext())
//                            .load(profilePicUrl)
//                            .into(profile_pic);
//                }
//            }
//        }
//    }


    private void setupProfilePictureListener() {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(username).child("profilePictures");
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String profilePicUrl = dataSnapshot.getValue(String.class);
                    if (profilePicUrl != null && !profilePicUrl.isEmpty()) {
                        // Save to SharedPreferences
                        saveProfilePictureUrl(profilePicUrl);

                        // Load the profile picture into the ImageView
                        Glide.with(requireContext())
                                .load(profilePicUrl)
                                .circleCrop()
                                .into(profile_pic);
                    }
                    else {
                        Glide.with(requireContext())
                                .load(R.drawable.defaultuser) // Assuming default user image is stored in resources
                                .circleCrop()
                                .into(profile_pic);
                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("ProfileFragment", "Failed to load profile picture.", databaseError.toException());
            }
        });
    }

    private void saveProfilePictureUrl(String url) {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("AppSettings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("profilePictureUrl", url);
        editor.apply();
    }

    private void loadProfilePictureFromSharedPreferences() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("AppSettings", Context.MODE_PRIVATE);
        String profilePicUrl = sharedPreferences.getString("profilePictureUrl", "");
        if (!profilePicUrl.isEmpty()) {
            Glide.with(requireContext())
                    .load(profilePicUrl)
                    .circleCrop()
                    .into(profile_pic);
        } else {
            // If the URL is not found in SharedPreferences, setup the Firebase listener to fetch it
            setupProfilePictureListener();
        }
    }
}
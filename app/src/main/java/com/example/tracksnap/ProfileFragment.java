package com.example.tracksnap;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ProfileFragment extends Fragment {
    private TextView usernameTxtView;
    private TextView bioTxtView;
    private Button moviesWatchedBtn;
    private Button watchlistBtn;
    private Button reviewsBtn;
    private Button friendsBtn;
    private String username = "";
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

        // Obtaining the user's username
        username = ProfileFragmentArgs.fromBundle(requireArguments()).getUsername();

        // Displays username at the top of the profile page
        usernameTxtView.setText("WELCOME " + username);

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
                Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_friendsFragment);
            }
        });


        return view;
    }

}
package com.example.tracksnap;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UsersProfileFragment extends Fragment {
    private TextView usernameTxtView;
    private TextView bioTxtView;
    private Button addBtn;
    private Button blockBtn;
    private String otherUsername = "";
    private String currUsername = "";
    DatabaseReference databaseReference;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users_profile, container, false);

        usernameTxtView = view.findViewById(R.id.username_txtview);
        bioTxtView = view.findViewById(R.id.user_bio_txtview);
        addBtn = view.findViewById(R.id.add_btn);
        blockBtn = view.findViewById(R.id.block_btn);

        // Obtaining the current username
        currUsername = UsersProfileFragmentArgs.fromBundle(requireArguments()).getCurrentUsername();
        Toast.makeText(requireContext(), "Current Username: " + currUsername, Toast.LENGTH_SHORT).show();

        // Obtaining the username of the person that was searched
        otherUsername = UsersProfileFragmentArgs.fromBundle(requireArguments()).getOtherUsername();
        Toast.makeText(requireContext(), "Other Username: " + otherUsername, Toast.LENGTH_SHORT).show();

        // Displays username at the top of the profile page
        usernameTxtView.setText(otherUsername);

        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(otherUsername);

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

        String senderUsername = currUsername;
        String receiverUsername = otherUsername;
        // Creating a unique ID for the request
        String requestId = senderUsername + "_" + receiverUsername;

        // Create a new node under friendRequests for the request
//        databaseReference.child("friendRequests").child(requestId).setValue(new FriendRequest(senderUsername, receiverUsername, "pending"));



        return view;
    }
}
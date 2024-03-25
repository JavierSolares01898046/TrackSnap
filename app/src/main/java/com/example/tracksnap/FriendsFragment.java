package com.example.tracksnap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class FriendsFragment extends Fragment {
//    private EditText searchbarEdtTxt;
//    private TextView friendslistTextView;
//    private RecyclerView friendsRecyclerView;
//    private TextView pendinglistTextView;
//    private RecyclerView pendingRecyclerView;
//    private LinearLayout searchresult_layout;
//    private DatabaseReference reference;
//    private FriendsAdapter pendingAdapter;
    private List<Friends> pendingList = new ArrayList<>();
    private List<Friends> friendsList = new ArrayList<>();
    private String username = "";
    private ImageButton searchBtn;
    private ImageButton backBtn;
    private FriendsAdapter pendingAdapter;
    private FriendsAdapter friendsAdapter;
    private DatabaseReference databaseReference;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);

        searchBtn = view.findViewById(R.id.search_imgBtn);
        backBtn = view.findViewById(R.id.back_arrow_imgBtn);

        // Obtaining the user's username
        username = FriendsFragmentArgs.fromBundle(requireArguments()).getUsername();
        Toast.makeText(requireContext(), "Current Username: " + username, Toast.LENGTH_SHORT).show();

//        backBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Navigation.findNavController(view).navigate(R.id.action_friendsFragment_to_profileFragment);
//            }
//        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FriendsFragmentDirections.ActionFriendsFragmentToSearchFragment action = FriendsFragmentDirections.actionFriendsFragmentToSearchFragment(username);
                Navigation.findNavController(view).navigate(action);
//                Navigation.findNavController(view).navigate(R.id.action_friendsFragment_to_searchFragment);
            }
        });


        databaseReference = FirebaseDatabase.getInstance().getReference().child("friendRequests");

        ////////////////// FRIENDS LIST RECYCLERVIEW //////////////////
        // Initialize RecyclerView and its adapter
        RecyclerView friendsRecyclerView = view.findViewById(R.id.friends_recyclerview);
        friendsAdapter = new FriendsAdapter(friendsList, username);
        friendsRecyclerView.setAdapter(friendsAdapter);

        // Set LinearLayoutManager for RecyclerView
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(requireContext());
        friendsRecyclerView.setLayoutManager(layoutManager1);

        // Fetch pending requests from Firebase and update UI
        fetchFriends();
        ////////////////// FRIENDS LIST RECYCLERVIEW //////////////////


        ////////////////// PENDING LIST RECYCLERVIEW //////////////////
        // Initialize RecyclerView and its adapter
        RecyclerView pendingRecyclerView = view.findViewById(R.id.pending_recyclerview);
        pendingAdapter = new FriendsAdapter(pendingList, username);
        pendingRecyclerView.setAdapter(pendingAdapter);

        // Set LinearLayoutManager for RecyclerView
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(requireContext());
        pendingRecyclerView.setLayoutManager(layoutManager2);

        // Fetch pending requests from Firebase and update UI
        fetchPendingRequests();
        ////////////////// PENDING LIST RECYCLERVIEW //////////////////




        return view;
    }

    private void fetchPendingRequests() {
        Log.d("FirebaseData", "FIRST CALL OF FUNCTION");
        // Assuming 'databaseReference' points to your 'friendRequests' node
        Query pendingRequestsQuery = databaseReference.orderByChild("status").equalTo("pending");
        pendingRequestsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Clear the existing pendingList before adding new items
                pendingList.clear();

                // Iterate through each child node under 'friendRequests'
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Get the sender and receiver from Firebase
                    String sender = snapshot.child("sender").getValue(String.class);
                    String receiver = snapshot.child("receiver").getValue(String.class);

                    Log.d("FirebaseData", "SENDER: " + sender);
                    Log.d("FirebaseData", "RECEIVER: " + receiver);

                    // Check if the current username is equal to sender or receiver
                    if (sender != null && receiver != null && (sender.equals(username) || receiver.equals(username))) {
                        String pendingFriendName = sender.equals(username) ? receiver : sender;

                        // Create a new Friends object and add it to the pendingList
                        Friends friend = new Friends(pendingFriendName, R.drawable.defaultuser);
                        pendingList.add(friend);
                        Log.d("FriendsFragment", "Pending List Size: " + pendingList.size());
                        pendingAdapter.notifyDataSetChanged();
                    }
                }

                // Notify the adapter or update UI as needed
                // adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle potential errors
//                Log.e(TAG, "Error fetching pending requests: " + databaseError.getMessage());
            }
        });
    }


    private void fetchFriends() {
        // Assuming 'databaseReference' points to your 'friendRequests' node
        Query friendsQuery = databaseReference.orderByChild("status").equalTo("friends");
        friendsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Clear the existing friendsList before adding new items
                friendsList.clear();

                // Iterate through each child node under 'friendRequests'
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Get the sender and receiver from the snapshot
                    String sender = snapshot.child("sender").getValue(String.class);
                    String receiver = snapshot.child("receiver").getValue(String.class);

                    // Check if the sender or receiver matches the current user
                    if (sender != null && sender.equals(username)) {
                        // If the sender is the current user, add receiver as friend
                        Friends friend = new Friends(receiver, R.drawable.defaultuser);
                        friendsList.add(friend);
                    } else if (receiver != null && receiver.equals(username)) {
                        // If the receiver is the current user, add sender as friend
                        Friends friend = new Friends(sender, R.drawable.defaultuser);
                        friendsList.add(friend);
                    }
                }

                // Update the RecyclerView adapter after adding friends
                friendsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle potential errors
                Log.e("FriendsFragment", "Error fetching friends: " + databaseError.getMessage());
            }
        });
    }

}
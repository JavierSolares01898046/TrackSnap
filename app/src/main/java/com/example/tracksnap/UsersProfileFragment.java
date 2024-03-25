package com.example.tracksnap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.util.Log;
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
    private Button declineBtn;
    private String otherUsername = "";
    private String currUsername = "";
    private String requestId = "";
    private String addBtnTxt = "";
    private String declineBtnTxt = "";
    private Boolean friendStatus = false;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    DatabaseReference friendRequestRef;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users_profile, container, false);

        usernameTxtView = view.findViewById(R.id.username_txtview);
        bioTxtView = view.findViewById(R.id.user_bio_txtview);
        addBtn = view.findViewById(R.id.add_btn);
        declineBtn = view.findViewById(R.id.decline_btn);

        // Making the declineBtn hidden
        declineBtn.setVisibility(View.GONE);

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

        friendRequestRef = FirebaseDatabase.getInstance().getReference("friendRequests");

        // Creating a unique ID for the request based on alphabetical order of usernames
        if (currUsername.compareTo(otherUsername) < 0) {
            requestId = currUsername + "_*_" + otherUsername;
        } else {
            requestId = otherUsername + "_*_" + currUsername;
        }

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBtnTxt = addBtn.getText().toString();
                friendRequestRef.child(requestId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if (!snapshot.exists()) {
//                            Toast.makeText(requireContext(), "snapshot CLICKED", Toast.LENGTH_SHORT).show();
//                            String status = snapshot.child("status").getValue(String.class);
                            if (addBtnTxt.equals("Add")) {
                                Toast.makeText(requireContext(), "ADD BTN CLICKED", Toast.LENGTH_SHORT).show();
                                friendRequestRef.child(requestId).child("status").setValue("pending");
                                sendingFriendRequest(currUsername, otherUsername, requestId);
                            } else if (addBtnTxt.equals("Accept")) {
                                friendRequestRef.child(requestId).child("status").setValue("friends");
                            }
                            setAddButtonStatus(currUsername, otherUsername);
                            Log.d("FirebaseData", "SECOND CALL OF FUNCTION");
//                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle onCancelled
                    }
                });

                Toast.makeText(requireContext(), "BUTTON CLICKED", Toast.LENGTH_SHORT).show();
            }
        });


        declineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                declineBtnTxt = declineBtn.getText().toString();
                friendRequestRef.child(requestId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if (snapshot.exists()) {
                        if (declineBtnTxt.equals("Decline") || declineBtnTxt.equals("Unsend")) {
                            friendRequestRef.child(requestId).child("status").setValue("none");
                        } else if (declineBtnTxt.equals("Remove")) {
                            // Popup to confirm the removal of your friend
                            showDeclinePopup(otherUsername);
                        }
                        setAddButtonStatus(currUsername, otherUsername);
//                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle onCancelled
                    }
                });
                Toast.makeText(requireContext(), "BUTTON CLICKED", Toast.LENGTH_SHORT).show();
            }
        });

        // This function will set the correct text of the addBtn
        setAddButtonStatus(currUsername, otherUsername);
        Log.d("FirebaseData", "FIRST CALL OF FUNCTION");


        return view;
    }

    private void sendingFriendRequest(String userSender, String userReceiver, String requestId) {
//        friendRequestRef = FirebaseDatabase.getInstance().getReference("friendRequests").child(requestId);
        friendRequestRef.child(requestId).child("sender").setValue(userSender);
        friendRequestRef.child(requestId).child("receiver").setValue(userReceiver);
//        friendRequestRef.child("status").setValue("pending");
    }



    // CHECK TO SEE THE STATUS OF THE CURRENT USERNAME

    // 1. If the current user's status = 'decline'/null
    //      a. set addBtn text --> 'Add'

    // 2. If the current user's status = 'pending' && current user == sender
    //      a. set addBtn text --> 'Pending'

    // 3. If the current user's status = 'pending' && current user == receiver
    //      a. set addBtn text --> 'Accept'
    //      b. add another button to decline

    // 4. If the current user's status = 'friends'
    //      a. set addBtn text --> 'Friends'
    public void setAddButtonStatus(String currentUser, String otherUsername) {
        friendRequestRef.child(requestId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("FirebaseData", "RequestId: " + requestId);




                if (snapshot.exists()) {
                    String senderValue = snapshot.child("sender").getValue(String.class);
                    String receiverValue = snapshot.child("receiver").getValue(String.class);
                    String status = snapshot.child("status").getValue(String.class);

                    Log.d("FirebaseData", "Sender: " + senderValue);
                    Log.d("FirebaseData", "Receiver: " + receiverValue);
                    Log.d("FirebaseData", "Status: " + status);
//                    switch (status) {
                    if (status.equals("none")) {
                        addBtn.setText("Add");
                        addBtn.setTextSize(18);
                        addBtn.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blue));
                        declineBtn.setVisibility(View.GONE);
                    } else if(status.equals("pending")) {
                        if (senderValue != null && senderValue.equals(currentUser) && receiverValue.equals(otherUsername)) {      // checking to see if the current user is the one who sent the friend request
                            addBtn.setText("Pending");
                            addBtn.setTextSize(16);
                            addBtn.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.gold));
                            // Making the declineBtn visible
                            declineBtn.setText("Unsend");
                            declineBtn.setTextSize(16);
                            declineBtn.setVisibility(View.VISIBLE);
                        } else if (receiverValue != null && receiverValue.equals(currentUser) && senderValue.equals(otherUsername)) {  // checking to see if the current user is the one who received the friend request
                            addBtn.setText("Accept");
                            addBtn.setTextSize(16);
                            addBtn.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.green));
                            // Making the declineBtn visible
                            declineBtn.setText("Decline");
                            declineBtn.setTextSize(16);
                            declineBtn.setVisibility(View.VISIBLE);

//                            // Add the other user to the "Pending List" in FriendsFragment
//                            Friends friend = new Friends(otherUsername, R.drawable.defaultuser); // Customize the avatar as needed
//                            FriendsAdapter adapter = new FriendsAdapter(friendsList); // Assuming friendsList is accessible here
//                            adapter.addFriend(friend);
                        }
                    } else if (status.equals("friends")) {
                        addBtn.setText("Friends");
                        addBtn.setTextSize(16);
                        addBtn.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.lavender));
                        // Making the declineBtn visible
                        declineBtn.setText("Remove");
                        declineBtn.setTextSize(16);
                        declineBtn.setVisibility(View.VISIBLE);
                    } else {
                        addBtn.setText("Add");
                        addBtn.setTextSize(18);
                        addBtn.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blue));
                        declineBtn.setVisibility(View.GONE);
                    }
//                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }

    public void showDeclinePopup(String otherUserUsername) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Friends");
        String message = "Are you sure you want to remove " + otherUserUsername;
        builder.setMessage(message);
        builder.setPositiveButton("YES!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                friendRequestRef.child(requestId).child("status").setValue("none", new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        if (error == null) {
                            // Data set successfully, update UI
                            setAddButtonStatus(currUsername, otherUsername);
                            Toast.makeText(requireContext(), "User removed as a friend", Toast.LENGTH_SHORT).show();
                        } else {
                            // Handle error if setValue operation fails
                            Toast.makeText(requireContext(), "Error removing user as a friend", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        builder.setNegativeButton("NO!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.show();
    }



}
package com.example.tracksnap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
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
//    private List<Friends> pendingList = new ArrayList<>();
    private String username = "";
    private ImageButton searchBtn;
    private ImageButton backBtn;


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


//        searchbarEdtTxt = view.findViewById(R.id.searchbar_edtTxt);
//        searchresult_layout = view.findViewById(R.id.search_result_linearlayout);
//        friendslistTextView = view.findViewById(R.id.friendslist_txtview);
//        friendsRecyclerView = view.findViewById(R.id.friends_recyclerview);
//        pendinglistTextView = view.findViewById(R.id.pendinglist_txtview);
//        pendingRecyclerView = view.findViewById(R.id.pending_recyclerview);
//
//
//        reference = FirebaseDatabase.getInstance().getReference("users");
//
//        searchbarEdtTxt.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
//                String query = s.toString().trim();
//                if (!query.isEmpty()) {
//                    searchUsers(query);
//                } else {
//                    // Adds back the original views when the searchbar is cleared
//                    searchresult_layout.removeAllViews();
//                    searchresult_layout.addView(friendslistTextView);
//                    searchresult_layout.addView(friendsRecyclerView);
//                    searchresult_layout.addView(pendinglistTextView);
//                    searchresult_layout.addView(pendingRecyclerView);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });


        // Initializing Recyclerview for the 'Pending Request' list

                // * If friendStatus of the user = 'pending', then:
                // 1. Find the user through firebase reference
                // 2. setUsername to the user found
                // 3. setAvatarImg to the user's (default image)
                //




//        pendingAdapter = new FriendsAdapter(pendingList);
//        pendingRecyclerView.setAdapter(pendingAdapter);



//        FriendsAdapter adapter = new FriendsAdapter()
//        RecyclerView recyclerView = view.findViewById(R.id.friends_recyclerview);
//        recyclerView.setAdapter(

        return view;
    }

//    private void searchUsers(String query) {
//        if (!query.isEmpty()) {
//            searchresult_layout.removeAllViews();
//        }
//        reference.orderByChild("username").startAt(query).endAt(query + "\uf8ff").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    String username = dataSnapshot.child("username").getValue(String.class);
//                        addUserBtn(username);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

//    private void addUserBtn(String username) {
//        Button button = new Button(requireContext());
//        button.setText(username);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showProfilePopup(username);
//            }
//        });
//        searchresult_layout.addView(button);
//    }

    // This is the popup that displays the user's profile avatar and username. Gives option for the user to send a friend request
//    private void showProfilePopup (String username) {
//        AlertDialog.Builder mBuilder = new AlertDialog.Builder(requireContext());
//        View mView = getLayoutInflater().inflate(R.layout.friend_item, null);
//
//        ImageButton avatarImgBtn = mView.findViewById(R.id.avatarImg_Btn);
//        TextView usernameTextView = mView.findViewById(R.id.usernameTextView);
//        Button positveBtn = mView.findViewById(R.id.friend_request_btn);
//        Button negativeBtn = mView.findViewById(R.id.cancel_btn);
//
//        avatarImgBtn.setImageResource(R.drawable.defaultuser);
//        usernameTextView.setText(username);
//
//        AlertDialog dialog = mBuilder.setView(mView).create();
//
//        // Reference to both the user searched and current user in the database
//        DatabaseReference userReference = reference.child(username);
//
//
//        // Checking the user's friend status
//        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    // Checks friend status from the database
//                    Boolean friendStatus = snapshot.child("pendingRequestStatus").getValue(Boolean.class);
//                    if (friendStatus != null && friendStatus) {
//                        positveBtn.setText("Pending");
//                        positveBtn.setEnabled(false);
//                    } else {
//                        // Friend request hasn't been sent yet
//                        positveBtn.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                // Call function to send friend request
//                                sendFriendRequest(username, snapshot.getKey());
//
//                                // Dismiss the dialog
//                                dialog.dismiss();
//                            }
//                        });
//
//                        // Check if a friend request already exists
//                        checkFriendRequest(username, snapshot.getKey());
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // Handle error
//            }
//        });
//
//        // Negative button click listener
//        negativeBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });
//
//        dialog.show();
//    }
}
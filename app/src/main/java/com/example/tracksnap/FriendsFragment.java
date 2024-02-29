package com.example.tracksnap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class FriendsFragment extends Fragment {
    private EditText searchbarEdtTxt;
    private LinearLayout searchresult_layout;
    private DatabaseReference reference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);

        searchbarEdtTxt = view.findViewById(R.id.searchbar_edtTxt);
        searchresult_layout = view.findViewById(R.id.search_result_linearlayout);

        reference = FirebaseDatabase.getInstance().getReference("users");

        searchbarEdtTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                String query = s.toString().trim();
                if (!query.isEmpty()) {
                    searchUsers(query);
                } else {
                    searchresult_layout.removeAllViews();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

//        FriendsAdapter adapter = new FriendsAdapter()
//        RecyclerView recyclerView = view.findViewById(R.id.friends_recyclerview);
//        recyclerView.setAdapter(

        return view;
    }

    private void searchUsers(String query) {
        searchresult_layout.removeAllViews();
        reference.orderByChild("username").startAt(query).endAt(query + "\uf8ff").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String username = dataSnapshot.child("username").getValue(String.class);
                    addUserBtn(username);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addUserBtn(String username) {
        Button button = new Button(requireContext());
        button.setText(username);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProfilePopup(username);
            }
        });
        searchresult_layout.addView(button);
    }

    // This is the popup that displays the user's profile avatar and username. Gives option for the user to send a friend request
    private void showProfilePopup (String username) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(requireContext());
        View mView = getLayoutInflater().inflate(R.layout.friend_item, null);

        ImageButton avatarImgBtn = mView.findViewById(R.id.avatarImg_Btn);
        TextView usernameTextView = mView.findViewById(R.id.usernameTextView);
        Button positveBtn = mView.findViewById(R.id.friend_request_btn);
        Button negativeBtn = mView.findViewById(R.id.cancel_btn);

        avatarImgBtn.setImageResource(R.drawable.defaultuser);
        usernameTextView.setText(username);

        AlertDialog dialog = mBuilder.setView(mView).create();

        // Reference to the user's node in the database
        DatabaseReference databaseReference = reference.child(username);

        // Checking the user's friend status
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Checks friend status from the database
                    String friendStatus = snapshot.child("friendStatus").getValue(String.class);
                    if (friendStatus != null && friendStatus.equals("Pending")) {
                        positveBtn.setText("Pending");
                        positveBtn.setEnabled(false);
                    } else {
                        // Friend request hasn't been sent yet
                        positveBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Updating friend status in the database
                                databaseReference.child("friendStatus").setValue("Pending");

                                positveBtn.setText("Pending");
                                positveBtn.setEnabled(false);

                                // Dismiss the dialog
                                dialog.dismiss();

                                // IMPLEMENT THIS FUNCTION TO ADD PENDING REQUEST TO RECYCLERVIEW
                                // addPendingRequest(username);
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });

        negativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                AlertDialog dialog = mBuilder.create();
                dialog.dismiss();
            }
        });

//        mBuilder.setView(mView);
//        AlertDialog dialog = mBuilder.create();
        dialog.show();
    }
}
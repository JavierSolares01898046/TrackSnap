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






//        pendingAdapter = new FriendsAdapter(pendingList);
//        pendingRecyclerView.setAdapter(pendingAdapter);



//        FriendsAdapter adapter = new FriendsAdapter()
//        RecyclerView recyclerView = view.findViewById(R.id.friends_recyclerview);
//        recyclerView.setAdapter(

        return view;
    }

}
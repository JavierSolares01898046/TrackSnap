package com.example.tracksnap;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {
    private ImageButton backBtn;
    SearchAdapter searchAdapter;
    RecyclerView searchRecycler;
    SearchView searchView;
    List<Friends> users = new ArrayList<>();
    private String currUsername = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        backBtn = view.findViewById(R.id.back_arrow_imgBtn);
        searchView = view.findViewById(R.id.searchView);
        searchRecycler = view.findViewById(R.id.search_recyclerview);

        // Obtaining the user's username
        currUsername = SearchFragmentArgs.fromBundle(requireArguments()).getUsername();
        Toast.makeText(requireContext(), "Current Username: " + currUsername, Toast.LENGTH_SHORT).show();

        // Goes back to the FriendsFragment
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_searchFragment_to_friendsFragment);
            }
        });

        searchAdapter = new SearchAdapter(requireContext(), users, currUsername);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext());
        searchRecycler.setLayoutManager(layoutManager);
        searchRecycler.setAdapter(searchAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchFromDB(s, true);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.length() > 2) {
                    searchFromDB(s, false);
                } else {
                    users.clear();
                    searchAdapter.notifyDataSetChanged();
                }
                return false;
            }
        });


        return view;
    }

    private void searchFromDB(String s, boolean match) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
        Query searchQuery;

        if (match) {
            searchQuery = usersRef.orderByChild("username").equalTo(s);
        } else {
            String lowercaseQuery = s.toLowerCase();
            searchQuery = usersRef.orderByChild("username_lowercase")
                    .startAt(lowercaseQuery)
                    .endAt(lowercaseQuery + "\uf8ff");
        }

        searchQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    // Add each user found to the list
                    String username = dataSnapshot.child("username").getValue(String.class);
                    Friends friend = new Friends(username, R.drawable.defaultuser);
                    users.add(friend);
                }
                searchAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}
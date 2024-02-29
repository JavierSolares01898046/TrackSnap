package com.example.tracksnap;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.firebase.database.DatabaseReference;


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


//        FriendsAdapter adapter = new FriendsAdapter()
//        RecyclerView recyclerView = view.findViewById(R.id.friends_recyclerview);
//        recyclerView.setAdapter(


        return view;
    }
}
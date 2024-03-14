package com.example.tracksnap;
//
//import android.os.Bundle;
//
//import androidx.annotation.NonNull;
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.Query;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class ReviewsFragment extends Fragment {
//    private RecyclerView recyclerView;
//    private List<ReviewClass> reviewList;
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_reviews, container, false);
//        recyclerView = view.findViewById(R.id.reviewRecyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        reviewList = new ArrayList<>();
//        return view;
//    }
//
////    private void fetchReviews() {
////        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
////        if (currentUser != null) {
////            String userId = currentUser.getUid();
////            DatabaseReference reviewsRef = FirebaseDatabase.getInstance().getReference().child("user_reviews").child(userId);
////            reviewsRef.addValueEventListener(new ValueEventListener() {
////                @Override
////                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
////                    reviewList.clear();
////                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
////                        ReviewClass review = snapshot.getValue(ReviewClass.class);
////                        reviewList.add(review);
////                    }
////                    reviewAdapter.notifyDataSetChanged();
////                }
////
////                @Override
////                public void onCancelled(@NonNull DatabaseError databaseError) {
////                    // Handle error
////                }
////            });
////        }
////    }
//
//
//
//}
//
//


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tracksnap.ReviewClass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ReviewsFragment extends Fragment {

    private RecyclerView reviewRecyclerView;
    private List<ReviewClass> reviewList;
    private ReviewAdapter reviewAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reviews, container, false);

        reviewRecyclerView = view.findViewById(R.id.reviewRecyclerView);
        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        reviewList = new ArrayList<>();
        reviewAdapter = new ReviewAdapter(reviewList);
        reviewRecyclerView.setAdapter(reviewAdapter);

        fetchReviews();

        return view;
    }

    private void fetchReviews() {
        DatabaseReference reviewsRef = FirebaseDatabase.getInstance().getReference().child("user_reviews");
        reviewsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                reviewList.clear();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot reviewSnapshot : userSnapshot.getChildren()) {
                        ReviewClass review = reviewSnapshot.getValue(ReviewClass.class);
                        if (review != null) {
                            reviewList.add(review);
                        }
                    }
                }
                // Notify the adapter that the data has changed
                reviewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }
}
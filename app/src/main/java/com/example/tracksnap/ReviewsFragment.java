package com.example.tracksnap;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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
    private String username = "";
    private BottomNavigationView bottomNavigation;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reviews, container, false);

        reviewRecyclerView = view.findViewById(R.id.reviewRecyclerView);
        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        reviewList = new ArrayList<>();
        reviewAdapter = new ReviewAdapter(reviewList);
        reviewRecyclerView.setAdapter(reviewAdapter);

        bottomNavigation = view.findViewById(R.id.bottom_navigation);
        // Retrieve username argument here
        username = ReviewsFragmentArgs.fromBundle(getArguments()).getUsername();
        Toast.makeText(requireContext(), "Current Username: " + username, Toast.LENGTH_SHORT).show();
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.action_movie) {
                    navigateToMovieHomeFragment(username); // Pass the username here
                    return true;
                } else if (item.getItemId() == R.id.action_profile) {
                    navigateToProfileFragment(username); // Pass the username here
                    return true;
                }
                return false;
            }
        });

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
                reviewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }

    private void navigateToMovieHomeFragment(String username) {
        ReviewsFragmentDirections.ActionReviewsFragmentToMovieHomeFragment action =
                ReviewsFragmentDirections.actionReviewsFragmentToMovieHomeFragment(username);
        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(action);
    }

    private void navigateToProfileFragment(String username) {
        ReviewsFragmentDirections.ActionReviewsFragmentToProfileFragment action =
                ReviewsFragmentDirections.actionReviewsFragmentToProfileFragment(username);
        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(action);
    }

}

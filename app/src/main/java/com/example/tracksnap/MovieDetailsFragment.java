package com.example.tracksnap;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MovieDetailsFragment extends Fragment {

    private TextView titleTextView, overviewTextView, releaseDateTextView, voteAverageTextView, genreTextView, userView;
    private ImageView imageView;
    private ImageView watchlistBtn;
    private String username;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_details, container, false);

        // Initialize views
        titleTextView = view.findViewById(R.id.title_txt2);
        overviewTextView = view.findViewById(R.id.overview_txt2);
        releaseDateTextView = view.findViewById(R.id.release_txt2);
        voteAverageTextView = view.findViewById(R.id.vote_txt2);
        genreTextView = view.findViewById(R.id.genre_txt2);
        imageView = view.findViewById(R.id.imageView2);
        watchlistBtn = view.findViewById(R.id.addwatchlistButton);
        userView = view.findViewById(R.id.userTextView);

        // Retrieve movie information from arguments
        Bundle bundle = getArguments();
        if (bundle != null) {
            String movieTitle = bundle.getString("title");
            String movieOverview = bundle.getString("overview");
            String movieReleaseDate = bundle.getString("releaseDate");
            double movieVoteAverage = bundle.getDouble("voteAverage");
            String movieImage = bundle.getString("image");
            List<String> movieGenreList = bundle.getStringArrayList("genreList");
            username = bundle.getString("username");
            // Set data to views
            titleTextView.setText(movieTitle);
            overviewTextView.setText(movieOverview);
            releaseDateTextView.setText(movieReleaseDate);
            voteAverageTextView.setText(String.valueOf(movieVoteAverage));
            userView.setText(username);

            // Set genres
            StringBuilder genres = new StringBuilder();
            for (String genre : movieGenreList) {
                genres.append(genre).append(", ");
            }
            // Remove the trailing comma and space
            if (genres.length() > 2) {
                genres.setLength(genres.length() - 2);
            }
            genreTextView.setText(genres.toString());

            // Load image
            Glide.with(requireContext())
                    .load(movieImage)
                    .into(imageView);
        } else {
            Log.e("MovieDetailsFragment", "Bundle is null");
        }


        watchlistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add movie details to Firebase Realtime Database
                DatabaseReference watchlistRef = FirebaseDatabase.getInstance().getReference("users").child(username).child("watchlist");

                String movieTitle = titleTextView.getText().toString();
                String movieImage = getArguments().getString("image");

                watchlistRef.child("movieTitle").setValue(movieTitle);
                watchlistRef.child("movieImage").setValue(movieImage);
                Toast.makeText(requireContext(), "Added to watchlist", Toast.LENGTH_SHORT).show();

                watchlistBtn.setImageResource(R.drawable.checksymbol);
            }
        });


        return view;
    }

}

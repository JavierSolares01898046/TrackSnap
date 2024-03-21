package com.example.tracksnap;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MovieDetailsFragment extends Fragment {

    private TextView titleTextView, overviewTextView, releaseDateTextView, voteAverageTextView, genreTextView;
    private ImageView imageView;

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

        // Retrieve movie information from arguments
        Bundle bundle = getArguments();
        if (bundle != null) {
            String movieTitle = bundle.getString("title");
            String movieOverview = bundle.getString("overview");
            String movieReleaseDate = bundle.getString("releaseDate");
            double movieVoteAverage = bundle.getDouble("voteAverage");
            String movieImage = bundle.getString("image");
            List<String> movieGenreList = bundle.getStringArrayList("genreList");

            // Set data to views
            titleTextView.setText(movieTitle);
            overviewTextView.setText(movieOverview);
            releaseDateTextView.setText(movieReleaseDate);
            voteAverageTextView.setText(String.valueOf(movieVoteAverage));

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

        return view;
    }
}

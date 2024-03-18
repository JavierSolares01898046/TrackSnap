package com.example.tracksnap;

import android.os.AsyncTask;
import android.os.Bundle;
import android.service.autofill.UserData;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MovieHomeFragment extends Fragment {
    private static final String API_URL = "https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=en-US&page=1&sort_by=popularity.desc";
    // Replace this token with your authorization token
    //
    private static final String AUTH_TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIyZmI0MTBjOWY3YzY1MjQzNzc2ODQwMWYxZTM5N2FhMCIsInN1YiI6IjY1Y2ZhN2U4NjBjNzUxMDE0NzY4YmRiMyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.2tABWOxcabMK_KtLyW53vH-iIKJ5-prNoof3towpcfs";

    private String username = "";
    private List<MovieModelClass> movieList;
    private RecyclerView recyclerView;

    private String userDuration;
    private String userGenres;
    private String userRatings;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_home, container, false);


        username = MovieHomeFragmentArgs.fromBundle(requireArguments()).getUsername2();
//        searchUserData(username);
//        Toast.makeText(getContext(), "Username: " + username, Toast.LENGTH_SHORT).show();

        movieList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recyclerView);
        searchUserData(username, new UserDataCallback() {
            @Override
            public void onUserGenresReceived(String userGenres) {
                // Parse userGenres into a list of integers
                List<Integer> genreIds = parseGenreIds(userGenres);

                // Show a toast with the list of genre IDs
                showToastWithGenreIds(genreIds);

                // Modify GetData execution to include genreIds
                GetData getData = new GetData();
                getData.execute(genreIds); // Pass genreIds to AsyncTask
            }
        });

        return view;
    }
    private void showToastWithGenreIds(List<Integer> genreIds) {
        String genreIdsString = genreIdsToString(genreIds);
        Toast.makeText(getContext(), "User Genre IDs: " + genreIdsString, Toast.LENGTH_SHORT).show();
    }
    private String genreIdsToString(List<Integer> genreIds) {
        StringBuilder builder = new StringBuilder();
        for (Integer id : genreIds) {
            builder.append(id).append(", ");
        }
        if (builder.length() > 0) {
            // Remove the trailing comma and space
            builder.setLength(builder.length() - 2);
        }
        return builder.toString();
    }
    private List<Integer> parseGenreIds(String userGenres) {
        List<Integer> genreIds = new ArrayList<>();
        if (userGenres != null && !userGenres.isEmpty()) {
            String[] genreStrings = userGenres.split(",");
            for (String genreString : genreStrings) {
                try {
                    int genreId = Integer.parseInt(genreString.trim());
                    genreIds.add(genreId);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
        return genreIds;
    }

    private String encodeGenreIds(List<Integer> genreIds) {
        StringBuilder encodedIds = new StringBuilder();
        for (int id : genreIds) {
            if (encodedIds.length() > 0) {
                encodedIds.append("%2C");
            }
            encodedIds.append(id);
        }
        return encodedIds.toString();
    }
    public class GetData extends AsyncTask<List<Integer>, Void, String> {

        @Override
        protected String doInBackground(List<Integer>... genreIdsList) {
            OkHttpClient client = new OkHttpClient();
            String encodedGenreIds = encodeGenreIds(genreIdsList[0]);

            String apiUrl = API_URL + "&with_genres=" + encodedGenreIds;


            Request request = new Request.Builder()
                    .url(apiUrl)
                    .get()
                    .addHeader("accept", "application/json")
                    .addHeader("Authorization", AUTH_TOKEN)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    return response.body().string();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s != null) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("results");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject movieObject = jsonArray.getJSONObject(i);

                        MovieModelClass model = new MovieModelClass();
                        model.setId(String.valueOf(movieObject.getInt("id")));
                        model.setTitle(movieObject.getString("title"));
                        model.setOverview(movieObject.getString("overview"));
                        model.setReleaseDate(movieObject.getString("release_date"));
                        model.setVoteAverage(movieObject.getDouble("vote_average"));
                        model.setImage("https://image.tmdb.org/t/p/w500" + movieObject.getString("poster_path"));

                        // Get genre IDs from the JSON object
                        JSONArray genreIdsArray = movieObject.getJSONArray("genre_ids");
                        List<String> genreList = new ArrayList<>();
                        for (int j = 0; j < genreIdsArray.length(); j++) {
                            int genreId = genreIdsArray.getInt(j);
                            String genre = getGenreName(genreId);
                            if (genre != null) {
                                genreList.add(genre);
                            }
                        }
                        model.setGenreList(genreList);

                        movieList.add(model);
                    }

                    PutDataIntoRecyclerView(movieList);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        // Method to get genre name based on ID
        private String getGenreName(int genreId) {
            switch (genreId) {
                case 28:
                    return "Action";
                case 12:
                    return "Adventure";
                case 16:
                    return "Animation";
                case 35:
                    return "Comedy";
                case 80:
                    return "Crime";
                case 99:
                    return "Documentary";
                case 18:
                    return "Drama";
                case 10751:
                    return "Family";
                case 14:
                    return "Fantasy";
                case 36:
                    return "History";
                case 27:
                    return "Horror";
                case 10402:
                    return "Music";
                case 9648:
                    return "Mystery";
                case 10749:
                    return "Romance";
                case 878:
                    return "Science Fiction";
                case 10770:
                    return "TV Movie";
                case 53:
                    return "Thriller";
                case 10752:
                    return "War";
                case 37:
                    return "Western";
                default:
                    return null; // Return null for unknown genre IDs
            }
        }
    }
    public interface UserDataCallback {
        void onUserGenresReceived(String userGenres);
    }
    private void searchUserData(String username, UserDataCallback callback) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
        Query searchQuery = usersRef.orderByChild("username").equalTo(username);

        searchQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    // Retrieve required fields (duration, genres, and ratings)
                    userDuration = dataSnapshot.child("duration").getValue(String.class);
                    userGenres = dataSnapshot.child("genre").getValue(String.class);
                    userRatings = dataSnapshot.child("rating").getValue(String.class);

                    // Parse userGenres to numbered strings
                    if (userGenres != null) {
                        userGenres = convertToNumberedStrings(userGenres);
                    }

                    // Pass userGenres to the callback
                    callback.onUserGenresReceived(userGenres);

                    // Display retrieved data for testing
                    Toast.makeText(getContext(), "User Duration (Max): " + userDuration, Toast.LENGTH_SHORT).show();
                    Toast.makeText(getContext(), "User Genres: " + userGenres, Toast.LENGTH_SHORT).show();
                    Toast.makeText(getContext(), "User Ratings: " + userRatings, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle cancellation or error
            }
        });
    }


    private String convertToNumberedStrings(String genreNames) {
        StringBuilder numberedStrings = new StringBuilder();
        String[] genres = genreNames.split(",");
        for (String genre : genres) {
            switch (genre.trim()) {
                case "Action":
                    numberedStrings.append("28,");
                    break;
                case "Adventure":
                    numberedStrings.append("12,");
                    break;
                case "Animation":
                    numberedStrings.append("16,");
                    break;
                case "Comedy":
                    numberedStrings.append("35,");
                    break;
                case "Crime":
                    numberedStrings.append("80,");
                    break;
                case "Documentary":
                    numberedStrings.append("99,");
                    break;
                case "Drama":
                    numberedStrings.append("18,");
                    break;
                case "Family":
                    numberedStrings.append("10751,");
                    break;
                case "Fantasy":
                    numberedStrings.append("14,");
                    break;
                case "History":
                    numberedStrings.append("36,");
                    break;
                case "Horror":
                    numberedStrings.append("27,");
                    break;
                case "Music":
                    numberedStrings.append("10402,");
                    break;
                case "Mystery":
                    numberedStrings.append("9648,");
                    break;
                case "Romance":
                    numberedStrings.append("10749,");
                    break;
                case "Science Fiction":
                    numberedStrings.append("878,");
                    break;
                case "TV Movie":
                    numberedStrings.append("10770,");
                    break;
                case "Thriller":
                    numberedStrings.append("53,");
                    break;
                case "War":
                    numberedStrings.append("10752,");
                    break;
                case "Western":
                    numberedStrings.append("37,");
                    break;
                default:
                    // Skip unknown genres
                    break;
            }
        }
        // Remove trailing comma if exists
        if (numberedStrings.length() > 0) {
            numberedStrings.deleteCharAt(numberedStrings.length() - 1);
        }
        return numberedStrings.toString();
    }

    private void PutDataIntoRecyclerView(List<MovieModelClass> movieList){
        Adaptery adaptery = new Adaptery(requireContext(), movieList);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));


        recyclerView.setAdapter(adaptery);
    }



}
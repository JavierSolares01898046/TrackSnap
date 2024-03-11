package com.example.tracksnap;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;

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
    private static final String AUTH_TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIyZmI0MTBjOWY3YzY1MjQzNzc2ODQwMWYxZTM5N2FhMCIsInN1YiI6IjY1Y2ZhN2U4NjBjNzUxMDE0NzY4YmRiMyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.2tABWOxcabMK_KtLyW53vH-iIKJ5-prNoof3towpcfs";

    private List<MovieModelClass> movieList;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_home, container, false);
//        setContentView(R.layout.activity_main);

        movieList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recyclerView);

        GetData getData = new GetData();
        getData.execute();

        return view;
    }

    public class GetData extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(API_URL)
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

    private void PutDataIntoRecyclerView(List<MovieModelClass> movieList){
        Adaptery adaptery = new Adaptery(requireContext(), movieList);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));


        recyclerView.setAdapter(adaptery);
    }



}
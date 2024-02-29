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


public class MovieHomeFragment extends Fragment {

    private static String JSON_URL = "https://run.mocky.io/v3/4a324606-1941-441f-952a-503b3bd6b1fe";

    List<MovieModelClass> movieList;
    RecyclerView recyclerView;

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

    public class GetData extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... strings) {

            String current = "";
            try{
                URL url;
                HttpURLConnection urlConnection = null;

                try{
                    url = new URL(JSON_URL);
                    urlConnection = (HttpURLConnection) url.openConnection();

                    InputStream is = urlConnection.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);

                    int data = isr.read();

                    while(data != -1){
                        current += (char) data;
                        data = isr.read();
                    }
                    return current;

                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }finally{
                    if(urlConnection != null){
                        urlConnection.disconnect();
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
            }

            return current;

        }

        @Override
        protected void onPostExecute(String s) {

            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("moviz");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                    String title = jsonObject1.getString("title");
                    String overview = jsonObject1.getString("overview");
                    String duration = jsonObject1.getString("duration");
                    String release = jsonObject1.getString("release");
                    double vote = jsonObject1.getDouble("vote");
                    String rating = jsonObject1.getString("rating");
                    String image = jsonObject1.getString("image");
                    JSONArray genreArray = jsonObject1.getJSONArray("genre");
                    List<String> genreList = new ArrayList<>();
                    for (int j = 0; j < genreArray.length(); j++) {
                        genreList.add(genreArray.getString(j));
                    }

                    MovieModelClass model = new MovieModelClass(title, overview, genreList, duration, release, vote, rating, image);
                    movieList.add(model);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            PutDataIntoRecyclerView(movieList);

        }
    }

    private void PutDataIntoRecyclerView(List<MovieModelClass> movieList){
        Adaptery adaptery = new Adaptery(requireContext(), movieList);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));


        recyclerView.setAdapter(adaptery);
    }



}
package com.example.tracksnap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class Adaptery extends RecyclerView.Adapter<Adaptery.MyViewHolder>{

    private Context mContext;
    private List<MovieModelClass> mData;

    public Adaptery(Context mContext, List<MovieModelClass> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;

        LayoutInflater inflater = LayoutInflater.from(mContext);

        v = inflater.inflate(R.layout.movie_item, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.title.setText(mData.get(position).getTitle());
        holder.overview.setText(mData.get(position).getOverview());
        holder.releaseDate.setText(mData.get(position).getReleaseDate());
        holder.voteAverage.setText(String.valueOf(mData.get(position).getVoteAverage()));

        // Set genres
        StringBuilder genres = new StringBuilder();
        for (String genre : mData.get(position).getGenreList()) {
            genres.append(genre).append(", ");
        }
        // Remove the trailing comma and space
        if (genres.length() > 2) {
            genres.setLength(genres.length() - 2);
        }
        holder.genre.setText(genres.toString());

        Glide.with(mContext)
                .load(mData.get(position).getImage())
                .into(holder.img);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView title;
        TextView overview;
        TextView releaseDate;
        TextView voteAverage;
        TextView genre;
        ImageView img;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title_txt);
            overview = itemView.findViewById(R.id.overview_txt);
            releaseDate = itemView.findViewById(R.id.release_txt);
            voteAverage = itemView.findViewById(R.id.vote_txt);
            genre = itemView.findViewById(R.id.genre_txt);
            img = itemView.findViewById(R.id.imageView);
        }
    }
}
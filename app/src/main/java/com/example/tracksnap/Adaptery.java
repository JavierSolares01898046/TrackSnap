package com.example.tracksnap;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class Adaptery extends RecyclerView.Adapter<Adaptery.MyViewHolder> {

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
        holder.duration.setText(mData.get(position).getDuration());
        holder.release.setText(mData.get(position).getRelease());
        holder.vote.setText(String.valueOf(mData.get(position).getVote()));
        holder.rating.setText(mData.get(position).getRating());


        Glide.with(mContext)
                .load(mData.get(position).getImage())
                .into(holder.image);

        // Set genre - Assuming genre is a List<String> converted to comma-separated string
        holder.genre.setText(TextUtils.join(", ", mData.get(position).getGenre()));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView title;
        TextView overview;
        TextView genre;
        TextView duration;
        TextView release;
        TextView vote;
        TextView rating;
        ImageView image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title_txt);
            overview = itemView.findViewById(R.id.overview_txt);
            genre = itemView.findViewById(R.id.genre_txt);
            duration = itemView.findViewById(R.id.duration_txt);
            release = itemView.findViewById(R.id.release_txt);
            vote = itemView.findViewById(R.id.vote_txt);
            rating = itemView.findViewById(R.id.rating_txt);
            image = itemView.findViewById(R.id.imageView);
        }
    }
}

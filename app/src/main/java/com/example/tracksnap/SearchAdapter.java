package com.example.tracksnap;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    private Context context;
    private List<Friends> users;
    private String currUsername = "";
    ImageView userAvatar;
    TextView usernameTxt;
    RelativeLayout topRel;

    public SearchAdapter(Context context, List<Friends> users, String currentUsername) {
        this.context = context;
        this.users = users;
        this.currUsername = currentUsername;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.ViewHolder holder, int position) {
        final Friends user = users.get(position);
        holder.usernameTxt.setText(user.getUsername());
        // Below loads the default image (FIX THIS LATER ON TO LOAD ANY IMAGE)
        holder.userAvatar.setImageResource(user.getAvatarImg());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchFragmentDirections.ActionSearchFragmentToUsersProfileFragment action = SearchFragmentDirections.actionSearchFragmentToUsersProfileFragment(user.getUsername(), currUsername);

                Navigation.findNavController(view).navigate(action);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView userAvatar;
        TextView usernameTxt;
        RelativeLayout topRel;

        public ViewHolder(View itemView) {
            super(itemView);

            // Find view references
            userAvatar = itemView.findViewById(R.id.avatarImg);
            usernameTxt = itemView.findViewById(R.id.usernameTextView);
            topRel = itemView.findViewById(R.id.top_rel);
        }
    }
}

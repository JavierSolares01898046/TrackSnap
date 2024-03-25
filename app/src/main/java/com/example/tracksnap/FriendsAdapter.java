package com.example.tracksnap;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder> {

    private List<Friends> friendsList;
    private String currUsername = "";

    public FriendsAdapter(List<Friends> friendsList, String currentUsername) {
        this.friendsList = friendsList;
        this.currUsername = currentUsername;
    }

    public void addFriend(Friends friend) {
        friendsList.add(friend);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Friends friends = friendsList.get(position);
        holder.usernameTxtView.setText(friends.getUsername());
        holder.avatarImgView.setImageResource(friends.getAvatarImg());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FriendsFragmentDirections.ActionFriendsFragmentToUsersProfileFragment action = FriendsFragmentDirections.actionFriendsFragmentToUsersProfileFragment(holder.usernameTxtView.getText().toString(), currUsername);
                Navigation.findNavController(view).navigate(action);
            }
        });
    }

    @Override
    public int getItemCount() {
        return friendsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView avatarImgView;
        public TextView usernameTxtView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            avatarImgView = itemView.findViewById(R.id.avatarImg_Btn);
            usernameTxtView = itemView.findViewById(R.id.username_txtview);
        }
    }
}

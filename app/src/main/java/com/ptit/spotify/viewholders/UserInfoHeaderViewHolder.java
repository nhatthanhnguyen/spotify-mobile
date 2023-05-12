package com.ptit.spotify.viewholders;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ptit.spotify.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserInfoHeaderViewHolder extends RecyclerView.ViewHolder {
    public ImageButton buttonBack;
    public CircleImageView imageViewUser;
    public TextView textViewUserName;
    public TextView textViewNumberFollowing;
    public Button buttonEditProfile;

    public UserInfoHeaderViewHolder(@NonNull View itemView) {
        super(itemView);
        buttonBack = itemView.findViewById(R.id.buttonBack);
        imageViewUser = itemView.findViewById(R.id.imageViewUser);
        textViewUserName = itemView.findViewById(R.id.textViewUserName);
        textViewNumberFollowing = itemView.findViewById(R.id.textViewNumberFollowing);
        buttonEditProfile = itemView.findViewById(R.id.buttonEditProfile);
    }
}

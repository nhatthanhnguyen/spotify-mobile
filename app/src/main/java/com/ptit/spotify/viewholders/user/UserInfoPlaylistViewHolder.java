package com.ptit.spotify.viewholders.user;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ptit.spotify.R;

public class UserInfoPlaylistViewHolder extends RecyclerView.ViewHolder {
    public ImageView imageViewPlaylist;
    public TextView textViewPlaylistName;

    public UserInfoPlaylistViewHolder(@NonNull View itemView) {
        super(itemView);
        imageViewPlaylist = itemView.findViewById(R.id.imageViewPlaylist);
        textViewPlaylistName = itemView.findViewById(R.id.textViewPlaylistName);
    }
}

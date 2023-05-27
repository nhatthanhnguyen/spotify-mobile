package com.ptit.spotify.viewholders.playlist;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ptit.spotify.R;

public class PlaylistSettingHeaderViewHolder extends RecyclerView.ViewHolder {
    public ImageView imageViewPlaylist;
    public TextView textViewPlaylistName;
    public TextView textViewUserCreated;

    public PlaylistSettingHeaderViewHolder(@NonNull View itemView) {
        super(itemView);
        imageViewPlaylist = itemView.findViewById(R.id.imageViewPlaylist);
        textViewPlaylistName = itemView.findViewById(R.id.textViewPlaylistName);
        textViewUserCreated = itemView.findViewById(R.id.textViewUserCreated);
    }
}

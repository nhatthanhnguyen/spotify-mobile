package com.ptit.spotify.viewholders.playlist;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ptit.spotify.R;

public class PlaylistSongViewHolder extends RecyclerView.ViewHolder {
    public ImageView imageViewSong;
    public TextView textViewSongName;
    public TextView textViewArtistName;
    public ImageButton buttonLikeSong;
    public ImageButton buttonMoreSettingSong;

    public PlaylistSongViewHolder(@NonNull View itemView) {
        super(itemView);
        imageViewSong = itemView.findViewById(R.id.imageViewSong);
        textViewSongName = itemView.findViewById(R.id.textViewSongName);
        textViewArtistName = itemView.findViewById(R.id.textViewArtistName);
        buttonLikeSong = itemView.findViewById(R.id.buttonLikeSong);
        buttonMoreSettingSong = itemView.findViewById(R.id.buttonMoreSettingSong);
    }
}

package com.ptit.spotify.viewholders.album;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ptit.spotify.R;

public class AlbumInfoSongViewHolder extends RecyclerView.ViewHolder {
    public TextView textViewSongName;
    public ImageView imageViewSongDownloaded;
    public TextView textViewArtistName;
    public ImageButton buttonMoreSettingSong;

    public AlbumInfoSongViewHolder(@NonNull View itemView) {
        super(itemView);
        textViewSongName = itemView.findViewById(R.id.textViewSongName);
        imageViewSongDownloaded = itemView.findViewById(R.id.imageViewSongDownloaded);
        textViewArtistName = itemView.findViewById(R.id.textViewArtistName);
        buttonMoreSettingSong = itemView.findViewById(R.id.buttonMoreSettingSong);
    }
}

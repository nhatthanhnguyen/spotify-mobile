package com.ptit.spotify.viewholders.setting;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ptit.spotify.R;

public class SettingSongHeaderViewHolder extends RecyclerView.ViewHolder {
    public ImageView imageViewSong;
    public TextView textViewSongName;
    public TextView textViewArtistName;
    public TextView textViewAlbumName;

    public SettingSongHeaderViewHolder(@NonNull View itemView) {
        super(itemView);
        imageViewSong = itemView.findViewById(R.id.imageViewSong);
        textViewSongName = itemView.findViewById(R.id.textViewSongName);
        textViewArtistName = itemView.findViewById(R.id.textViewArtistName);
        textViewAlbumName = itemView.findViewById(R.id.textViewAlbumName);
    }
}

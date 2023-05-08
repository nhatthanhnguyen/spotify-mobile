package com.ptit.spotify.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ptit.spotify.R;

public class ArtistSongViewHolder extends RecyclerView.ViewHolder {
    public TextView textViewOrderNumber;
    public ImageView imageViewSong;
    public TextView textViewSongName;
    public TextView textViewNumberOfLikes;
    public ArtistSongViewHolder(@NonNull View itemView) {
        super(itemView);
        textViewOrderNumber = itemView.findViewById(R.id.textViewOrderNumber);
        imageViewSong = itemView.findViewById(R.id.imageViewSong);
        textViewSongName = itemView.findViewById(R.id.textViewSongName);
        textViewNumberOfLikes = itemView.findViewById(R.id.textViewNumberOfLikes);
    }
}

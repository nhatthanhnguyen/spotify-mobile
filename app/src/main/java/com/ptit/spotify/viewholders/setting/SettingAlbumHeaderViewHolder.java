package com.ptit.spotify.viewholders.setting;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ptit.spotify.R;

public class SettingAlbumHeaderViewHolder extends RecyclerView.ViewHolder {
    public ImageView imageViewAlbum;
    public TextView textViewAlbumName;
    public TextView textViewArtistName;

    public SettingAlbumHeaderViewHolder(@NonNull View itemView) {
        super(itemView);
        imageViewAlbum = itemView.findViewById(R.id.imageViewAlbum);
        textViewAlbumName = itemView.findViewById(R.id.textViewAlbumName);
        textViewArtistName = itemView.findViewById(R.id.textViewArtistName);
    }
}

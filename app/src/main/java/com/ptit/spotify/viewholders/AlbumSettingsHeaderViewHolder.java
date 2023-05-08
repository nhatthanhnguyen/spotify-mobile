package com.ptit.spotify.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ptit.spotify.R;

public class AlbumSettingsHeaderViewHolder extends RecyclerView.ViewHolder {
    public ImageView imageViewAlbumImage;
    public TextView textViewAlbumName;
    public TextView textViewArtistName;
    public AlbumSettingsHeaderViewHolder(@NonNull View itemView) {
        super(itemView);
        imageViewAlbumImage = itemView.findViewById(R.id.imageViewAlbumImage);
        textViewAlbumName = itemView.findViewById(R.id.textViewAlbumName);
        textViewArtistName = itemView.findViewById(R.id.textViewArtistName);
    }
}

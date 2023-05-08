package com.ptit.spotify.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ptit.spotify.R;

public class AlbumSettingsOptionViewHolder extends RecyclerView.ViewHolder {
    public ImageView imageViewAlbumSettingsOption;
    public TextView textViewAlbumSettingsOption;
    public AlbumSettingsOptionViewHolder(@NonNull View itemView) {
        super(itemView);
        imageViewAlbumSettingsOption = itemView.findViewById(R.id.imageViewAlbumSettingsOption);
        textViewAlbumSettingsOption = itemView.findViewById(R.id.textViewAlbumSettingsOption);
    }
}

package com.ptit.spotify.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ptit.spotify.R;

public class PlaylistSettingsOptionViewHolder extends RecyclerView.ViewHolder {
    public ImageView imageViewPlaylistSettings;
    public TextView textViewPlaylistSettingsName;

    public PlaylistSettingsOptionViewHolder(@NonNull View itemView) {
        super(itemView);
        imageViewPlaylistSettings = itemView.findViewById(R.id.imageViewPlaylistSettings);
        textViewPlaylistSettingsName = itemView.findViewById(R.id.textViewPlaylistSettingsName);
    }
}

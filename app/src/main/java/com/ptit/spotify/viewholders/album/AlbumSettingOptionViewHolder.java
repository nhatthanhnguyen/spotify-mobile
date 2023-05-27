package com.ptit.spotify.viewholders.album;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ptit.spotify.R;

public class AlbumSettingOptionViewHolder extends RecyclerView.ViewHolder {
    public ImageView imageViewAlbumSettingsOption;
    public TextView textViewAlbumSettingsOption;
    public AlbumSettingOptionViewHolder(@NonNull View itemView) {
        super(itemView);
        imageViewAlbumSettingsOption = itemView.findViewById(R.id.imageViewAlbumSettingsOption);
        textViewAlbumSettingsOption = itemView.findViewById(R.id.textViewAlbumSettingsOption);
    }
}

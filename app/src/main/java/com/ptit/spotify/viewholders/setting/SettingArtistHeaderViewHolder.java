package com.ptit.spotify.viewholders.setting;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ptit.spotify.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingArtistHeaderViewHolder extends RecyclerView.ViewHolder {
    public CircleImageView imageViewArtist;
    public TextView textViewArtistName;

    public SettingArtistHeaderViewHolder(@NonNull View itemView) {
        super(itemView);
        imageViewArtist = itemView.findViewById(R.id.imageViewArtist);
        textViewArtistName = itemView.findViewById(R.id.textViewArtistName);
    }
}

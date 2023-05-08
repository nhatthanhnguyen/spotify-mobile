package com.ptit.spotify.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ptit.spotify.R;

public class ArtistCaptionViewHolder extends RecyclerView.ViewHolder {
    public TextView textViewCaption;
    public ArtistCaptionViewHolder(@NonNull View itemView) {
        super(itemView);
        textViewCaption = itemView.findViewById(R.id.textViewCaption);
    }
}

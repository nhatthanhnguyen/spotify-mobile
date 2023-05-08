package com.ptit.spotify.viewholders;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.ptit.spotify.R;

public class ArtistDescriptionViewHolder extends RecyclerView.ViewHolder {
    public RoundedImageView imageViewArtist;
    public TextView textViewNumberOfLikes;
    public TextView textViewDescription;
    public ImageButton buttonMoreInfo;

    public ArtistDescriptionViewHolder(@NonNull View itemView) {
        super(itemView);
        imageViewArtist = itemView.findViewById(R.id.imageViewArtist);
        textViewNumberOfLikes = itemView.findViewById(R.id.textViewNumberOfLikes);
        textViewDescription = itemView.findViewById(R.id.textViewDescription);
        buttonMoreInfo = itemView.findViewById(R.id.buttonMoreInfo);
    }
}

package com.ptit.spotify.viewholders;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.ptit.spotify.R;

public class ArtistInfoHeaderViewHolder extends RecyclerView.ViewHolder {
    public LinearLayout linearLayoutHeader;
    public ImageButton buttonBack;
    public ImageView imageViewArtist;
    public TextView textViewArtistName;
    public TextView textViewNumberOfLikes;
    public AppCompatButton buttonFollow;
    public ImageButton buttonSettingsArtist;
    public ImageButton buttonPlayArtist;

    public ArtistInfoHeaderViewHolder(@NonNull View itemView) {
        super(itemView);
        linearLayoutHeader = itemView.findViewById(R.id.linearLayoutHeader);
        buttonBack = itemView.findViewById(R.id.buttonBack);
        imageViewArtist = itemView.findViewById(R.id.imageViewArtist);
        textViewArtistName = itemView.findViewById(R.id.textViewArtistName);
        textViewNumberOfLikes = itemView.findViewById(R.id.textViewNumberOfLikes);
        buttonFollow = itemView.findViewById(R.id.buttonFollow);
        buttonSettingsArtist = itemView.findViewById(R.id.buttonSettingsArtist);
        buttonPlayArtist = itemView.findViewById(R.id.buttonPlayArtist);
    }
}

package com.ptit.spotify.viewholders.search;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ptit.spotify.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchItemViewHolder extends RecyclerView.ViewHolder {
    public ImageView imageView;
    public CircleImageView imageViewCircle;
    public TextView textViewTitle;
    public TextView textViewDescription;
    public ImageButton buttonMoreSetting;

    public SearchItemViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imageView);
        imageViewCircle = itemView.findViewById(R.id.imageViewCircle);
        textViewTitle = itemView.findViewById(R.id.textViewTitle);
        textViewDescription = itemView.findViewById(R.id.textViewDescription);
        buttonMoreSetting = itemView.findViewById(R.id.buttonMoreSetting);
    }
}

package com.ptit.spotify.viewholders.home;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ptit.spotify.R;

public class HomeCardViewHolder extends RecyclerView.ViewHolder {
    public LinearLayout linearLayoutCard;
    public ImageView imageView;
    public TextView textViewTitle, textViewDescription;

    public HomeCardViewHolder(@NonNull View itemView) {
        super(itemView);
        linearLayoutCard = itemView.findViewById(R.id.linearLayoutCard);
        imageView = itemView.findViewById(R.id.imageView);
        textViewTitle = itemView.findViewById(R.id.textViewTitle);
        textViewDescription = itemView.findViewById(R.id.textViewDescription);
    }
}

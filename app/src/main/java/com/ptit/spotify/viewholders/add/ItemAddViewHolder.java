package com.ptit.spotify.viewholders.add;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ptit.spotify.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ItemAddViewHolder extends RecyclerView.ViewHolder {
    public ImageView imageView;
    public CircleImageView imageViewCircle;
    public TextView textViewTitle;
    public TextView textViewDescription;
    public ImageButton buttonAdd;

    public ItemAddViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imageView);
        imageViewCircle = itemView.findViewById(R.id.imageViewCircle);
        textViewTitle = itemView.findViewById(R.id.textViewTitle);
        textViewDescription = itemView.findViewById(R.id.textViewDescription);
        buttonAdd = itemView.findViewById(R.id.buttonAdd);
    }
}

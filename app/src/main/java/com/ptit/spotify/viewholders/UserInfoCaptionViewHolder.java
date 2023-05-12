package com.ptit.spotify.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ptit.spotify.R;

public class UserInfoCaptionViewHolder extends RecyclerView.ViewHolder {
    public TextView textViewCaption;

    public UserInfoCaptionViewHolder(@NonNull View itemView) {
        super(itemView);
        textViewCaption = itemView.findViewById(R.id.textViewCaption);
    }
}

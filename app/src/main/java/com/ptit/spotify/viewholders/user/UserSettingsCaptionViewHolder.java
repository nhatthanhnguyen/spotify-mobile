package com.ptit.spotify.viewholders.user;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ptit.spotify.R;

public class UserSettingsCaptionViewHolder extends RecyclerView.ViewHolder {
    public TextView textViewCaption;

    public UserSettingsCaptionViewHolder(@NonNull View itemView) {
        super(itemView);
        textViewCaption = itemView.findViewById(R.id.textViewCaption);
    }
}

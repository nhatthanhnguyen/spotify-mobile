package com.ptit.spotify.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ptit.spotify.R;

public class UserSettingsHeaderViewHolder extends RecyclerView.ViewHolder {
    public ImageView imageViewAccount;
    public TextView textViewAccountName;

    public UserSettingsHeaderViewHolder(@NonNull View itemView) {
        super(itemView);
        imageViewAccount = itemView.findViewById(R.id.imageViewAccount);
        textViewAccountName = itemView.findViewById(R.id.textViewAccountName);
    }
}

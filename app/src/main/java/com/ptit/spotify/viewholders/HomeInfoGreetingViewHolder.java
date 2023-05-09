package com.ptit.spotify.viewholders;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ptit.spotify.R;

public class HomeInfoGreetingViewHolder extends RecyclerView.ViewHolder {
    public TextView textViewGreeting;
    public ImageButton buttonSettings;
    public HomeInfoGreetingViewHolder(@NonNull View itemView) {
        super(itemView);
        textViewGreeting = itemView.findViewById(R.id.textViewGreeting);
        buttonSettings = itemView.findViewById(R.id.buttonSettings);
    }
}

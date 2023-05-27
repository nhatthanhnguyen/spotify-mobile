package com.ptit.spotify.viewholders.home;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ptit.spotify.R;

public class HomeGreetingViewHolder extends RecyclerView.ViewHolder {
    public TextView textViewGreeting;
    public ImageButton buttonUserSettings;
    public ImageButton buttonRecentlyPlayed;
//    public ImageButton buttonNotification;

    public HomeGreetingViewHolder(@NonNull View itemView) {
        super(itemView);
        textViewGreeting = itemView.findViewById(R.id.textViewGreeting);
        buttonUserSettings = itemView.findViewById(R.id.buttonUserSettings);
        buttonRecentlyPlayed = itemView.findViewById(R.id.buttonRecentlyPlayed);
//        buttonNotification = itemView.findViewById(R.id.buttonNotification);
    }
}

package com.ptit.spotify.viewholders.search;

import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ptit.spotify.R;

public class ButtonFilterViewHolder extends RecyclerView.ViewHolder {
    public Button buttonFilter;

    public ButtonFilterViewHolder(@NonNull View itemView) {
        super(itemView);
        buttonFilter = itemView.findViewById(R.id.buttonFilter);
    }
}

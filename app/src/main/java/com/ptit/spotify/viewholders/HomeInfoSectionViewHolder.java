package com.ptit.spotify.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ptit.spotify.R;

public class HomeInfoSectionViewHolder extends RecyclerView.ViewHolder {
    public TextView textViewSectionTitle;
    public RecyclerView recyclerViewSection;

    public HomeInfoSectionViewHolder(@NonNull View itemView) {
        super(itemView);
        textViewSectionTitle = itemView.findViewById(R.id.textViewSectionTitle);
        recyclerViewSection = itemView.findViewById(R.id.recyclerViewSection);
        recyclerViewSection.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
    }
}

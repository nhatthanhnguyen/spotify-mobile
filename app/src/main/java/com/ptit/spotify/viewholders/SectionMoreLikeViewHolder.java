package com.ptit.spotify.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ptit.spotify.R;

public class SectionMoreLikeViewHolder extends RecyclerView.ViewHolder {
    public LinearLayout linearLayoutTitle;
    public ImageView imageView;
    public TextView textViewTitle;
    public RecyclerView recyclerViewSection;
    public SectionMoreLikeViewHolder(@NonNull View itemView) {
        super(itemView);
        linearLayoutTitle = itemView.findViewById(R.id.linearLayoutTitle);
        imageView = itemView.findViewById(R.id.imageView);
        textViewTitle = itemView.findViewById(R.id.textViewTitle);
        recyclerViewSection = itemView.findViewById(R.id.recyclerViewSection);
        recyclerViewSection.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
    }
}

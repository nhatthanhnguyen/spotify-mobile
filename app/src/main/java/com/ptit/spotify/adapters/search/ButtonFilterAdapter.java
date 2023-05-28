package com.ptit.spotify.adapters.search;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ptit.spotify.R;
import com.ptit.spotify.dto.data.ButtonFilterData;
import com.ptit.spotify.utils.OnItemButtonFilterClickedListener;
import com.ptit.spotify.viewholders.search.ButtonFilterViewHolder;

import java.util.List;

public class ButtonFilterAdapter extends RecyclerView.Adapter<ButtonFilterViewHolder> {
    private List<ButtonFilterData> items;
    private OnItemButtonFilterClickedListener onItemButtonFilterClickedListener;

    public ButtonFilterAdapter(
            List<ButtonFilterData> items,
            OnItemButtonFilterClickedListener onItemButtonFilterClickedListener
    ) {
        this.items = items;
        this.onItemButtonFilterClickedListener = onItemButtonFilterClickedListener;
    }

    @NonNull
    @Override
    public ButtonFilterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.layout_button_filter, parent, false);
        return new ButtonFilterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ButtonFilterViewHolder holder, int position) {
        ButtonFilterData data = items.get(position);
        switch (data.getType()) {
            case SONG:
                holder.buttonFilter.setText("Song");
                break;
            case PLAYLIST:
                holder.buttonFilter.setText("Playlist");
                break;
            case ARTIST:
                holder.buttonFilter.setText("Artist");
                break;
            case ALBUM:
                holder.buttonFilter.setText("Album.java");
                break;
            case TOP_RESULT:
                holder.buttonFilter.setText("Top result");
                break;
            case ALL_RESULT:
                holder.buttonFilter.setText("All result");
                break;
        }
        if (data.isSelected()) {
            holder.buttonFilter.setBackgroundTintList(
                    ContextCompat.getColorStateList(holder.itemView.getContext(), R.color.bright_lime_green));
            holder.buttonFilter.setTextColor(
                    holder.itemView.getResources().getColor(
                            R.color.dark_black,
                            holder.itemView.getContext().getTheme()
                    )
            );
        } else {
            holder.buttonFilter.setBackgroundTintList(ColorStateList.valueOf(Color.TRANSPARENT));
            holder.buttonFilter.setTextColor(
                    holder.itemView.getResources().getColor(
                            R.color.white,
                            holder.itemView.getContext().getTheme()
                    )
            );
        }
        holder.buttonFilter.setOnClickListener(v -> {
            onItemButtonFilterClickedListener.updateButtonFilter(position);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}

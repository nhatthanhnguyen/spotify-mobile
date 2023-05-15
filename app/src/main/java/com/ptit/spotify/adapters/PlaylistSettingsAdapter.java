package com.ptit.spotify.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ptit.spotify.R;
import com.ptit.spotify.dto.data.PlaylistSettingsHeaderData;
import com.ptit.spotify.dto.data.PlaylistSettingsOptionToggleData;
import com.ptit.spotify.utils.TypePlaylistSettingsItem;
import com.ptit.spotify.viewholders.PlaylistSettingsHeaderViewHolder;
import com.ptit.spotify.viewholders.PlaylistSettingsOptionViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PlaylistSettingsAdapter extends RecyclerView.Adapter {
    private List<Object> playlistSettingsItems;
    private OnItemClickedListener onItemClickedListener;

    public PlaylistSettingsAdapter(List<Object> playlistSettingsItems, OnItemClickedListener onItemClickedListener) {
        this.playlistSettingsItems = playlistSettingsItems;
        this.onItemClickedListener = onItemClickedListener;
    }

    @Override
    public int getItemViewType(int position) {
        Object item = playlistSettingsItems.get(position);
        if (item instanceof PlaylistSettingsHeaderData) {
            return TypePlaylistSettingsItem.HEADER.ordinal();
        }

        if (item instanceof PlaylistSettingsOptionToggleData) {
            return TypePlaylistSettingsItem.TOGGLE_OPTION.ordinal();
        }

        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TypePlaylistSettingsItem.HEADER.ordinal()) {
            View view = inflater.inflate(R.layout.layout_playlist_settings_header, parent, false);
            return new PlaylistSettingsHeaderViewHolder(view);
        }

        if (viewType == TypePlaylistSettingsItem.TOGGLE_OPTION.ordinal()) {
            View view = inflater.inflate(R.layout.layout_playlist_settings_option, parent, false);
            return new PlaylistSettingsOptionViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PlaylistSettingsHeaderViewHolder) {
            PlaylistSettingsHeaderViewHolder viewHolder = (PlaylistSettingsHeaderViewHolder) holder;
            PlaylistSettingsHeaderData data = (PlaylistSettingsHeaderData) playlistSettingsItems.get(position);
            Picasso.get().load(data.getPlaylistImageUrl()).into(viewHolder.imageViewPlaylist);
            viewHolder.textViewPlaylistName.setText(data.getPlaylistName());
            viewHolder.textViewUserCreated.setText(data.getUserCreatedName());
            viewHolder.itemView.setOnClickListener(v -> {
                onItemClickedListener.onBackClickedListener();
            });
        }

        if (holder instanceof PlaylistSettingsOptionViewHolder) {
            PlaylistSettingsOptionViewHolder viewHolder = (PlaylistSettingsOptionViewHolder) holder;
            PlaylistSettingsOptionToggleData data = (PlaylistSettingsOptionToggleData) playlistSettingsItems.get(position);
            int drawableId = viewHolder.itemView.getContext().getResources().getIdentifier(data.getImageResource(), "drawable",
                    viewHolder.itemView.getContext().getPackageName());
            viewHolder.textViewPlaylistSettingsName.setText(data.getTextOption());
            viewHolder.imageViewPlaylistSettings.setImageResource(drawableId);
            switch (data.getType()) {
                case TOGGLE_OPTION:
                    viewHolder.itemView.setOnClickListener(v -> {
                        onItemClickedListener.onToggleClickedListener();
                    });
            }
        }
    }

    @Override
    public int getItemCount() {
        return playlistSettingsItems.size();
    }

    public interface OnItemClickedListener {

        void onBackClickedListener();

        void onToggleClickedListener();
    }
}

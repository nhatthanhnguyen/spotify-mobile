package com.ptit.spotify.adapters.playlist;

import static com.ptit.spotify.utils.ItemType.PLAYLIST_HEADER;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ptit.spotify.R;
import com.ptit.spotify.dto.data.PlaylistSettingHeaderData;
import com.ptit.spotify.dto.data.SettingOptionData;
import com.ptit.spotify.utils.OnItemPlaylistSettingClickedListener;
import com.ptit.spotify.viewholders.blank.BlankViewHolder;
import com.ptit.spotify.viewholders.playlist.PlaylistSettingHeaderViewHolder;
import com.ptit.spotify.viewholders.playlist.PlaylistSettingOptionViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PlaylistSettingAdapter extends RecyclerView.Adapter {
    private List<Object> playlistSettingsItems;
    private OnItemPlaylistSettingClickedListener onItemPlaylistSettingClickedListener;

    public PlaylistSettingAdapter(
            List<Object> playlistSettingsItems,
            OnItemPlaylistSettingClickedListener onItemPlaylistSettingClickedListener
    ) {
        this.playlistSettingsItems = playlistSettingsItems;
        this.onItemPlaylistSettingClickedListener = onItemPlaylistSettingClickedListener;
    }

    @Override
    public int getItemViewType(int position) {
        Object item = playlistSettingsItems.get(position);
        if (item instanceof PlaylistSettingHeaderData) {
            return PLAYLIST_HEADER.ordinal();
        }

        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        if (viewType == PLAYLIST_HEADER.ordinal()) {
            view = inflater.inflate(R.layout.layout_playlist_settings_header, parent, false);
            return new PlaylistSettingHeaderViewHolder(view);
        }
        view = inflater.inflate(R.layout.layout_blank_item, parent, false);
        return new BlankViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PlaylistSettingHeaderViewHolder) {
            PlaylistSettingHeaderViewHolder viewHolder = (PlaylistSettingHeaderViewHolder) holder;
            PlaylistSettingHeaderData data = (PlaylistSettingHeaderData) playlistSettingsItems.get(position);
            Picasso.get().load(data.getPlaylistImageUrl()).into(viewHolder.imageViewPlaylist);
            viewHolder.textViewPlaylistName.setText(data.getPlaylistName());
            viewHolder.textViewUserCreated.setText(data.getUserCreatedName());
            viewHolder.itemView.setOnClickListener(v -> {
                onItemPlaylistSettingClickedListener.onBackButtonClickedListener();
            });
        }

        if (holder instanceof PlaylistSettingOptionViewHolder) {
            PlaylistSettingOptionViewHolder viewHolder = (PlaylistSettingOptionViewHolder) holder;
            SettingOptionData data = (SettingOptionData) playlistSettingsItems.get(position);
            viewHolder.textViewPlaylistSettingsName.setText(data.getDescription());
            viewHolder.imageViewPlaylistSettings.setImageResource(data.getIconId());
            viewHolder.itemView.setOnClickListener(v -> {
                onItemPlaylistSettingClickedListener.onOptionClickedListener();
            });
        }
    }

    @Override
    public int getItemCount() {
        return playlistSettingsItems.size();
    }
}

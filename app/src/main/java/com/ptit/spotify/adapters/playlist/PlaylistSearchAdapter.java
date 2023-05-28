package com.ptit.spotify.adapters.playlist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ptit.spotify.R;
import com.ptit.spotify.dto.data.PlaylistSongData;
import com.ptit.spotify.utils.OnItemPlaylistSearchClickedListener;
import com.ptit.spotify.viewholders.playlist.PlaylistSongViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PlaylistSearchAdapter extends RecyclerView.Adapter<PlaylistSongViewHolder> {
    private List<PlaylistSongData> items;
    private OnItemPlaylistSearchClickedListener onItemPlaylistSearchClickedListener;

    public PlaylistSearchAdapter(
            List<PlaylistSongData> items,
            OnItemPlaylistSearchClickedListener onItemPlaylistSearchClickedListener
    ) {
        this.items = items;
        this.onItemPlaylistSearchClickedListener = onItemPlaylistSearchClickedListener;
    }

    @NonNull
    @Override
    public PlaylistSongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.layout_playlist_song, parent, false);
        return new PlaylistSongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistSongViewHolder holder, int position) {
        PlaylistSongData data = (PlaylistSongData) items.get(position);
        holder.textViewSongName.setText(data.getSongName());
        holder.textViewArtistName.setText(data.getArtistName());
        if (!data.isLiked()) {
            holder.buttonLikeSong.setVisibility(View.GONE);
        }
        if (!data.isDownloaded()) {
            holder.imageViewSongDownloaded.setVisibility(View.GONE);
        }
        holder.buttonMoreSettingSong.setOnClickListener(v -> {
            onItemPlaylistSearchClickedListener.onSongSettingClickedListener(data);
        });

        holder.buttonLikeSong.setOnClickListener(v -> {
            if (data.isLiked()) {
                data.setLiked(false);
                holder.buttonLikeSong.setVisibility(View.GONE);
            } else {
                data.setLiked(true);
                holder.buttonLikeSong.setVisibility(View.VISIBLE);
            }
        });
        Picasso.get().load(data.getSongImageUrl()).into(holder.imageViewSong);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}

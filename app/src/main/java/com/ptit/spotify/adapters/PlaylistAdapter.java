package com.ptit.spotify.adapters;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.RecyclerView;

import com.ptit.spotify.R;
import com.ptit.spotify.dto.data.PlaylistHeaderData;
import com.ptit.spotify.dto.data.PlaylistSongData;
import com.ptit.spotify.utils.TypePlaylistItem;
import com.ptit.spotify.viewholders.PlaylistHeaderViewHolder;
import com.ptit.spotify.viewholders.PlaylistSongViewHolder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

public class PlaylistAdapter extends RecyclerView.Adapter {
    private List<Object> playlistItems;
    private OnItemClickedListener onItemClickedListener;

    public PlaylistAdapter(List<Object> playlistItems, OnItemClickedListener onItemClickedListener) {
        this.playlistItems = playlistItems;
        this.onItemClickedListener = onItemClickedListener;
    }

    @Override
    public int getItemViewType(int position) {
        Object item = playlistItems.get(position);
        if (item instanceof PlaylistHeaderData) {
            return TypePlaylistItem.HEADER.ordinal();
        }

        if (item instanceof PlaylistSongData) {
            return TypePlaylistItem.SONG.ordinal();
        }

        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TypePlaylistItem.HEADER.ordinal()) {
            View view = inflater.inflate(R.layout.layout_playlist_header, parent, false);
            return new PlaylistHeaderViewHolder(view);
        }

        if (viewType == TypePlaylistItem.SONG.ordinal()) {
            View view = inflater.inflate(R.layout.layout_playlist_song, parent, false);
            return new PlaylistSongViewHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PlaylistHeaderViewHolder) {
            PlaylistHeaderViewHolder viewHolder = (PlaylistHeaderViewHolder) holder;
            PlaylistHeaderData data = (PlaylistHeaderData) playlistItems.get(position);
            viewHolder.buttonBack.setOnClickListener(v -> {
                onItemClickedListener.onBackButtonClickedListener();
            });
            viewHolder.linearLayoutSearch.setOnClickListener(v -> {
                onItemClickedListener.onSearchClickedListener();
            });
            viewHolder.textViewPlaylistDescription.setText(data.getPlaylistDescription());
            viewHolder.textViewUserCreated.setText(data.getUserCreatedName());
            viewHolder.textViewNumberOfLikes.setText(data.getNumberOfLikes() + " likes");
            viewHolder.textViewTotalLength.setText("12h 48m");
            viewHolder.buttonLikePlaylist.setImageResource(R.drawable.ic_like_outlined);
            if (data.isLiked())
                viewHolder.buttonLikePlaylist.setImageResource(R.drawable.ic_like_filled);

            viewHolder.buttonDownloadPlaylist.setOnClickListener(v -> {
            });

            viewHolder.buttonLikePlaylist.setOnClickListener(v -> {
                if (data.isLiked()) {
                    data.setLiked(false);
                    viewHolder.buttonLikePlaylist.setImageResource(R.drawable.ic_like_outlined);
                } else {
                    data.setLiked(true);
                    viewHolder.buttonLikePlaylist.setImageResource(R.drawable.ic_like_filled);
                }
            });

            viewHolder.buttonMoreSettingPlaylist.setOnClickListener(v -> {
                onItemClickedListener.onMoreSettingsClickedListener();
            });
            Picasso.get().load(data.getPlaylistImageUrl()).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    Palette.from(bitmap).generate(palette -> {
                        if (palette != null) {
                            Palette.Swatch dominantSwatch = palette.getDominantSwatch();
                            if (dominantSwatch != null) {
                                GradientDrawable gradientDrawable = new GradientDrawable(
                                        GradientDrawable.Orientation.TOP_BOTTOM,
                                        new int[]{
                                                dominantSwatch.getRgb(),
                                                viewHolder.itemView.getContext().getColor(R.color.dark_black)
                                        }
                                );
                                viewHolder.imageViewPlaylist.setImageBitmap(bitmap);
                                viewHolder.linearLayoutHeader.setBackground(gradientDrawable);
                            }
                        }
                    });
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });
            Picasso.get().load(data.getUserCreatedImageUrl()).into(viewHolder.imageViewUserCreated);
        }

        if (holder instanceof PlaylistSongViewHolder) {
            PlaylistSongViewHolder viewHolder = (PlaylistSongViewHolder) holder;
            PlaylistSongData data = (PlaylistSongData) playlistItems.get(position);
            viewHolder.textViewSongName.setText(data.getSongName());
            viewHolder.textViewArtistName.setText(data.getArtistName());
            if (!data.isLiked()) {
                viewHolder.buttonLikeSong.setVisibility(View.GONE);
            }
            if (!data.isDownloaded()) {
                viewHolder.imageViewSongDownloaded.setVisibility(View.GONE);
            }

            viewHolder.buttonLikeSong.setOnClickListener(v -> {
                if (data.isLiked()) {
                    data.setLiked(false);
                    viewHolder.buttonLikeSong.setVisibility(View.GONE);
                } else {
                    data.setLiked(true);
                    viewHolder.buttonLikeSong.setVisibility(View.VISIBLE);
                }
            });
            Picasso.get().load(data.getSongImageUrl()).into(viewHolder.imageViewSong);
        }
    }

    @Override
    public int getItemCount() {
        return playlistItems.size();
    }

    public interface OnItemClickedListener {
        void onMoreSettingsClickedListener();

        void onBackButtonClickedListener();

        void onSearchClickedListener();
    }
}

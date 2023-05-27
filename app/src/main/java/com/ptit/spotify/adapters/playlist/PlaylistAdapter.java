package com.ptit.spotify.adapters.playlist;

import static com.ptit.spotify.utils.ItemType.BLANK;
import static com.ptit.spotify.utils.ItemType.PLAYLIST_HEADER;
import static com.ptit.spotify.utils.ItemType.PLAYLIST_SONG;

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
import com.ptit.spotify.utils.OnItemPlaylistClickedListener;
import com.ptit.spotify.viewholders.blank.BlankViewHolder;
import com.ptit.spotify.viewholders.playlist.PlaylistHeaderViewHolder;
import com.ptit.spotify.viewholders.playlist.PlaylistSongViewHolder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

public class PlaylistAdapter extends RecyclerView.Adapter {
    private List<Object> playlistItems;
    private OnItemPlaylistClickedListener onItemPlaylistClickedListener;

    public PlaylistAdapter(List<Object> playlistItems, OnItemPlaylistClickedListener onItemPlaylistClickedListener) {
        this.playlistItems = playlistItems;
        this.onItemPlaylistClickedListener = onItemPlaylistClickedListener;
    }

    @Override
    public int getItemViewType(int position) {
        Object item = playlistItems.get(position);
        if (item instanceof PlaylistHeaderData) {
            return PLAYLIST_HEADER.ordinal();
        }

        if (item instanceof PlaylistSongData) {
            return PLAYLIST_SONG.ordinal();
        }

        return BLANK.ordinal();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        if (viewType == PLAYLIST_HEADER.ordinal()) {
            view = inflater.inflate(R.layout.layout_playlist_header, parent, false);
            return new PlaylistHeaderViewHolder(view);
        }

        if (viewType == PLAYLIST_SONG.ordinal()) {
            view = inflater.inflate(R.layout.layout_playlist_song, parent, false);
            return new PlaylistSongViewHolder(view);
        }
        view = inflater.inflate(R.layout.layout_blank_item, parent, false);
        return new BlankViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PlaylistHeaderViewHolder) {
            PlaylistHeaderViewHolder viewHolder = (PlaylistHeaderViewHolder) holder;
            PlaylistHeaderData data = (PlaylistHeaderData) playlistItems.get(position);
            viewHolder.buttonBack.setOnClickListener(v -> {
                onItemPlaylistClickedListener.onBackButtonClickedListener();
            });
            viewHolder.linearLayoutSearch.setOnClickListener(v -> {
                onItemPlaylistClickedListener.onSearchClickedListener();
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
                onItemPlaylistClickedListener.onMoreSettingsClickedListener();
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
}

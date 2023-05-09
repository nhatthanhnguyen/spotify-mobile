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
import com.ptit.spotify.dto.data.AlbumHeaderData;
import com.ptit.spotify.dto.data.AlbumSongData;
import com.ptit.spotify.utils.TypeAlbumItem;
import com.ptit.spotify.viewholders.AlbumInfoHeaderViewHolder;
import com.ptit.spotify.viewholders.AlbumInfoSongViewHolder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

public class AlbumInfoAdapter extends RecyclerView.Adapter {
    private List<Object> albumData;
    private OnItemClickedListener itemClickedListener;

    public AlbumInfoAdapter(List<Object> albumData, OnItemClickedListener itemClickedListener) {
        this.albumData = albumData;
        this.itemClickedListener = itemClickedListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TypeAlbumItem.ALBUM_HEADER.ordinal()) {
            View viewAlbumHeader = inflater.inflate(R.layout.layout_album_header, parent, false);
            return new AlbumInfoHeaderViewHolder(viewAlbumHeader);
        }

        if (viewType == TypeAlbumItem.ALBUM_SONG.ordinal()) {
            View viewAlbumSong = inflater.inflate(R.layout.layout_album_song, parent, false);
            return new AlbumInfoSongViewHolder(viewAlbumSong);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof AlbumInfoHeaderViewHolder) {
            AlbumInfoHeaderViewHolder headerViewHolder = (AlbumInfoHeaderViewHolder) holder;
            AlbumHeaderData headerData = (AlbumHeaderData) albumData.get(position);
            headerViewHolder.buttonBack.setOnClickListener(view -> {
                itemClickedListener.onItemClickedToFragmentListener();
            });
            headerViewHolder.textViewAlbumDateReleased.setText(headerData.getAlbumDateReleased());
            headerViewHolder.textViewAlbumName.setText(headerData.getAlbumName());
            headerViewHolder.buttonMoreSettingAlbum.setOnClickListener(view -> {
                itemClickedListener.onItemClickedToActivityListener();
            });
            headerViewHolder.textViewArtistName.setText(headerData.getArtistName());
            setViewHolderForToggleButton(headerViewHolder, headerData);
            headerViewHolder.buttonLikeAlbum.setOnClickListener(view -> {
                headerData.setLiked(!headerData.isLiked());
                setViewHolderForToggleButton(headerViewHolder, headerData);
            });

            headerViewHolder.buttonPlayAlbum.setOnClickListener(view -> {
                headerData.setPlaying(!headerData.isPlaying());
                setViewHolderForToggleButton(headerViewHolder, headerData);
            });
            Picasso.get().load(headerData.getImageUrl()).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    Palette.from(bitmap).generate(palette -> {
                        if (palette != null) {
                            Palette.Swatch dominantSwatch = palette.getDominantSwatch();
                            if (dominantSwatch != null) {
                                GradientDrawable gradientDrawable = new GradientDrawable(
                                        GradientDrawable.Orientation.TOP_BOTTOM,
                                        new int[]{dominantSwatch.getRgb(),
                                                headerViewHolder.itemView.getContext().getColor(R.color.dark_black)}
                                );
                                headerViewHolder.imageViewAlbum.setImageBitmap(bitmap);
                                headerViewHolder.linearLayoutHeader.setBackground(gradientDrawable);
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
            Picasso.get().load(headerData.getImageArtistUrl()).into(headerViewHolder.imageViewArtist);
        }

        if (holder instanceof AlbumInfoSongViewHolder) {
            AlbumInfoSongViewHolder songViewHolder = (AlbumInfoSongViewHolder) holder;
            AlbumSongData songData = (AlbumSongData) albumData.get(position);
            songViewHolder.textViewSongName.setText(songData.getName());
            songViewHolder.textViewArtistName.setText(songData.getArtistName());
        }
    }

    @Override
    public int getItemViewType(int position) {
        Object data = albumData.get(position);
        if (data instanceof AlbumHeaderData) {
            return TypeAlbumItem.ALBUM_HEADER.ordinal();
        }
        if (data instanceof AlbumSongData) {
            return TypeAlbumItem.ALBUM_SONG.ordinal();
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        return albumData.size();
    }

    public interface OnItemClickedListener {
        void onItemClickedToFragmentListener();

        void onItemClickedToActivityListener();
    }

    private void setViewHolderForToggleButton(AlbumInfoHeaderViewHolder headerViewHolder, AlbumHeaderData data) {
        String defPackageStr = headerViewHolder.itemView.getContext().getPackageName();
        int drawableLikedId = getDrawableId(headerViewHolder, "ic_like_filled", defPackageStr);
        int drawableNotLikeId = getDrawableId(headerViewHolder, "ic_like_outlined", defPackageStr);

        int drawablePlayingId = getDrawableId(headerViewHolder, "ic_play_green_large", defPackageStr);
        int drawablePauseId = getDrawableId(headerViewHolder, "ic_pause_green_large", defPackageStr);

        int drawableLikeId = data.isLiked() ? drawableLikedId : drawableNotLikeId;
        int drawablePlayId = data.isPlaying() ? drawablePauseId : drawablePlayingId;

        headerViewHolder.buttonLikeAlbum.setImageResource(drawableLikeId);
        headerViewHolder.buttonPlayAlbum.setImageResource(drawablePlayId);
    }

    private int getDrawableId(RecyclerView.ViewHolder viewHolder, String drawableName, String defPackageStr) {
        return viewHolder.itemView.getContext().getResources().getIdentifier(drawableName, "drawable", defPackageStr);
    }
}

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
import com.ptit.spotify.models.data.AlbumHeaderData;
import com.ptit.spotify.models.data.AlbumSongData;
import com.ptit.spotify.viewholders.AlbumHeaderViewHolder;
import com.ptit.spotify.viewholders.AlbumSongViewHolder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter {
    private final int ALBUM_HEADER_DATA = 0;
    private final int ALBUM_SONG_DATA = 1;
    private List<Object> albumData;
    private OnItemClickedListener itemClickedListener;

    public AlbumAdapter(List<Object> albumData, OnItemClickedListener itemClickedListener) {
        this.albumData = albumData;
        this.itemClickedListener = itemClickedListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case ALBUM_HEADER_DATA:
                View viewAlbumHeader = inflater.inflate(R.layout.layout_album_header, parent, false);
                return new AlbumHeaderViewHolder(viewAlbumHeader);
            case ALBUM_SONG_DATA:
                View viewAlbumSong = inflater.inflate(R.layout.layout_album_song, parent, false);
                return new AlbumSongViewHolder(viewAlbumSong);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof AlbumHeaderViewHolder) {
            AlbumHeaderViewHolder albumHeaderViewHolder = (AlbumHeaderViewHolder) holder;
            AlbumHeaderData albumHeaderData = (AlbumHeaderData) albumData.get(position);
            albumHeaderViewHolder.buttonBack.setOnClickListener(view -> {
                itemClickedListener.onItemClickedToFragmentListener();
            });
            albumHeaderViewHolder.textViewAlbumDateReleased.setText(albumHeaderData.getAlbumDateReleased());
            albumHeaderViewHolder.textViewAlbumName.setText(albumHeaderData.getAlbumName());
            albumHeaderViewHolder.buttonMoreSettingAlbum.setOnClickListener(view -> {
                itemClickedListener.onItemClickedToActivityListener();
            });
            albumHeaderViewHolder.textViewArtistName.setText(albumHeaderData.getArtistName());
            Picasso.get().load(albumHeaderData.getImageUrl()).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    Palette.from(bitmap).generate(palette -> {
                        if (palette != null) {
                            Palette.Swatch dominantSwatch = palette.getDominantSwatch();
                            if (dominantSwatch != null) {
                                GradientDrawable gradientDrawable = new GradientDrawable(
                                        GradientDrawable.Orientation.TOP_BOTTOM,
                                        new int[]{dominantSwatch.getRgb(),
                                                albumHeaderViewHolder.itemView.getContext().getColor(R.color.dark_black)}
                                );
                                albumHeaderViewHolder.imageViewAlbum.setImageBitmap(bitmap);
                                albumHeaderViewHolder.linearLayoutHeader.setBackground(gradientDrawable);
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
            Picasso.get().load(albumHeaderData.getImageArtistUrl()).into(albumHeaderViewHolder.imageViewArtist);
        }

        if (holder instanceof AlbumSongViewHolder) {
            AlbumSongViewHolder albumSongViewHolder = (AlbumSongViewHolder) holder;
            AlbumSongData albumSongData = (AlbumSongData) albumData.get(position);
            albumSongViewHolder.textViewSongName.setText(albumSongData.getName());
            albumSongViewHolder.textViewArtistName.setText(albumSongData.getArtistName());
        }
    }

    @Override
    public int getItemViewType(int position) {
        Object data = albumData.get(position);
        if (data instanceof AlbumHeaderData) {
            return ALBUM_HEADER_DATA;
        }
        if (data instanceof AlbumSongData) {
            return ALBUM_SONG_DATA;
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
}

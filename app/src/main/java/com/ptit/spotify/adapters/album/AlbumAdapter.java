package com.ptit.spotify.adapters.album;

import static com.ptit.spotify.utils.ItemType.ALBUM_HEADER;
import static com.ptit.spotify.utils.ItemType.ALBUM_SONG;
import static com.ptit.spotify.utils.ItemType.BLANK;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.RecyclerView;

import com.ptit.spotify.R;
import com.ptit.spotify.dto.data.AlbumHeaderData;
import com.ptit.spotify.dto.data.AlbumSongData;
import com.ptit.spotify.utils.OnItemAlbumClickedListener;
import com.ptit.spotify.viewholders.album.AlbumInfoHeaderViewHolder;
import com.ptit.spotify.viewholders.album.AlbumInfoSongViewHolder;
import com.ptit.spotify.viewholders.blank.BlankViewHolder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter {
    private List<Object> albumData;
    private OnItemAlbumClickedListener onItemAlbumClickedListener;

    public AlbumAdapter(List<Object> albumData, OnItemAlbumClickedListener onItemAlbumClickedListener) {
        this.albumData = albumData;
        this.onItemAlbumClickedListener = onItemAlbumClickedListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        if (viewType == ALBUM_HEADER.ordinal()) {
            view = inflater.inflate(R.layout.layout_album_header, parent, false);
            return new AlbumInfoHeaderViewHolder(view);
        }

        if (viewType == ALBUM_SONG.ordinal()) {
            view = inflater.inflate(R.layout.layout_album_song, parent, false);
            return new AlbumInfoSongViewHolder(view);
        }

        view = inflater.inflate(R.layout.layout_blank_item, parent, false);
        return new BlankViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof AlbumInfoHeaderViewHolder) {
            AlbumInfoHeaderViewHolder viewHolder = (AlbumInfoHeaderViewHolder) holder;
            AlbumHeaderData data = (AlbumHeaderData) albumData.get(position);
            viewHolder.buttonBack.setOnClickListener(view -> {
                onItemAlbumClickedListener.onItemClickedToFragmentListener();
            });
            viewHolder.textViewAlbumDateReleased.setText(data.getAlbumDateReleased());
            viewHolder.textViewAlbumName.setText(data.getAlbumName());
            viewHolder.buttonMoreSettingAlbum.setOnClickListener(view -> {
                Toast.makeText(holder.itemView.getContext(), "More setting album", Toast.LENGTH_SHORT).show();
            });
            viewHolder.textViewArtistName.setText(data.getArtistName());
            setViewHolderForToggleButton(viewHolder, data);
            viewHolder.buttonLikeAlbum.setOnClickListener(view -> {
                data.setLiked(!data.isLiked());
                setViewHolderForToggleButton(viewHolder, data);
            });

            viewHolder.buttonPlayAlbum.setOnClickListener(view -> {
                data.setPlaying(!data.isPlaying());
                setViewHolderForToggleButton(viewHolder, data);
            });
            Picasso.get().load(data.getImageUrl()).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    Palette.from(bitmap).generate(palette -> {
                        if (palette != null) {
                            Palette.Swatch dominantSwatch = palette.getDominantSwatch();
                            if (dominantSwatch != null) {
                                GradientDrawable gradientDrawable = new GradientDrawable(
                                        GradientDrawable.Orientation.TOP_BOTTOM,
                                        new int[]{dominantSwatch.getRgb(),
                                                viewHolder.itemView.getContext().getColor(R.color.dark_black)}
                                );
                                viewHolder.imageViewAlbum.setImageBitmap(bitmap);
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
            Picasso.get().load(data.getImageArtistUrl()).into(viewHolder.imageViewArtist);
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
            return ALBUM_HEADER.ordinal();
        }
        if (data instanceof AlbumSongData) {
            return ALBUM_SONG.ordinal();
        }
        return BLANK.ordinal();
    }

    @Override
    public int getItemCount() {
        return albumData.size();
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

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
import com.ptit.spotify.models.data.ArtistCaptionData;
import com.ptit.spotify.models.data.ArtistDescriptionData;
import com.ptit.spotify.models.data.ArtistHeaderData;
import com.ptit.spotify.models.data.ArtistSongData;
import com.ptit.spotify.viewholders.ArtistCaptionViewHolder;
import com.ptit.spotify.viewholders.ArtistDescriptionViewHolder;
import com.ptit.spotify.viewholders.ArtistHeaderViewHolder;
import com.ptit.spotify.viewholders.ArtistSongViewHolder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

public class ArtistAdapter extends RecyclerView.Adapter {
    private final int ARTIST_HEADER = 0;
    private final int ARTIST_SONG = 1;
    private final int ARTIST_CAPTION = 2;
    private final int ARTIST_DESCRIPTION = 3;
    private List<Object> artistItems;
    private OnItemClickedListener onItemClickedListener;

    public ArtistAdapter(List<Object> artistItems, OnItemClickedListener onItemClickedListener) {
        this.artistItems = artistItems;
        this.onItemClickedListener = onItemClickedListener;
    }


    @Override
    public int getItemViewType(int position) {
        Object artistItem = artistItems.get(position);
        if (artistItem instanceof ArtistHeaderData) {
            return ARTIST_HEADER;
        }
        if (artistItem instanceof ArtistSongData) {
            return ARTIST_SONG;
        }
        if (artistItem instanceof ArtistCaptionData) {
            return ARTIST_CAPTION;
        }
        if (artistItem instanceof ArtistDescriptionData) {
            return ARTIST_DESCRIPTION;
        }
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case ARTIST_HEADER:
                View viewArtistHeader = inflater.inflate(R.layout.layout_artist_header, parent, false);
                return new ArtistHeaderViewHolder(viewArtistHeader);
            case ARTIST_CAPTION:
                View viewArtistCaption = inflater.inflate(R.layout.layout_artist_caption, parent, false);
                return new ArtistCaptionViewHolder(viewArtistCaption);
            case ARTIST_SONG:
                View viewArtistSong = inflater.inflate(R.layout.layout_artist_popular_songs, parent, false);
                return new ArtistSongViewHolder(viewArtistSong);
            case ARTIST_DESCRIPTION:
                View viewArtistDescription = inflater.inflate(R.layout.layout_artist_description, parent, false);
                return new ArtistDescriptionViewHolder(viewArtistDescription);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ArtistHeaderViewHolder) {
            ArtistHeaderData headerData = (ArtistHeaderData) artistItems.get(position);
            ArtistHeaderViewHolder headerViewHolder = (ArtistHeaderViewHolder) holder;
            headerViewHolder.textViewArtistName.setText(headerData.getArtistName());
            headerViewHolder.buttonBack.setOnClickListener(view -> {
                onItemClickedListener.onBackButtonListener();
            });
            headerViewHolder.textViewNumberOfLikes.setText(headerData.getNumberOfLikes() + " users liked");
            Picasso.get().load(headerData.getArtistImageUrl()).into(new Target() {
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
                                                headerViewHolder.itemView.getContext().getColor(R.color.dark_black)
                                        }
                                );
                                headerViewHolder.imageViewArtist.setImageBitmap(bitmap);
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
            if (headerData.isFollow()) {
                headerViewHolder.buttonFollow.setText("Followed");
            } else {
                headerViewHolder.buttonFollow.setText("Follow");
            }
            headerViewHolder.buttonFollow.setOnClickListener(view -> {
                headerData.setFollow(!headerData.isFollow());
                if (headerData.isFollow()) {
                    headerViewHolder.buttonFollow.setText("Followed");
                } else {
                    headerViewHolder.buttonFollow.setText("Follow");
                }
            });
        }

        if (holder instanceof ArtistCaptionViewHolder) {
            ArtistCaptionData captionData = (ArtistCaptionData) artistItems.get(position);
            ArtistCaptionViewHolder captionViewHolder = (ArtistCaptionViewHolder) holder;
            captionViewHolder.textViewCaption.setText(captionData.getCaption());
        }

        if (holder instanceof ArtistSongViewHolder) {
            ArtistSongData artistSongData = (ArtistSongData) artistItems.get(position);
            ArtistSongViewHolder songViewHolder = (ArtistSongViewHolder) holder;
            Picasso.get().load(artistSongData.getSongImageUrl()).into(songViewHolder.imageViewSong);
            songViewHolder.textViewSongName.setText(artistSongData.getSongName());
            songViewHolder.textViewNumberOfLikes.setText(artistSongData.getNumberOfLikes());
            songViewHolder.textViewOrderNumber.setText(artistSongData.getOrderNumber());
        }

        if (holder instanceof ArtistDescriptionViewHolder) {
            ArtistDescriptionData descriptionData = (ArtistDescriptionData) artistItems.get(position);
            ArtistDescriptionViewHolder descriptionViewHolder = (ArtistDescriptionViewHolder) holder;
            Picasso.get().load(descriptionData.getArtistImageUrl()).into(descriptionViewHolder.imageViewArtist);
            descriptionViewHolder.textViewDescription.setText(descriptionData.getDescription());
            descriptionViewHolder.textViewNumberOfLikes.setText(descriptionData.getNumberOfLikes() + " users liked");
        }
    }

    @Override
    public int getItemCount() {
        return artistItems.size();
    }

    public interface OnItemClickedListener {
        void onBackButtonListener();
    }
}

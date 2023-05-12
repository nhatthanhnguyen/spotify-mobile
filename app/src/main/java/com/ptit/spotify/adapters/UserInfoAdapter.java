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
import com.ptit.spotify.dto.data.UserInfoHeaderData;
import com.ptit.spotify.dto.data.UserInfoPlaylistData;
import com.ptit.spotify.utils.TypeUserInfoItem;
import com.ptit.spotify.viewholders.UserInfoCaptionViewHolder;
import com.ptit.spotify.viewholders.UserInfoHeaderViewHolder;
import com.ptit.spotify.viewholders.UserInfoPlaylistViewHolder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

public class UserInfoAdapter extends RecyclerView.Adapter {
    private OnItemClickedListener onItemClickedListener;
    private List<Object> userInfoItems;

    public UserInfoAdapter(List<Object> userInfoItems, OnItemClickedListener onItemClickedListener) {
        this.userInfoItems = userInfoItems;
        this.onItemClickedListener = onItemClickedListener;
    }

    @Override
    public int getItemViewType(int position) {
        Object item = userInfoItems.get(position);
        if (item instanceof UserInfoHeaderData) {
            return TypeUserInfoItem.HEADER.ordinal();
        }

        if (item instanceof String) {
            return TypeUserInfoItem.CAPTION.ordinal();
        }

        if (item instanceof UserInfoPlaylistData) {
            return TypeUserInfoItem.PLAYLIST.ordinal();
        }

        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TypeUserInfoItem.HEADER.ordinal()) {
            View view = inflater.inflate(R.layout.layout_user_info_header, parent, false);
            return new UserInfoHeaderViewHolder(view);
        }

        if (viewType == TypeUserInfoItem.CAPTION.ordinal()) {
            View view = inflater.inflate(R.layout.layout_user_info_caption, parent, false);
            return new UserInfoCaptionViewHolder(view);
        }

        if (viewType == TypeUserInfoItem.PLAYLIST.ordinal()) {
            View view = inflater.inflate(R.layout.layout_user_info_playlist, parent, false);
            return new UserInfoPlaylistViewHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof UserInfoHeaderViewHolder) {
            UserInfoHeaderViewHolder viewHolder = (UserInfoHeaderViewHolder) holder;
            UserInfoHeaderData data = (UserInfoHeaderData) userInfoItems.get(position);
            if (data.getImageUrl() != null) {
                Picasso.get().load(data.getImageUrl()).into(new Target() {
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
                                    viewHolder.imageViewUser.setImageBitmap(bitmap);
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
            }
            viewHolder.textViewUserName.setText(data.getName());
            viewHolder.textViewNumberFollowing.setText(String.valueOf(data.getNumberFollowing()));
            viewHolder.buttonBack.setOnClickListener(v -> {
                onItemClickedListener.onButtonBackClickedListener();
            });

            viewHolder.linearLayoutFollowing.setOnClickListener(v -> {
                onItemClickedListener.onFollowingClickedListener();
            });

            viewHolder.buttonEditProfile.setOnClickListener(v -> {
                onItemClickedListener.onEditProfileClickedListener();
            });
        }

        if (holder instanceof UserInfoCaptionViewHolder) {
            UserInfoCaptionViewHolder viewHolder = (UserInfoCaptionViewHolder) holder;
            String captionStr = (String) userInfoItems.get(position);
            viewHolder.textViewCaption.setText(captionStr);
        }

        if (holder instanceof UserInfoPlaylistViewHolder) {
            UserInfoPlaylistViewHolder viewHolder = (UserInfoPlaylistViewHolder) holder;
            UserInfoPlaylistData data = (UserInfoPlaylistData) userInfoItems.get(position);
            Picasso.get().load(data.getPlaylistImageUrl()).into(viewHolder.imageViewPlaylist);
            viewHolder.textViewPlaylistName.setText(data.getPlaylistName());
            viewHolder.itemView.setOnClickListener(v -> {
                onItemClickedListener.onPlaylistClickedListener();
            });
        }
    }

    @Override
    public int getItemCount() {
        return userInfoItems.size();
    }

    public interface OnItemClickedListener {
        void onButtonBackClickedListener();

        void onEditProfileClickedListener();

        void onFollowingClickedListener();

        void onPlaylistClickedListener();
    }
}

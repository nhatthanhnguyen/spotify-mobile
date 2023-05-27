package com.ptit.spotify.adapters.user;

import static com.ptit.spotify.utils.ItemType.BLANK;
import static com.ptit.spotify.utils.ItemType.USER_CAPTION;
import static com.ptit.spotify.utils.ItemType.USER_HEADER;
import static com.ptit.spotify.utils.ItemType.USER_PLAYLIST;

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
import com.ptit.spotify.utils.OnItemUserClickedListener;
import com.ptit.spotify.viewholders.blank.BlankViewHolder;
import com.ptit.spotify.viewholders.user.UserInfoCaptionViewHolder;
import com.ptit.spotify.viewholders.user.UserInfoHeaderViewHolder;
import com.ptit.spotify.viewholders.user.UserInfoPlaylistViewHolder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter {
    private OnItemUserClickedListener onItemUserClickedListener;
    private List<Object> userInfoItems;

    public UserAdapter(
            List<Object> userInfoItems,
            OnItemUserClickedListener onItemUserClickedListener
    ) {
        this.userInfoItems = userInfoItems;
        this.onItemUserClickedListener = onItemUserClickedListener;
    }

    @Override
    public int getItemViewType(int position) {
        Object item = userInfoItems.get(position);
        if (item instanceof UserInfoHeaderData) {
            return USER_HEADER.ordinal();
        }

        if (item instanceof String) {
            return USER_CAPTION.ordinal();
        }

        if (item instanceof UserInfoPlaylistData) {
            return USER_PLAYLIST.ordinal();
        }

        return BLANK.ordinal();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        if (viewType == USER_HEADER.ordinal()) {
            view = inflater.inflate(R.layout.layout_user_header, parent, false);
            return new UserInfoHeaderViewHolder(view);
        }

        if (viewType == USER_CAPTION.ordinal()) {
            view = inflater.inflate(R.layout.layout_user_caption, parent, false);
            return new UserInfoCaptionViewHolder(view);
        }

        if (viewType == USER_PLAYLIST.ordinal()) {
            view = inflater.inflate(R.layout.layout_user_playlist, parent, false);
            return new UserInfoPlaylistViewHolder(view);
        }

        view = inflater.inflate(R.layout.layout_blank_item, parent, false);
        return new BlankViewHolder(view);
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
                onItemUserClickedListener.onButtonBackClickedListener();
            });

            viewHolder.linearLayoutFollowing.setOnClickListener(v -> {
                onItemUserClickedListener.onFollowingClickedListener();
            });

            viewHolder.buttonEditProfile.setOnClickListener(v -> {
                onItemUserClickedListener.onEditProfileClickedListener();
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
                onItemUserClickedListener.onPlaylistClickedListener();
            });
        }
    }

    @Override
    public int getItemCount() {
        return userInfoItems.size();
    }
}

package com.ptit.spotify.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ptit.spotify.R;
import com.ptit.spotify.dto.data.UserSettingsHeaderData;
import com.ptit.spotify.dto.data.UserSettingsOptionData;
import com.ptit.spotify.utils.TypeUserSettingsItem;
import com.ptit.spotify.viewholders.UserSettingsCaptionViewHolder;
import com.ptit.spotify.viewholders.UserSettingsHeaderViewHolder;
import com.ptit.spotify.viewholders.UserSettingsOptionViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserSettingsAdapter extends RecyclerView.Adapter {
    private List<Object> userSettingsItems;
    private OnItemClickedListener onItemClickedListener;

    public UserSettingsAdapter(List<Object> userSettingsItems, OnItemClickedListener onItemClickedListener) {
        this.userSettingsItems = userSettingsItems;
        this.onItemClickedListener = onItemClickedListener;
    }

    @Override
    public int getItemViewType(int position) {
        Object item = userSettingsItems.get(position);
        if (item instanceof UserSettingsHeaderData) {
            return TypeUserSettingsItem.HEADER.ordinal();
        }

        if (item instanceof String) {
            return TypeUserSettingsItem.CAPTION.ordinal();
        }

        if (item instanceof UserSettingsOptionData) {
            UserSettingsOptionData optionData = (UserSettingsOptionData) item;
            return optionData.getType().ordinal();
        }
        return -1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TypeUserSettingsItem.HEADER.ordinal()) {
            View view = inflater.inflate(R.layout.layout_user_settings_header, parent, false);
            return new UserSettingsHeaderViewHolder(view);
        }

        if (viewType == TypeUserSettingsItem.CAPTION.ordinal()) {
            View view = inflater.inflate(R.layout.layout_user_settings_caption, parent, false);
            return new UserSettingsCaptionViewHolder(view);
        }

        if (viewType == TypeUserSettingsItem.EMAIL.ordinal() ||
                viewType == TypeUserSettingsItem.SIGN_OUT.ordinal()) {
            View view = inflater.inflate(R.layout.layout_user_settings_options, parent, false);
            return new UserSettingsOptionViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof UserSettingsHeaderViewHolder) {
            UserSettingsHeaderViewHolder headerViewHolder = (UserSettingsHeaderViewHolder) holder;
            UserSettingsHeaderData data = (UserSettingsHeaderData) userSettingsItems.get(position);
            if (data.getAccountImageUrl() != null) {
                Picasso.get().load(data.getAccountImageUrl()).into(headerViewHolder.imageViewAccount);
            }
            headerViewHolder.textViewAccountName.setText(data.getAccountName());
            headerViewHolder.itemView.setOnClickListener(v -> {
                onItemClickedListener.onUserProfileClickedListener();
            });
        }

        if (holder instanceof UserSettingsCaptionViewHolder) {
            UserSettingsCaptionViewHolder captionViewHolder = (UserSettingsCaptionViewHolder) holder;
            String captionStr = (String) userSettingsItems.get(position);
            captionViewHolder.textViewCaption.setText(captionStr);
        }

        if (holder instanceof UserSettingsOptionViewHolder) {
            UserSettingsOptionViewHolder optionViewHolder = (UserSettingsOptionViewHolder) holder;
            UserSettingsOptionData data = (UserSettingsOptionData) userSettingsItems.get(position);
            optionViewHolder.textViewTitle.setText(data.getTitle());
            optionViewHolder.textViewDescription.setText(data.getDescription());
            if (data.getType().equals(TypeUserSettingsItem.SIGN_OUT)) {
                optionViewHolder.itemView.setOnClickListener(v -> {
                    onItemClickedListener.onSignOutClickedListener();
                });
            }

            if (data.getType().equals(TypeUserSettingsItem.EMAIL)) {
                optionViewHolder.itemView.setOnClickListener(v -> {
                    onItemClickedListener.onEmailClickedListener();
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return userSettingsItems.size();
    }

    public interface OnItemClickedListener {
        void onUserProfileClickedListener();

        void onEmailClickedListener();

        void onSignOutClickedListener();
    }
}

package com.ptit.spotify.adapters.user;

import static com.ptit.spotify.utils.ItemType.BLANK;
import static com.ptit.spotify.utils.ItemType.USER_SETTING_CAPTION;
import static com.ptit.spotify.utils.ItemType.USER_SETTING_EMAIL;
import static com.ptit.spotify.utils.ItemType.USER_SETTING_HEADER;
import static com.ptit.spotify.utils.ItemType.USER_SETTING_LOG_OUT;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ptit.spotify.R;
import com.ptit.spotify.dto.data.UserSettingHeaderData;
import com.ptit.spotify.dto.data.UserSettingOptionData;
import com.ptit.spotify.utils.OnItemUserSettingClickedListener;
import com.ptit.spotify.viewholders.blank.BlankViewHolder;
import com.ptit.spotify.viewholders.user.UserSettingsCaptionViewHolder;
import com.ptit.spotify.viewholders.user.UserSettingsHeaderViewHolder;
import com.ptit.spotify.viewholders.user.UserSettingsOptionViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserSettingAdapter extends RecyclerView.Adapter {
    private List<Object> userSettingsItems;
    private OnItemUserSettingClickedListener onItemUserSettingClickedListener;

    public UserSettingAdapter(
            List<Object> userSettingsItems,
            OnItemUserSettingClickedListener onItemUserSettingClickedListener
    ) {
        this.userSettingsItems = userSettingsItems;
        this.onItemUserSettingClickedListener = onItemUserSettingClickedListener;
    }

    @Override
    public int getItemViewType(int position) {
        Object item = userSettingsItems.get(position);
        if (item instanceof UserSettingHeaderData) {
            return USER_SETTING_HEADER.ordinal();
        }

        if (item instanceof String) {
            return USER_SETTING_CAPTION.ordinal();
        }

        if (item instanceof UserSettingOptionData) {
            UserSettingOptionData data = (UserSettingOptionData) item;
            return data.getType().ordinal();
        }

        return BLANK.ordinal();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        if (viewType == USER_SETTING_HEADER.ordinal()) {
            view = inflater.inflate(R.layout.layout_user_setting_header, parent, false);
            return new UserSettingsHeaderViewHolder(view);
        }

        if (viewType == USER_SETTING_CAPTION.ordinal()) {
            view = inflater.inflate(R.layout.layout_user_setting_caption, parent, false);
            return new UserSettingsCaptionViewHolder(view);
        }

        if (viewType == USER_SETTING_EMAIL.ordinal() ||
                viewType == USER_SETTING_LOG_OUT.ordinal()) {
            view = inflater.inflate(R.layout.layout_user_setting_option, parent, false);
            return new UserSettingsOptionViewHolder(view);
        }

        view = inflater.inflate(R.layout.layout_blank_item, parent, false);
        return new BlankViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof UserSettingsHeaderViewHolder) {
            UserSettingsHeaderViewHolder headerViewHolder = (UserSettingsHeaderViewHolder) holder;
            UserSettingHeaderData data = (UserSettingHeaderData) userSettingsItems.get(position);
            if (data.getAccountImageUrl() != null) {
                Picasso.get().load(data.getAccountImageUrl()).into(headerViewHolder.imageViewAccount);
            }
            headerViewHolder.textViewAccountName.setText(data.getAccountName());
            headerViewHolder.itemView.setOnClickListener(v -> {
                onItemUserSettingClickedListener.onUserClickedListener();
            });
        }

        if (holder instanceof UserSettingsCaptionViewHolder) {
            UserSettingsCaptionViewHolder captionViewHolder = (UserSettingsCaptionViewHolder) holder;
            String captionStr = (String) userSettingsItems.get(position);
            captionViewHolder.textViewCaption.setText(captionStr);
        }

        if (holder instanceof UserSettingsOptionViewHolder) {
            UserSettingsOptionViewHolder optionViewHolder = (UserSettingsOptionViewHolder) holder;
            UserSettingOptionData data = (UserSettingOptionData) userSettingsItems.get(position);
            optionViewHolder.textViewTitle.setText(data.getTitle());
            optionViewHolder.textViewDescription.setText(data.getDescription());
            if (data.getType().equals(USER_SETTING_LOG_OUT)) {
                optionViewHolder.itemView.setOnClickListener(v -> {
                    onItemUserSettingClickedListener.onSignOutClickedListener();
                });
            }

            if (data.getType().equals(USER_SETTING_EMAIL)) {
                optionViewHolder.itemView.setOnClickListener(v -> {
                    onItemUserSettingClickedListener.onEmailClickedListener();
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return userSettingsItems.size();
    }
}

package com.ptit.spotify.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ptit.spotify.R;
import com.ptit.spotify.dto.data.AlbumSettingsHeaderData;
import com.ptit.spotify.dto.data.AlbumSettingsOptionNavigationData;
import com.ptit.spotify.dto.data.AlbumSettingsOptionToggleData;
import com.ptit.spotify.utils.TypeDestination;
import com.ptit.spotify.viewholders.AlbumSettingsHeaderViewHolder;
import com.ptit.spotify.viewholders.AlbumSettingsOptionViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AlbumSettingsAdapter extends RecyclerView.Adapter {
    private final int ALBUM_SETTINGS_HEADER = 0;
    private final int ALBUM_SETTINGS_OPTION_NAVIGATE = 1;
    private final int ALBUM_SETTINGS_OPTION_TOGGLE = 2;

    private List<Object> albumSettingsItems;
    private OnItemClickedListener onItemClickedListener;

    public AlbumSettingsAdapter(List<Object> albumSettingsItems, OnItemClickedListener onItemClickedListener) {
        this.albumSettingsItems = albumSettingsItems;
        this.onItemClickedListener = onItemClickedListener;
    }

    @Override
    public int getItemViewType(int position) {
        Object albumSettingsItem = albumSettingsItems.get(position);
        if (albumSettingsItem instanceof AlbumSettingsHeaderData) {
            return ALBUM_SETTINGS_HEADER;
        }
        if (albumSettingsItem instanceof AlbumSettingsOptionNavigationData) {
            return ALBUM_SETTINGS_OPTION_NAVIGATE;
        }
        if (albumSettingsItem instanceof AlbumSettingsOptionToggleData) {
            return ALBUM_SETTINGS_OPTION_TOGGLE;
        }
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case ALBUM_SETTINGS_HEADER:
                View viewAlbumSettingsHeader = inflater.inflate(R.layout.layout_album_settings_header, parent, false);
                return new AlbumSettingsHeaderViewHolder(viewAlbumSettingsHeader);
            case ALBUM_SETTINGS_OPTION_NAVIGATE:
            case ALBUM_SETTINGS_OPTION_TOGGLE:
                View viewAlbumSettingsOption = inflater.inflate(R.layout.layout_album_settings_option, parent, false);
                return new AlbumSettingsOptionViewHolder(viewAlbumSettingsOption);
            default:
                return null;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof AlbumSettingsHeaderViewHolder) {
            AlbumSettingsHeaderViewHolder albumSettingsHeaderViewHolder = (AlbumSettingsHeaderViewHolder) holder;
            AlbumSettingsHeaderData albumSettingsHeaderData = (AlbumSettingsHeaderData) albumSettingsItems.get(position);
            albumSettingsHeaderViewHolder.textViewArtistName.setText(albumSettingsHeaderData.getArtistName());
            albumSettingsHeaderViewHolder.textViewAlbumName.setText(albumSettingsHeaderData.getAlbumName());
            Picasso.get().load(albumSettingsHeaderData.getAlbumImageUrl()).into(albumSettingsHeaderViewHolder.imageViewAlbumImage);
        }

        if (holder instanceof AlbumSettingsOptionViewHolder) {
            AlbumSettingsOptionViewHolder optionViewHolder = (AlbumSettingsOptionViewHolder) holder;
            Object settingsOptionData = albumSettingsItems.get(position);
            if (settingsOptionData instanceof AlbumSettingsOptionNavigationData) {
                AlbumSettingsOptionNavigationData navigationData = (AlbumSettingsOptionNavigationData) settingsOptionData;
                optionViewHolder.textViewAlbumSettingsOption.setText(navigationData.getTextOption());
                int drawableId = optionViewHolder.itemView.getContext().getResources().getIdentifier(navigationData.getImageResourceOption(), "drawable", optionViewHolder.itemView.getContext().getPackageName());
                optionViewHolder.imageViewAlbumSettingsOption.setImageResource(drawableId);
                if (navigationData.getTypeDestination().equals(TypeDestination.ARTIST)) {
                    optionViewHolder.itemView.setOnClickListener(view -> {
                        onItemClickedListener.onItemClickedNavigationOptionListener(TypeDestination.ARTIST);
                    });
                }
            }

            if (settingsOptionData instanceof AlbumSettingsOptionToggleData) {
                AlbumSettingsOptionToggleData toggleData = (AlbumSettingsOptionToggleData) settingsOptionData;
                setViewHolder(optionViewHolder, toggleData);
                optionViewHolder.itemView.setOnClickListener(view -> {
                    toggleData.setOn(!toggleData.isOn());
                    setViewHolder(optionViewHolder, toggleData);
                });
            }
        }
    }

    private void setViewHolder(AlbumSettingsOptionViewHolder optionViewHolder, AlbumSettingsOptionToggleData toggleData) {
        String drawableNameOn = toggleData.getImageResourceOption() + "_filled";
        String drawableNameOff = toggleData.getImageResourceOption() + "_outlined";
        int greenText = optionViewHolder.itemView.getContext().getColor(R.color.bright_lime_green);
        int whiteText = optionViewHolder.itemView.getContext().getColor(R.color.white);
        String textOptionOn = toggleData.getTextOptionOn();
        String textOptionOff = toggleData.getTextOptionOff();
        int drawableOn = optionViewHolder.itemView.getContext().getResources().getIdentifier(drawableNameOn, "drawable", optionViewHolder.itemView.getContext().getPackageName());
        int drawableOff = optionViewHolder.itemView.getContext().getResources().getIdentifier(drawableNameOff, "drawable", optionViewHolder.itemView.getContext().getPackageName());
        int drawableId;
        if (toggleData.isOn()) {
            drawableId = drawableOn;
            optionViewHolder.textViewAlbumSettingsOption.setText(textOptionOn);
            optionViewHolder.textViewAlbumSettingsOption.setTextColor(greenText);
        } else {
            drawableId = drawableOff;
            optionViewHolder.textViewAlbumSettingsOption.setText(textOptionOff);
            optionViewHolder.textViewAlbumSettingsOption.setTextColor(whiteText);
        }
        optionViewHolder.imageViewAlbumSettingsOption.setImageResource(drawableId);
    }

    @Override
    public int getItemCount() {
        return albumSettingsItems.size();
    }

    public interface OnItemClickedListener {
        void onItemClickedNavigationOptionListener(TypeDestination type);
    }
}

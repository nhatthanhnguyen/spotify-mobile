package com.ptit.spotify.adapters.album;

import static com.ptit.spotify.utils.ItemType.ALBUM_SETTING_HEADER;
import static com.ptit.spotify.utils.ItemType.BLANK;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ptit.spotify.R;
import com.ptit.spotify.dto.data.AlbumSettingHeaderData;
import com.ptit.spotify.dto.data.SettingOptionData;
import com.ptit.spotify.utils.OnItemAlbumSettingClickedListener;
import com.ptit.spotify.viewholders.album.AlbumSettingHeaderViewHolder;
import com.ptit.spotify.viewholders.album.AlbumSettingOptionViewHolder;
import com.ptit.spotify.viewholders.blank.BlankViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AlbumSettingAdapter extends RecyclerView.Adapter {
    private List<Object> items;
    private OnItemAlbumSettingClickedListener onItemAlbumSettingClickedListener;

    public AlbumSettingAdapter(
            List<Object> items,
            OnItemAlbumSettingClickedListener onItemAlbumSettingClickedListener
    ) {
        this.items = items;
        this.onItemAlbumSettingClickedListener = onItemAlbumSettingClickedListener;
    }

    @Override
    public int getItemViewType(int position) {
        Object item = items.get(position);
        if (item instanceof AlbumSettingHeaderData) {
            return ALBUM_SETTING_HEADER.ordinal();
        } else if (item instanceof SettingOptionData) {
            return ((SettingOptionData) item).getItemType().ordinal();
        }
        return BLANK.ordinal();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        if (viewType == ALBUM_SETTING_HEADER.ordinal()) {
            view = inflater.inflate(R.layout.layout_album_setting_header, parent, false);
            return new AlbumSettingHeaderViewHolder(view);
        }
        if (viewType == BLANK.ordinal()) {
            view = inflater.inflate(R.layout.layout_blank_item, parent, false);
            return new BlankViewHolder(view);
        }
        view = inflater.inflate(R.layout.layout_album_setting_option, parent, false);
        return new AlbumSettingOptionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof AlbumSettingHeaderViewHolder) {
            AlbumSettingHeaderViewHolder viewHolder = (AlbumSettingHeaderViewHolder) holder;
            AlbumSettingHeaderData data = (AlbumSettingHeaderData) items.get(position);
            viewHolder.textViewArtistName.setText(data.getArtistName());
            viewHolder.textViewAlbumName.setText(data.getAlbumName());
            Picasso.get().load(data.getAlbumImageUrl()).into(viewHolder.imageViewAlbumImage);
        }

        if (holder instanceof AlbumSettingOptionViewHolder) {
            AlbumSettingOptionViewHolder viewHolder = (AlbumSettingOptionViewHolder) holder;
            SettingOptionData data = (SettingOptionData) items.get(position);
            viewHolder.imageViewAlbumSettingsOption.setImageResource(data.getIconId());
            viewHolder.textViewAlbumSettingsOption.setText(data.getDescription());
            viewHolder.itemView.setOnClickListener(v -> {
                Toast.makeText(viewHolder.itemView.getContext(), data.getItemType().toString() + " is clicked!", Toast.LENGTH_SHORT).show();
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}

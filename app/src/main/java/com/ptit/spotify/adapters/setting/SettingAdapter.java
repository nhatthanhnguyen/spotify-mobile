package com.ptit.spotify.adapters.setting;

import static com.ptit.spotify.utils.ItemType.ALBUM_SETTING_HEADER;
import static com.ptit.spotify.utils.ItemType.ARTIST_SETTING_HEADER;
import static com.ptit.spotify.utils.ItemType.BLANK;
import static com.ptit.spotify.utils.ItemType.PLAYLIST_SETTING_HEADER;
import static com.ptit.spotify.utils.ItemType.SETTING_OPTION;
import static com.ptit.spotify.utils.ItemType.SONG_SETTING_HEADER;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ptit.spotify.R;
import com.ptit.spotify.dto.data.AlbumSettingHeaderData;
import com.ptit.spotify.dto.data.ArtistSettingHeaderData;
import com.ptit.spotify.dto.data.PlaylistSettingHeaderData;
import com.ptit.spotify.dto.data.SettingOptionData;
import com.ptit.spotify.dto.data.SongSettingHeaderData;
import com.ptit.spotify.utils.OnItemSettingClickedListener;
import com.ptit.spotify.viewholders.blank.BlankViewHolder;
import com.ptit.spotify.viewholders.setting.SettingAlbumHeaderViewHolder;
import com.ptit.spotify.viewholders.setting.SettingArtistHeaderViewHolder;
import com.ptit.spotify.viewholders.setting.SettingOptionViewHolder;
import com.ptit.spotify.viewholders.setting.SettingPlaylistHeaderViewHolder;
import com.ptit.spotify.viewholders.setting.SettingSongHeaderViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SettingAdapter extends RecyclerView.Adapter {
    private List<Object> items;
    private OnItemSettingClickedListener onItemSettingClickedListener;

    public SettingAdapter(
            List<Object> items,
            OnItemSettingClickedListener onItemSettingClickedListener) {
        this.items = items;
        this.onItemSettingClickedListener = onItemSettingClickedListener;
    }

    @Override
    public int getItemViewType(int position) {
        Object item = items.get(position);
        if (item instanceof AlbumSettingHeaderData) {
            return ALBUM_SETTING_HEADER.ordinal();
        }
        if (item instanceof PlaylistSettingHeaderData) {
            return PLAYLIST_SETTING_HEADER.ordinal();
        }
        if (item instanceof ArtistSettingHeaderData) {
            return ARTIST_SETTING_HEADER.ordinal();
        }
        if (item instanceof SongSettingHeaderData) {
            return SONG_SETTING_HEADER.ordinal();
        }
        if (item instanceof SettingOptionData) {
            return SETTING_OPTION.ordinal();
        }
        return BLANK.ordinal();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view;
        if (viewType == ALBUM_SETTING_HEADER.ordinal()) {
            view = layoutInflater.inflate(R.layout.layout_setting_album_header, parent, false);
            return new SettingAlbumHeaderViewHolder(view);
        }
        if (viewType == ARTIST_SETTING_HEADER.ordinal()) {
            view = layoutInflater.inflate(R.layout.layout_setting_artist_header, parent, false);
            return new SettingArtistHeaderViewHolder(view);
        }
        if (viewType == SONG_SETTING_HEADER.ordinal()) {
            view = layoutInflater.inflate(R.layout.layout_setting_song_header, parent, false);
            return new SettingSongHeaderViewHolder(view);
        }
        if (viewType == PLAYLIST_SETTING_HEADER.ordinal()) {
            view = layoutInflater.inflate(R.layout.layout_setting_playlist_header, parent, false);
            return new SettingPlaylistHeaderViewHolder(view);
        }
        if (viewType == SETTING_OPTION.ordinal()) {
            view = layoutInflater.inflate(R.layout.layout_setting_option, parent, false);
            return new SettingOptionViewHolder(view);
        }
        view = layoutInflater.inflate(R.layout.layout_blank_item, parent, false);
        return new BlankViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SettingAlbumHeaderViewHolder) {
            SettingAlbumHeaderViewHolder viewHolder = (SettingAlbumHeaderViewHolder) holder;
            AlbumSettingHeaderData data = (AlbumSettingHeaderData) items.get(position);
            viewHolder.textViewAlbumName.setText(data.getAlbumName());
            viewHolder.textViewArtistName.setText(data.getArtistName());
            Picasso.get().load(data.getAlbumImageUrl()).into(viewHolder.imageViewAlbum);
            return;
        }

        if (holder instanceof SettingArtistHeaderViewHolder) {
            SettingArtistHeaderViewHolder viewHolder = (SettingArtistHeaderViewHolder) holder;
            ArtistSettingHeaderData data = (ArtistSettingHeaderData) items.get(position);
            viewHolder.textViewArtistName.setText(data.getArtistName());
            Picasso.get().load(data.getArtistImageUrl()).into(viewHolder.imageViewArtist);
            return;
        }

        if (holder instanceof SettingPlaylistHeaderViewHolder) {
            SettingPlaylistHeaderViewHolder viewHolder = (SettingPlaylistHeaderViewHolder) holder;
            PlaylistSettingHeaderData data = (PlaylistSettingHeaderData) items.get(position);
            viewHolder.textViewPlaylistName.setText(data.getPlaylistName());
            viewHolder.textViewUserCreated.setText(data.getUserCreatedName());
            Picasso.get().load(data.getPlaylistImageUrl()).into(viewHolder.imageViewPlaylist);
            return;
        }

        if (holder instanceof SettingSongHeaderViewHolder) {
            SettingSongHeaderViewHolder viewHolder = (SettingSongHeaderViewHolder) holder;
            SongSettingHeaderData data = (SongSettingHeaderData) items.get(position);
            viewHolder.textViewArtistName.setText(data.getArtistName());
            viewHolder.textViewAlbumName.setText(data.getAlbumName());
            viewHolder.textViewSongName.setText(data.getSongName());
            Picasso.get().load(data.getImageSongUrl()).into(viewHolder.imageViewSong);
            return;
        }

        if (holder instanceof SettingOptionViewHolder) {
            SettingOptionViewHolder viewHolder = (SettingOptionViewHolder) holder;
            SettingOptionData data = (SettingOptionData) items.get(position);
            viewHolder.textViewSetting.setText(data.getDescription());
            viewHolder.imageViewSetting.setImageResource(data.getIconId());
            viewHolder.itemView.setOnClickListener(v -> {
                onItemSettingClickedListener.onSettingItemClickedListener(data);
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}

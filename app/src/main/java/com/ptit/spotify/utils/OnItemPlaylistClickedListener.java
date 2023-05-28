package com.ptit.spotify.utils;

import com.ptit.spotify.dto.data.PlaylistHeaderData;
import com.ptit.spotify.dto.data.PlaylistSongData;

public interface OnItemPlaylistClickedListener {
    void onPlaylistSettingClickedListener(PlaylistHeaderData data);

    void onBackButtonClickedListener();

    void onSearchClickedListener();

    void onSongSettingClickedListener(PlaylistSongData data);
}

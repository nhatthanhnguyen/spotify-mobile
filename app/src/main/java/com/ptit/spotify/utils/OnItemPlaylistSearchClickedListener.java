package com.ptit.spotify.utils;

import com.ptit.spotify.dto.data.PlaylistSongData;

public interface OnItemPlaylistSearchClickedListener {
    void onBackButtonClickedListener();
    void onSongSettingClickedListener(PlaylistSongData data);
}

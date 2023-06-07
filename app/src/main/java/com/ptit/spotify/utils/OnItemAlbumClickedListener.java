package com.ptit.spotify.utils;

import com.ptit.spotify.dto.data.AlbumHeaderData;
import com.ptit.spotify.dto.data.AlbumSongData;

public interface OnItemAlbumClickedListener {
    void onBackButtonClickedListener();
    void onAlbumSettingClickedListener(AlbumHeaderData data);
    void onSongSettingClickedListener(int position, AlbumSongData data);
    void onLikeButtonClickedListener(AlbumHeaderData data);
}

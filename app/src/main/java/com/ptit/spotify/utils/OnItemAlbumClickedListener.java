package com.ptit.spotify.utils;

import com.ptit.spotify.dto.data.AlbumHeaderData;

public interface OnItemAlbumClickedListener {
    void onBackButtonClickedListener();
    void onAlbumSettingClickedListener(AlbumHeaderData data);
}

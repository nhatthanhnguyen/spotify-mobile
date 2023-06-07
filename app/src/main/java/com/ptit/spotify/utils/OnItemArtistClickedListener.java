package com.ptit.spotify.utils;

import com.ptit.spotify.dto.data.ArtistHeaderData;

public interface OnItemArtistClickedListener {
    void onBackButtonClickedListener();
    void onArtistSettingClickedListener(ArtistHeaderData data);
    void onArtistLikeClickedListener(ArtistHeaderData data);
}

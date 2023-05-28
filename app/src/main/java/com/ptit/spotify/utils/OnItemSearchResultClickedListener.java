package com.ptit.spotify.utils;

import com.ptit.spotify.dto.data.SearchItemResultData;

public interface OnItemSearchResultClickedListener {
    void onSongSettingClickedListener(SearchItemResultData data);
    void onItemClickedListener(SearchItemResultData data);
}

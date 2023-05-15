package com.ptit.spotify.dto.data;

import com.ptit.spotify.utils.TypePlaylistSettingsItem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaylistSettingsOptionToggleData {
    private String imageResource;
    private String textOption;
    private TypePlaylistSettingsItem type = TypePlaylistSettingsItem.TOGGLE_OPTION;

    public PlaylistSettingsOptionToggleData(String imageResource, String textOption) {
        this.imageResource = imageResource;
        this.textOption = textOption;
    }
}

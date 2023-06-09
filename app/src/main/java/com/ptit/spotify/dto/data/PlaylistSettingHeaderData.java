package com.ptit.spotify.dto.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaylistSettingHeaderData {
    private String playlistImageUrl;
    private String playlistName;
    private String userCreatedName;
}

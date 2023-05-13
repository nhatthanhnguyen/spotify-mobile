package com.ptit.spotify.dto.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlbumSettingsHeaderData {
    private String albumImageUrl;
    private String albumName;
    private String artistName;
}

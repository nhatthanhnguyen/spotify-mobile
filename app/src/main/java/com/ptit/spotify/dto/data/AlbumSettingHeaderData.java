package com.ptit.spotify.dto.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlbumSettingHeaderData {
    private String albumImageUrl;
    private String albumName;
    private String artistName;
}

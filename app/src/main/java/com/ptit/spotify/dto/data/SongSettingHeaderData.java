package com.ptit.spotify.dto.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SongSettingHeaderData {
    private String imageSongUrl;
    private String songName;
    private String artistName;
    private String albumName;
}

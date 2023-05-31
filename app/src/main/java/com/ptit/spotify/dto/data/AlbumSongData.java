package com.ptit.spotify.dto.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlbumSongData {
    private String id;
    private String name;
    private String imageUrl;
    private String artistName;
    private boolean liked;
    private boolean selected;
}

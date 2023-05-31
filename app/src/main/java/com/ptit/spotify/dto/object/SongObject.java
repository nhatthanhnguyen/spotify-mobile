package com.ptit.spotify.dto.object;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SongObject {
    private int id;
    private String name;
    private ArtistObject artist;
    private int albumId;
    private String lyrics;
    private int length;
    private String url;
    private String youtubeLink;
    private String songCloudId;
}

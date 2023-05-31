package com.ptit.spotify.dto.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlbumHeaderData {
    private int id;
    private String imageUrl;
    private String albumName;
    private int artistId;
    private String artistName;
    private String imageArtistUrl;
    private String albumDateReleased;
    private boolean isLiked;
    private boolean isPlaying;
}

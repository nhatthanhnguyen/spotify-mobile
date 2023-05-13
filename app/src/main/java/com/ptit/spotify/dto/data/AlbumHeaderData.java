package com.ptit.spotify.dto.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlbumHeaderData {
    private String imageUrl;
    private String albumName;
    private String artistName;
    private String imageArtistUrl;
    private String albumDateReleased;

    private boolean isLiked;
    private boolean isLastPlayed;
    private boolean isPlaying;

    public AlbumHeaderData(String imageUrl, String albumName, String artistName, String imageArtistUrl, String albumDateReleased) {
        this.imageUrl = imageUrl;
        this.albumName = albumName;
        this.artistName = artistName;
        this.imageArtistUrl = imageArtistUrl;
        this.albumDateReleased = albumDateReleased;
        this.isLiked = false;
        this.isLastPlayed = false;
        this.isPlaying = false;
    }
}

package com.ptit.spotify.models.data;

public class AlbumSettingsHeaderData {
    private String albumImageUrl;
    private String albumName;
    private String artistName;

    public AlbumSettingsHeaderData(String albumImageUrl, String albumName, String artistName) {
        this.albumImageUrl = albumImageUrl;
        this.albumName = albumName;
        this.artistName = artistName;
    }

    public String getAlbumImageUrl() {
        return albumImageUrl;
    }

    public void setAlbumImageUrl(String albumImageUrl) {
        this.albumImageUrl = albumImageUrl;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }
}

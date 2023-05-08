package com.ptit.spotify.models.data;
public class AlbumHeaderData {
    private String imageUrl;
    private String albumName;
    private String artistName;
    private String imageArtistUrl;
    private String albumDateReleased;

    public AlbumHeaderData(String imageUrl, String albumName, String artistName, String imageArtistUrl, String albumDateReleased) {
        this.imageUrl = imageUrl;
        this.albumName = albumName;
        this.artistName = artistName;
        this.imageArtistUrl = imageArtistUrl;
        this.albumDateReleased = albumDateReleased;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public String getImageArtistUrl() {
        return imageArtistUrl;
    }

    public void setImageArtistUrl(String imageArtistUrl) {
        this.imageArtistUrl = imageArtistUrl;
    }

    public String getAlbumDateReleased() {
        return albumDateReleased;
    }

    public void setAlbumDateReleased(String albumDateReleased) {
        this.albumDateReleased = albumDateReleased;
    }
}

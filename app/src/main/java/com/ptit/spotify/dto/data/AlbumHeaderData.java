package com.ptit.spotify.dto.data;
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

    public AlbumHeaderData(String imageUrl, String albumName, String artistName, String imageArtistUrl, String albumDateReleased, boolean isLiked, boolean isLastPlayed, boolean isPlaying) {
        this.imageUrl = imageUrl;
        this.albumName = albumName;
        this.artistName = artistName;
        this.imageArtistUrl = imageArtistUrl;
        this.albumDateReleased = albumDateReleased;
        this.isLiked = isLiked;
        this.isLastPlayed = isLastPlayed;
        this.isPlaying = isPlaying;
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

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public boolean isLastPlayed() {
        return isLastPlayed;
    }

    public void setLastPlayed(boolean lastPlayed) {
        isLastPlayed = lastPlayed;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }
}

package com.ptit.spotify.dto.data;

public class AlbumSongData {
    private String id;
    private String name;
    private String imageUrl;
    private String artistName;

    public AlbumSongData(String id, String name, String imageUrl, String artistName) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.artistName = artistName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }
}

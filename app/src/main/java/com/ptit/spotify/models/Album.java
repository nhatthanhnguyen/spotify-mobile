package com.ptit.spotify.models;

import com.ptit.spotify.utils.Constants;

import java.util.List;

public class Album {
    private String id;
    private String name;
    private String imageUrl;
    private List<String> genres;
    private Artist artist;
    private String type = Constants.ALBUM;

    public Album(String id, String name, String imageUrl, List<String> genres, Artist artist) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.genres = genres;
        this.artist = artist;
    }

    public Album(String id, String name, String imageUrl, List<String> genres, Artist artist, String type) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.genres = genres;
        this.artist = artist;
        this.type = type;
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

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

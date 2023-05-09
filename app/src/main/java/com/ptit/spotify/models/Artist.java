package com.ptit.spotify.models;

import com.ptit.spotify.utils.Constants;

import java.util.List;

public class Artist {
    private String id;
    private String name;
    private String imageUrl;
    private List<String> genres;
    private String description;
    private String type = Constants.ARTIST;

    public Artist(String id, String name, String imageUrl, List<String> genres, String description) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.genres = genres;
        this.description = description;
    }

    public Artist(String id, String name, String imageUrl, List<String> genres, String description, String type) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.genres = genres;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
